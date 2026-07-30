package com.powersphere.notification.exception;

public class NotificationPreferenceNotFoundException extends RuntimeException {

    public NotificationPreferenceNotFoundException(Long id) {
        super("Notification preference not found with id: " + id);
    }

    public NotificationPreferenceNotFoundException(String message) {
        super(message);
    }
}
