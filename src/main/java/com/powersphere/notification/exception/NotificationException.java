package com.powersphere.notification.exception;

/**
 * Base exception class for all notification-related errors in the PowerSphere system.
 * Provides a standardized exception hierarchy for the notification module.
 */
public class NotificationException extends RuntimeException {

    private final String errorCode;

    public NotificationException(String message) {
        super(message);
        this.errorCode = "NOTIFICATION_ERROR";
    }

    public NotificationException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public NotificationException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "NOTIFICATION_ERROR";
    }

    public NotificationException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
