package com.powersphere.notification.enums;

/**
 * Represents the lifecycle states of a notification within the PowerSphere system.
 * Each notification transitions through these states as it is processed, delivered,
 * and acknowledged by the recipient.
 */
public enum NotificationStatus {

    /** Notification has been created and queued for processing */
    PENDING,

    /** Notification is currently being processed and dispatched */
    PROCESSING,

    /** Notification has been successfully sent to the delivery channel */
    SENT,

    /** Notification has been confirmed as delivered to the recipient */
    DELIVERED,

    /** Notification delivery failed and will be retried based on retry policy */
    RETRYING,

    /** Notification delivery has permanently failed after exhausting retries */
    FAILED,

    /** Notification has been read/acknowledged by the recipient (for in-app notifications) */
    READ,

    /** Notification has been archived and is no longer active */
    ARCHIVED,

    /** Notification has been cancelled before processing */
    CANCELLED
}
