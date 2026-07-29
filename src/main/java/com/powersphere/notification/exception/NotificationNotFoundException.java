package com.powersphere.notification.exception;

/**
 * Exception thrown when a requested notification resource cannot be found.
 */
public class NotificationNotFoundException extends NotificationException {

    public NotificationNotFoundException(Long id) {
        super("Notification not found with id: " + id, "NOTIFICATION_NOT_FOUND");
    }

    public NotificationNotFoundException(String message) {
        super(message, "NOTIFICATION_NOT_FOUND");
    }
}
