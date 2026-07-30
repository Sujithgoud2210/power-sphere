package com.powersphere.notification.validation;

import com.powersphere.notification.dto.request.CreateNotificationRequest;
import com.powersphere.notification.dto.request.UpdateNotificationRequest;
import com.powersphere.notification.model.NotificationChannel;
import com.powersphere.notification.model.NotificationPriority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class NotificationValidatorTest {

    private NotificationValidator validator;

    @BeforeEach
    void setUp() {
        validator = new NotificationValidator();
    }

    @Test
    void validateCreate_ShouldPass_WhenAllFieldsValid() {
        CreateNotificationRequest request = CreateNotificationRequest.builder()
                .title("Test Notification")
                .message("Test message")
                .notificationType(NotificationChannel.EMAIL)
                .priority(NotificationPriority.HIGH)
                .channel(NotificationChannel.EMAIL)
                .recipientUser(1L)
                .build();

        assertDoesNotThrow(() -> validator.validateCreate(request));
    }

    @Test
    void validateCreate_ShouldThrow_WhenTitleMissing() {
        CreateNotificationRequest request = CreateNotificationRequest.builder()
                .title("")
                .message("Test message")
                .notificationType(NotificationChannel.EMAIL)
                .priority(NotificationPriority.HIGH)
                .channel(NotificationChannel.EMAIL)
                .recipientUser(1L)
                .build();

        assertThrows(IllegalArgumentException.class,
                () -> validator.validateCreate(request));
    }

    @Test
    void validateCreate_ShouldThrow_WhenMessageMissing() {
        CreateNotificationRequest request = CreateNotificationRequest.builder()
                .title("Test")
                .message("")
                .notificationType(NotificationChannel.EMAIL)
                .priority(NotificationPriority.HIGH)
                .channel(NotificationChannel.EMAIL)
                .recipientUser(1L)
                .build();

        assertThrows(IllegalArgumentException.class,
                () -> validator.validateCreate(request));
    }

    @Test
    void validateCreate_ShouldThrow_WhenTypeNull() {
        CreateNotificationRequest request = CreateNotificationRequest.builder()
                .title("Test")
                .message("Message")
                .notificationType(null)
                .priority(NotificationPriority.HIGH)
                .channel(NotificationChannel.EMAIL)
                .recipientUser(1L)
                .build();

        assertThrows(IllegalArgumentException.class,
                () -> validator.validateCreate(request));
    }

    @Test
    void validateCreate_ShouldThrow_WhenNoRecipient() {
        CreateNotificationRequest request = CreateNotificationRequest.builder()
                .title("Test")
                .message("Message")
                .notificationType(NotificationChannel.EMAIL)
                .priority(NotificationPriority.HIGH)
                .channel(NotificationChannel.EMAIL)
                .recipientUser(null)
                .recipientEmail(null)
                .recipientPhone(null)
                .build();

        assertThrows(IllegalArgumentException.class,
                () -> validator.validateCreate(request));
    }

    @Test
    void validateCreate_ShouldPass_WithEmailRecipient() {
        CreateNotificationRequest request = CreateNotificationRequest.builder()
                .title("Test")
                .message("Message")
                .notificationType(NotificationChannel.EMAIL)
                .priority(NotificationPriority.HIGH)
                .channel(NotificationChannel.EMAIL)
                .recipientEmail("user@example.com")
                .build();

        assertDoesNotThrow(() -> validator.validateCreate(request));
    }

    @Test
    void validateCreate_ShouldPass_WithPhoneRecipient() {
        CreateNotificationRequest request = CreateNotificationRequest.builder()
                .title("Test")
                .message("Message")
                .notificationType(NotificationChannel.SMS)
                .priority(NotificationPriority.HIGH)
                .channel(NotificationChannel.SMS)
                .recipientPhone("+1234567890")
                .build();

        assertDoesNotThrow(() -> validator.validateCreate(request));
    }

    @Test
    void validateUpdate_ShouldPass_WhenAllFieldsValid() {
        UpdateNotificationRequest request = UpdateNotificationRequest.builder()
                .title("Updated Title")
                .message("Updated message")
                .build();

        assertDoesNotThrow(() -> validator.validateUpdate(request));
    }

    @Test
    void validateUpdate_ShouldThrow_WhenTitleEmpty() {
        UpdateNotificationRequest request = UpdateNotificationRequest.builder()
                .title("   ")
                .build();

        assertThrows(IllegalArgumentException.class,
                () -> validator.validateUpdate(request));
    }

    @Test
    void validateUpdate_ShouldThrow_WhenMessageEmpty() {
        UpdateNotificationRequest request = UpdateNotificationRequest.builder()
                .message("   ")
                .build();

        assertThrows(IllegalArgumentException.class,
                () -> validator.validateUpdate(request));
    }

    @Test
    void validateScheduledNotification_ShouldThrow_WhenTimeInPast() {
        CreateNotificationRequest request = CreateNotificationRequest.builder()
                .title("Test")
                .message("Message")
                .notificationType(NotificationChannel.EMAIL)
                .priority(NotificationPriority.HIGH)
                .channel(NotificationChannel.EMAIL)
                .recipientUser(1L)
                .scheduledTime(LocalDateTime.now().minusHours(1))
                .build();

        assertThrows(IllegalArgumentException.class,
                () -> validator.validateScheduledNotification(request));
    }

    @Test
    void validateScheduledNotification_ShouldThrow_WhenTimeNull() {
        CreateNotificationRequest request = CreateNotificationRequest.builder()
                .title("Test")
                .message("Message")
                .notificationType(NotificationChannel.EMAIL)
                .priority(NotificationPriority.HIGH)
                .channel(NotificationChannel.EMAIL)
                .recipientUser(1L)
                .scheduledTime(null)
                .build();

        assertThrows(IllegalArgumentException.class,
                () -> validator.validateScheduledNotification(request));
    }
}
