package com.powersphere.notification.enums;

/**
 * Defines the priority levels for notifications, determining their processing order
 * and delivery urgency within the PowerSphere system. Higher priority notifications
 * are processed and delivered before lower priority ones.
 */
public enum NotificationPriority {

    /** Routine notifications with no time sensitivity (e.g., marketing emails, weekly digests) */
    LOW,

    /** Standard notifications with normal delivery requirements */
    MEDIUM,

    /** Important notifications requiring prompt attention (e.g., account alerts, billing notices) */
    HIGH,

    /** Critical notifications requiring immediate processing and delivery (e.g., security alerts, system outages) */
    URGENT
}
