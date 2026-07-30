package com.powersphere.notification.exception;

/**
 * Exception thrown when a notification template cannot be found.
 */
public class NotificationTemplateNotFoundException extends NotificationException {

    public NotificationTemplateNotFoundException(String code) {
        super("Notification template not found with code: " + code, "TEMPLATE_NOT_FOUND");
    }
}
