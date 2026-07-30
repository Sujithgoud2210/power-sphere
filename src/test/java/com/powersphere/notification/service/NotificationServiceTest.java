package com.powersphere.notification.service;

import com.powersphere.notification.dto.request.CreateNotificationRequest;
import com.powersphere.notification.dto.request.UpdateNotificationRequest;
import com.powersphere.notification.dto.response.NotificationResponse;
import com.powersphere.notification.dto.response.PagedResponse;
import com.powersphere.notification.entity.Notification;
import com.powersphere.notification.exception.NotificationNotFoundException;
import com.powersphere.notification.mapper.NotificationMapper;
import com.powersphere.notification.model.NotificationChannel;
import com.powersphere.notification.model.NotificationPriority;
import com.powersphere.notification.model.NotificationStatus;
import com.powersphere.notification.repository.NotificationRepository;
import com.powersphere.notification.service.impl.NotificationServiceImpl;
import com.powersphere.notification.validation.NotificationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationMapper notificationMapper;

    @Mock
    private NotificationValidator notificationValidator;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        notificationService = new NotificationServiceImpl(
                notificationRepository, notificationMapper,
                notificationValidator, eventPublisher);
    }

    @Test
    void createNotification_ShouldReturnResponse() {
        CreateNotificationRequest request = CreateNotificationRequest.builder()
                .title("Test Notification")
                .message("Test message")
                .notificationType(NotificationChannel.EMAIL)
                .priority(NotificationPriority.HIGH)
                .channel(NotificationChannel.EMAIL)
                .build();

        Notification notification = Notification.builder()
                .id(1L)
                .title("Test Notification")
                .message("Test message")
                .notificationType(NotificationChannel.EMAIL)
                .priority(NotificationPriority.HIGH)
                .status(NotificationStatus.PENDING)
                .channel(NotificationChannel.EMAIL)
                .createdAt(LocalDateTime.now())
                .build();

        NotificationResponse response = NotificationResponse.builder()
                .id(1L)
                .title("Test Notification")
                .message("Test message")
                .notificationType("EMAIL")
                .priority("HIGH")
                .status("PENDING")
                .channel("EMAIL")
                .build();

        when(notificationMapper.toEntity(request)).thenReturn(notification);
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
        when(notificationMapper.toResponse(notification)).thenReturn(response);

        NotificationResponse result = notificationService.createNotification(request);

        assertNotNull(result);
        assertEquals("Test Notification", result.getTitle());
        assertEquals("HIGH", result.getPriority());
        verify(notificationValidator).validateCreate(request);
        verify(eventPublisher).publishEvent(any());
    }

    @Test
    void getNotificationById_ShouldReturnResponse() {
        Notification notification = Notification.builder()
                .id(1L)
                .title("Test")
                .build();

        NotificationResponse response = NotificationResponse.builder()
                .id(1L)
                .title("Test")
                .build();

        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
        when(notificationMapper.toResponse(notification)).thenReturn(response);

        NotificationResponse result = notificationService.getNotificationById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test", result.getTitle());
    }

    @Test
    void getNotificationById_ShouldThrowException_WhenNotFound() {
        when(notificationRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NotificationNotFoundException.class,
                () -> notificationService.getNotificationById(999L));
    }

    @Test
    void updateNotification_ShouldReturnUpdatedResponse() {
        UpdateNotificationRequest request = UpdateNotificationRequest.builder()
                .title("Updated Title")
                .build();

        Notification notification = Notification.builder()
                .id(1L)
                .title("Original Title")
                .build();

        NotificationResponse response = NotificationResponse.builder()
                .id(1L)
                .title("Updated Title")
                .build();

        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
        when(notificationMapper.toResponse(notification)).thenReturn(response);

        NotificationResponse result = notificationService.updateNotification(1L, request);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        verify(notificationValidator).validateUpdate(request);
    }

    @Test
    void deleteNotification_ShouldDeleteSuccessfully() {
        Notification notification = Notification.builder().id(1L).build();
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

        notificationService.deleteNotification(1L);

        verify(notificationRepository).delete(notification);
    }

    @Test
    void deleteNotification_ShouldThrowException_WhenNotFound() {
        when(notificationRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NotificationNotFoundException.class,
                () -> notificationService.deleteNotification(999L));
    }

    @Test
    void markAsRead_ShouldUpdateStatus() {
        Notification notification = Notification.builder()
                .id(1L)
                .status(NotificationStatus.PENDING)
                .build();

        NotificationResponse response = NotificationResponse.builder()
                .id(1L)
                .status("READ")
                .build();

        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
        when(notificationMapper.toResponse(notification)).thenReturn(response);

        NotificationResponse result = notificationService.markAsRead(1L);

        assertNotNull(result);
        assertEquals("READ", result.getStatus());
        verify(eventPublisher).publishEvent(any());
    }

    @Test
    void markAsRead_ShouldThrowException_WhenAlreadyRead() {
        Notification notification = Notification.builder()
                .id(1L)
                .status(NotificationStatus.READ)
                .build();

        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

        assertThrows(IllegalStateException.class,
                () -> notificationService.markAsRead(1L));
    }

    @Test
    void cancelNotification_ShouldSetFailedStatus() {
        Notification notification = Notification.builder()
                .id(1L)
                .status(NotificationStatus.PENDING)
                .build();

        NotificationResponse response = NotificationResponse.builder()
                .id(1L)
                .status("FAILED")
                .remarks("Notification cancelled by user")
                .build();

        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
        when(notificationMapper.toResponse(notification)).thenReturn(response);

        NotificationResponse result = notificationService.cancelNotification(1L);

        assertNotNull(result);
        assertEquals("FAILED", result.getStatus());
    }

    @Test
    void cancelNotification_ShouldThrowException_WhenAlreadySent() {
        Notification notification = Notification.builder()
                .id(1L)
                .status(NotificationStatus.SENT)
                .build();

        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

        assertThrows(IllegalStateException.class,
                () -> notificationService.cancelNotification(1L));
    }

    @Test
    void getAllNotifications_ShouldReturnPagedResponse() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Notification> page = new PageImpl<>(Collections.emptyList());

        when(notificationRepository.findAll(pageable)).thenReturn(page);

        PagedResponse<NotificationResponse> result = notificationService.getAllNotifications(pageable);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
    }
}
