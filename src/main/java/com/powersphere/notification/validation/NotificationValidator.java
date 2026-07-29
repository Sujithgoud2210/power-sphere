package com.powersphere.notification.validation;

import com.powersphere.notification.dto.request.SendNotificationRequest;
import com.powersphere.notification.enums.NotificationChannel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Validator class for notification-related requests. Provides validation logic
 * to ensure notification requests contain all required fields and valid data
 * before processing.
 */
@Component
public class NotificationValidator {

    /**
     * Validates a {@link SendNotificationRequest} and returns a list of validation
     * error messages. Returns an empty list if the request is valid.
     *
     * @param request the notification request to validate
     * @return list of validation error messages, empty if valid
     */
    public List<String> validate(SendNotificationRequest request) {
        List<String> errors = new ArrayList<>();

        if (request == null) {
            errors.add("Notification request must not be null");
            return errors;
        }

        // Type validation
        if (request.getType() == null) {
            errors.add("Notification type is required");
        }

        // Channel validation
        if (request.getChannel() == null) {
            errors.add("Notification channel is required");
        }

        // Recipient validation - require at minimum a recipient ID
        if (request.getRecipientId() == null) {
            errors.add("Recipient ID is required");
        }

        // Channel-specific validations
        if (request.getChannel() != null) {
            switch (request.getChannel()) {
                case EMAIL:
                    if (request.getRecipientEmail() == null || request.getRecipientEmail().isBlank()) {
                        errors.add("Recipient email is required for EMAIL notifications");
                    } else if (!isValidEmail(request.getRecipientEmail())) {
                        errors.add("Invalid recipient email format");
                    }
                    break;
                case SMS:
                    if (request.getRecipientPhone() == null || request.getRecipientPhone().isBlank()) {
                        errors.add("Recipient phone number is required for SMS notifications");
                    }
                    break;
                default:
                    break;
            }
        }

        // Content validation - require either content or a template code
        if ((request.getContent() == null || request.getContent().isBlank())
                && (request.getTemplateCode() == null || request.getTemplateCode().isBlank())) {
            errors.add("Either content or templateCode must be provided");
        }

        // Subject validation
        if (request.getSubject() != null && request.getSubject().length() > 500) {
            errors.add("Subject must not exceed 500 characters");
        }

        // Max retries validation
        if (request.getMaxRetries() < 0) {
            errors.add("Max retries must be a non-negative number");
        }

        return errors;
    }

    /**
     * Validates an email address format.
     */
    private boolean isValidEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}
