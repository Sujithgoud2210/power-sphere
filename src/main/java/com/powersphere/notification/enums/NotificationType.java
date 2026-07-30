package com.powersphere.notification.enums;

/**
 * Defines the supported types of notifications within the PowerSphere system.
 * Each type represents a distinct category of notification that can be processed
 * and delivered through various channels.
 */
public enum NotificationType {

    /** System-generated alerts and warnings */
    SYSTEM_ALERT,

    /** User account-related notifications (registration, password change, etc.) */
    ACCOUNT,

    /** Security-related notifications (login from new device, suspicious activity) */
    SECURITY,

    /** Billing and subscription-related notifications */
    BILLING,

    /** Marketing and promotional communications */
    MARKETING,

    /** Workflow and task assignment notifications */
    WORKFLOW,

    /** Approval request notifications */
    APPROVAL,

    /** Reminder notifications for scheduled events or deadlines */
    REMINDER,

    /** Scheduled report delivery notifications */
    REPORT,

    /** Integration and third-party service notifications */
    INTEGRATION
}
