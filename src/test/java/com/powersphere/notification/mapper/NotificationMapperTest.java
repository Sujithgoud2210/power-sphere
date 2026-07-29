package com.powersphere.notification.mapper;

import com.powersphere.notification.dto.request.CreateNotificationRequest;
import com.powersphere.notification.dto.request.UpdateNotificationRequest;
import com.powersphere.notification.dto.response.NotificationResponse;
import com.powersphere.notification.entity.Notification;
import com.powersphere.notification.model.NotificationChannel;
import com.powersphere.notification.model.NotificationPriority;
import com.powersphere.notification.model.NotificationStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NotificationMapperTest {

    private final NotificationMapper mapper = Mappers.getMapper(NotificationMapper.class);

    @Test
    void toEntity_ShouldMapAllFields() {
        CreateNotificationRequest request = CreateNotificationRequest.builder()
                .title("Test Notification")
                .message("This is a test message")
                .recipientUser(1L)
                .recipientEmail("user@example.com")
                .notificationType(NotificationChannel.EMAIL)
                .priority(NotificationPriority.HIGH)
                .channel(NotificationChannel.EMAIL)
                .remarks("Test remarks")
                .billId(100L)
                .energyAlertId(200L)
                .meterEventId(300L)
                .build();

        Notification entity = mapper.toEntity(request);

        assertNotNull(entity);
        assertEquals("Test Notification", entity.getTitle());
        assertEquals("This is a test message", entity.getMessage());
        assertEquals(1L, entity.getRecipientUser());
        assertEquals("user@example.com", entity.getRecipientEmail());
        assertEquals(NotificationChannel.EMAIL, entity.getNotificationType());
        assertEquals(NotificationPriority.HIGH, entity.getPriority());
        assertEquals(NotificationChannel.EMAIL, entity.getChannel());
        assertEquals("Test remarks", entity.getRemarks());
        assertEquals(100L, entity.getBillId());
        assertEquals(200L, entity.getEnergyAlertId());
        assertEquals(300L, entity.getMeterEventId());
    }

    @Test
    void toEntity_ShouldHandleNullScheduledTime() {
        CreateNotificationRequest request = CreateNotificationRequest.builder()
                .title("Test")
                .message("Message")
                .notificationType(NotificationChannel.EMAIL)
                .priority(NotificationPriority.LOW)
                .channel(NotificationChannel.EMAIL)
                .build();

        Notification entity = mapper.toEntity(request);

        assertNull(entity.getScheduledTime());
    }

    @Test
    void toResponse_ShouldMapAllFields() {
        Notification entity = Notification.builder()
                .id(1L)
                .title("Test Notification")
                .message("Test message")
                .recipientUser(1L)
                .recipientEmail("user@example.com")
                .notificationType(NotificationChannel.EMAIL)
                .priority(NotificationPriority.CRITICAL)
                .status(NotificationStatus.PENDING)
                .channel(NotificationChannel.SMS)
                .scheduledTime(LocalDateTime.of(2026, 8, 1, 10, 0))
                .sentTime(LocalDateTime.of(2026, 8, 1, 10, 5))
                .readTime(LocalDateTime.of(2026, 8, 1, 11, 0))
                .retryCount(0)
                .remarks("Test remarks")
                .billId(100L)
                .energyAlertId(200L)
                .meterEventId(300L)
                .createdAt(LocalDateTime.of(2026, 8, 1, 9, 0))
                .updatedAt(LocalDateTime.of(2026, 8, 1, 9, 30))
                .build();

        NotificationResponse response = mapper.toResponse(entity);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test Notification", response.getTitle());
        assertEquals("Test message", response.getMessage());
        assertEquals(1L, response.getRecipientUser());
        assertEquals("user@example.com", response.getRecipientEmail());
        assertEquals("EMAIL", response.getNotificationType());
        assertEquals("CRITICAL", response.getPriority());
        assertEquals("PENDING", response.getStatus());
        assertEquals("SMS", response.getChannel());
        assertNotNull(response.getScheduledTime());
        assertNotNull(response.getSentTime());
        assertNotNull(response.getReadTime());
        assertEquals(0, response.getRetryCount());
        assertEquals("Test remarks", response.getRemarks());
        assertEquals(100L, response.getBillId());
        assertEquals(200L, response.getEnergyAlertId());
        assertEquals(300L, response.getMeterEventId());
    }

    @Test
    void updateEntity_ShouldUpdateNonNullFields() {
        Notification entity = Notification.builder()
                .id(1L)
                .title("Original Title")
                .message("Original message")
                .priority(NotificationPriority.LOW)
                .build();

        UpdateNotificationRequest request = UpdateNotificationRequest.builder()
                .title("Updated Title")
                .priority(NotificationPriority.HIGH)
                .build();

        mapper.updateEntity(entity, request);

        assertEquals("Updated Title", entity.getTitle());
        assertEquals(NotificationPriority.HIGH, entity.getPriority());
        assertEquals("Original message", entity.getMessage());
    }
}
