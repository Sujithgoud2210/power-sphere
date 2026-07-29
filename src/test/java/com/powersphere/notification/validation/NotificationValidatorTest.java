package com.powersphere.notification.validation;

import com.powersphere.notification.dto.request.SendNotificationRequest;
import com.powersphere.notification.enums.NotificationChannel;
import com.powersphere.notification.enums.NotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link NotificationValidator}.
 */
class NotificationValidatorTest {

    private NotificationValidator validator;

    @BeforeEach
    void setUp() {
        validator = new NotificationValidator();
    }

    @Test
    void shouldPassValidationForValidEmailRequest() {
        // Given
        SendNotificationRequest request = new SendNotificationRequest();
        request.setType(NotificationType.SYSTEM_ALERT);
        request.setChannel(NotificationChannel.EMAIL);
        request.setRecipientId(1L);
        request.setRecipientEmail("user@example.com");
        request.setContent("Test content");

        // When
        List<String> errors = validator.validate(request);

        // Then
        assertThat(errors).isEmpty();
    }

    @Test
    void shouldFailWhenTypeIsMissing() {
        // Given
        SendNotificationRequest request = new SendNotificationRequest();
        request.setChannel(NotificationChannel.IN_APP);
        request.setRecipientId(1L);
        request.setContent("Test");

        // When
        List<String> errors = validator.validate(request);

        // Then
        assertThat(errors).contains("Notification type is required");
    }

    @Test
    void shouldFailWhenChannelIsMissing() {
        // Given
        SendNotificationRequest request = new SendNotificationRequest();
        request.setType(NotificationType.SYSTEM_ALERT);
        request.setRecipientId(1L);
        request.setContent("Test");

        // When
        List<String> errors = validator.validate(request);

        // Then
        assertThat(errors).contains("Notification channel is required");
    }

    @Test
    void shouldFailWhenRecipientIdIsMissing() {
        // Given
        SendNotificationRequest request = new SendNotificationRequest();
        request.setType(NotificationType.SYSTEM_ALERT);
        request.setChannel(NotificationChannel.IN_APP);
        request.setContent("Test");

        // When
        List<String> errors = validator.validate(request);

        // Then
        assertThat(errors).contains("Recipient ID is required");
    }

    @Test
    void shouldFailForEmailChannelWithoutEmail() {
        // Given
        SendNotificationRequest request = new SendNotificationRequest();
        request.setType(NotificationType.SYSTEM_ALERT);
        request.setChannel(NotificationChannel.EMAIL);
        request.setRecipientId(1L);
        request.setContent("Test");

        // When
        List<String> errors = validator.validate(request);

        // Then
        assertThat(errors).contains("Recipient email is required for EMAIL notifications");
    }

    @Test
    void shouldFailWithInvalidEmailFormat() {
        // Given
        SendNotificationRequest request = new SendNotificationRequest();
        request.setType(NotificationType.SYSTEM_ALERT);
        request.setChannel(NotificationChannel.EMAIL);
        request.setRecipientId(1L);
        request.setRecipientEmail("invalid-email");
        request.setContent("Test");

        // When
        List<String> errors = validator.validate(request);

        // Then
        assertThat(errors).contains("Invalid recipient email format");
    }

    @Test
    void shouldFailWhenBothContentAndTemplateAreMissing() {
        // Given
        SendNotificationRequest request = new SendNotificationRequest();
        request.setType(NotificationType.SYSTEM_ALERT);
        request.setChannel(NotificationChannel.EMAIL);
        request.setRecipientId(1L);
        request.setRecipientEmail("user@example.com");

        // When
        List<String> errors = validator.validate(request);

        // Then
        assertThat(errors).contains("Either content or templateCode must be provided");
    }
}
