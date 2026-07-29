package com.powersphere.notification.enums;

/**
 * Defines the available delivery channels for sending notifications within the
 * PowerSphere system. Each channel represents a distinct communication medium
 * through which notifications can be delivered to recipients.
 */
public enum NotificationChannel {

    /** Email delivery via SMTP or email service provider */
    EMAIL,

    /** SMS text message delivery via SMS gateway provider */
    SMS,

    /** Push notification delivery to mobile or web applications */
    PUSH,

    /** In-app notification displayed within the application interface */
    IN_APP,

    /** Webhook POST to a configured HTTP endpoint */
    WEBHOOK,

    /** Slack message delivery via Slack API */
    SLACK,

    /** Microsoft Teams message delivery via Teams webhook */
    TEAMS
}
