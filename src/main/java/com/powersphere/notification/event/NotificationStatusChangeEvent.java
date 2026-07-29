package com.powersphere.notification.event;

import com.powersphere.notification.enums.NotificationStatus;
import com.powersphere.notification.entity.Notification;
import org.springframework.context.ApplicationEvent;

/**
 * Application event published when a notification's status changes.
 * Allows other components to react to status transitions, such as
 * logging, auditing, or triggering follow-up actions.
 */
public class NotificationStatusChangeEvent extends ApplicationEvent {

    private final Notification notification;
    private final NotificationStatus previousStatus;
    private final NotificationStatus newStatus;

    public NotificationStatusChangeEvent(
            Object source,
            Notification notification,
            NotificationStatus previousStatus,
            NotificationStatus newStatus) {
        super(source);
        this.notification = notification;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
    }

    public Notification getNotification() {
        return notification;
    }

    public NotificationStatus getPreviousStatus() {
        return previousStatus;
    }

    public NotificationStatus getNewStatus() {
        return newStatus;
    }
}
