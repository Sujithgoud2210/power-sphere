package com.powersphere.notification.validation;

import com.powersphere.notification.dto.request.CreateNotificationRequest;
import com.powersphere.notification.dto.request.UpdateNotificationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class NotificationValidator {

    public void validateCreate(CreateNotificationRequest request) {
        if (!StringUtils.hasText(request.getTitle())) {
            throw new IllegalArgumentException("Notification title must not be empty");
        }
        if (!StringUtils.hasText(request.getMessage())) {
            throw new IllegalArgumentException("Notification message must not be empty");
        }
        if (request.getNotificationType() == null) {
            throw new IllegalArgumentException("Notification type is required");
        }
        if (request.getPriority() == null) {
            throw new IllegalArgumentException("Notification priority is required");
        }
        if (request.getChannel() == null) {
            throw new IllegalArgumentException("Notification channel is required");
        }
        if (request.getRecipientUser() == null
                && !StringUtils.hasText(request.getRecipientEmail())
                && !StringUtils.hasText(request.getRecipientPhone())) {
            throw new IllegalArgumentException(
                    "At least one recipient (userId, email, or phone) must be provided");
        }
    }

    public void validateUpdate(UpdateNotificationRequest request) {
        if (request.getTitle() != null && request.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Notification title must not be empty");
        }
        if (request.getMessage() != null && request.getMessage().trim().isEmpty()) {
            throw new IllegalArgumentException("Notification message must not be empty");
        }
    }

    public void validateScheduledNotification(CreateNotificationRequest request) {
        if (request.getScheduledTime() == null) {
            throw new IllegalArgumentException("Scheduled time is required for scheduled notifications");
        }
        if (request.getScheduledTime().isBefore(java.time.LocalDateTime.now())) {
            throw new IllegalArgumentException("Scheduled time must be in the future");
        }
    }
}
