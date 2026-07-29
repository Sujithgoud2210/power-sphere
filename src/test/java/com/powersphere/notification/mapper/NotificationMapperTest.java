package com.powersphere.notification.mapper;

import com.powersphere.notification.dto.request.SendNotificationRequest;
import com.powersphere.notification.dto.response.NotificationResponse;
import com.powersphere.notification.entity.Notification;
import com.powersphere.notification.enums.NotificationChannel;
import com.powersphere.notification.enums.NotificationPriority;
import com.powersphere.notification.enums.NotificationStatus;
import com.powersphere.notification.enums.NotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link NotificationMapper}.
 */
class NotificationMapperTest {

    private NotificationMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new NotificationMapper();
    }

    @Test
    void shouldMapRequestToEntity() {
        // Given
        SendNotificationRequest request = new SendNotificationRequest();
        request.setType(NotificationType.SYSTEM_ALERT);
        request.setChannel(NotificationChannel.EMAIL);
        request.setPriority(NotificationPriority.HIGH);
        request.setSubject("Test Subject");
        request.setContent("Test content");
        request.setRecipientId(1L);
        request.setRecipientEmail("user@example.com");
        request.setSenderId(100L);
        request.setSenderEmail("sender@example.com");

        // When
        Notification entity = mapper.toEntity(request);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getType()).isEqualTo(NotificationType.SYSTEM_ALERT);
        assertThat(entity.getChannel()).isEqualTo(NotificationChannel.EMAIL);
        assertThat(entity.getPriority()).isEqualTo(NotificationPriority.HIGH);
        assertThat(entity.getSubject()).isEqualTo("Test Subject");
        assertThat(entity.getContent()).isEqualTo("Test content");
        assertThat(entity.getRecipientId()).isEqualTo(1L);
        assertThat(entity.getRecipientEmail()).isEqualTo("user@example.com");
        assertThat(entity.getSenderId()).isEqualTo(100L);
        assertThat(entity.getSenderEmail()).isEqualTo("sender@example.com");
        assertThat(entity.getMetadata()).isNotNull();
        assertThat(entity.getAttachments()).isNotNull();
    }

    @Test
    void shouldMapEntityToResponse() {
        // Given
        Notification entity = Notification.builder()
                .id(1L)
                .type(NotificationType.SYSTEM_ALERT)
                .status(NotificationStatus.PENDING)
                .priority(NotificationPriority.HIGH)
                .channel(NotificationChannel.EMAIL)
                .subject("Test Subject")
                .content("Test content")
                .recipientId(1L)
                .recipientEmail("user@example.com")
                .senderId(100L)
                .senderEmail("sender@example.com")
                .metadata(Collections.singletonMap("key", "value"))
                .attachments(Collections.singletonList("http://example.com/file.pdf"))
                .build();

        // When
        NotificationResponse response = mapper.toResponse(entity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getType()).isEqualTo(NotificationType.SYSTEM_ALERT);
        assertThat(response.getStatus()).isEqualTo(NotificationStatus.PENDING);
        assertThat(response.getPriority()).isEqualTo(NotificationPriority.HIGH);
        assertThat(response.getChannel()).isEqualTo(NotificationChannel.EMAIL);
        assertThat(response.getSubject()).isEqualTo("Test Subject");
        assertThat(response.getRecipientEmail()).isEqualTo("user@example.com");
        assertThat(response.getMetadata()).containsEntry("key", "value");
        assertThat(response.getAttachments()).contains("http://example.com/file.pdf");
    }

    @Test
    void shouldReturnNullForNullEntity() {
        assertThat(mapper.toResponse(null)).isNull();
        assertThat(mapper.toTemplateResponse(null)).isNull();
        assertThat(mapper.toPreferenceResponse(null)).isNull();
    }
}
