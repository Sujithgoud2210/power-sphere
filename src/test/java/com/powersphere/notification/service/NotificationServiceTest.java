package com.powersphere.notification.service;

import com.powersphere.notification.dto.request.SendNotificationRequest;
import com.powersphere.notification.dto.response.NotificationResponse;
import com.powersphere.notification.entity.Notification;
import com.powersphere.notification.enums.NotificationChannel;
import com.powersphere.notification.enums.NotificationPriority;
import com.powersphere.notification.enums.NotificationStatus;
import com.powersphere.notification.enums.NotificationType;
import com.powersphere.notification.mapper.NotificationMapper;
import com.powersphere.notification.repository.NotificationRepository;
import com.powersphere.notification.repository.NotificationTemplateRepository;
import com.powersphere.notification.service.impl.NotificationServiceImpl;
import com.powersphere.notification.util.NotificationTemplateEngine;
import com.powersphere.notification.validation.NotificationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link NotificationServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationTemplateRepository templateRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private NotificationMapper mapper;
    private NotificationValidator validator;
    private NotificationTemplateEngine templateEngine;
    private NotificationServiceImpl notificationService;

    @BeforeEach
    void setUp() {
        mapper = new NotificationMapper();
        validator = new NotificationValidator();
        templateEngine = new NotificationTemplateEngine();
        notificationService = new NotificationServiceImpl(
                notificationRepository,
                templateRepository,
                mapper,
                validator,
                templateEngine,
                eventPublisher);
    }

    @Test
    void shouldSendNotificationSuccessfully() {
        // Given
        SendNotificationRequest request = createValidRequest();
        Notification savedNotification = createNotificationEntity();

        when(notificationRepository.save(any(Notification.class))).thenReturn(savedNotification);

        // When
        NotificationResponse response = notificationService.sendNotification(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getType()).isEqualTo(NotificationType.SYSTEM_ALERT);
        assertThat(response.getChannel()).isEqualTo(NotificationChannel.EMAIL);
        assertThat(response.getRecipientId()).isEqualTo(1L);
        assertThat(response.getSubject()).isEqualTo("Test Subject");
        assertThat(response.getContent()).isEqualTo("Test content");

        verify(notificationRepository, times(1)).save(any(Notification.class));
        verify(eventPublisher, times(1)).publishEvent(any());
    }

    @Test
    void shouldThrowExceptionWhenRequestIsInvalid() {
        // Given
        SendNotificationRequest request = new SendNotificationRequest();
        request.setChannel(NotificationChannel.EMAIL);
        request.setRecipientEmail("invalid-email");

        // When & Then
        assertThatThrownBy(() -> notificationService.sendNotification(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Validation failed");
    }

    @Test
    void shouldMarkNotificationAsRead() {
        // Given
        Long notificationId = 1L;
        Long recipientId = 1L;
        Notification notification = createNotificationEntity();

        when(notificationRepository.findByIdAndRecipientId(notificationId, recipientId))
                .thenReturn(java.util.Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        // When
        NotificationResponse response = notificationService.markAsRead(notificationId, recipientId);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getReadAt()).isNotNull();

        verify(notificationRepository, times(1)).findByIdAndRecipientId(notificationId, recipientId);
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void shouldArchiveNotification() {
        // Given
        Long notificationId = 1L;
        Notification notification = createNotificationEntity();

        when(notificationRepository.findById(notificationId))
                .thenReturn(java.util.Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        // When
        NotificationResponse response = notificationService.archiveNotification(notificationId);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(NotificationStatus.ARCHIVED);

        verify(notificationRepository, times(1)).findById(notificationId);
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    private SendNotificationRequest createValidRequest() {
        SendNotificationRequest request = new SendNotificationRequest();
        request.setType(NotificationType.SYSTEM_ALERT);
        request.setChannel(NotificationChannel.EMAIL);
        request.setSubject("Test Subject");
        request.setContent("Test content");
        request.setRecipientId(1L);
        request.setRecipientEmail("test@example.com");
        request.setSenderId(100L);
        request.setSenderEmail("sender@example.com");
        request.setPriority(NotificationPriority.HIGH);
        return request;
    }

    private Notification createNotificationEntity() {
        return Notification.builder()
                .id(1L)
                .type(NotificationType.SYSTEM_ALERT)
                .status(NotificationStatus.PENDING)
                .priority(NotificationPriority.HIGH)
                .channel(NotificationChannel.EMAIL)
                .subject("Test Subject")
                .content("Test content")
                .senderId(100L)
                .senderEmail("sender@example.com")
                .recipientId(1L)
                .recipientEmail("test@example.com")
                .build();
    }
}
