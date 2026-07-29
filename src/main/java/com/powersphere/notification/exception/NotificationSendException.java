package com.powersphere.notification.exception;

/**
 * Exception thrown when a notification fails to be sent through the delivery channel.
 */
public class NotificationSendException extends NotificationException {

    private final String channel;
    private final Long notificationId;

    public NotificationSendException(Long notificationId, String channel, String message) {
        super("Failed to send notification " + notificationId + " via " + channel + ": " + message,
              "NOTIFICATION_SEND_FAILED");
        this.notificationId = notificationId;
        this.channel = channel;
    }

    public NotificationSendException(Long notificationId, String channel, String message, Throwable cause) {
        super("Failed to send notification " + notificationId + " via " + channel + ": " + message,
              "NOTIFICATION_SEND_FAILED", cause);
        this.notificationId = notificationId;
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }

    public Long getNotificationId() {
        return notificationId;
    }
}
