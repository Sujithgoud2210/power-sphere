package com.powersphere.notification.event;

import com.powersphere.notification.entity.Notification;
import org.springframework.context.ApplicationEvent;

/**
 * Application event published when a notification is created and needs to be
 * processed for delivery. Listeners can pick up this event to dispatch the
 * notification through the appropriate delivery channel asynchronously.
 */
public class NotificationEvent extends ApplicationEvent {

    private final Notification notification;

    public NotificationEvent(Object source, Notification notification) {
        super(source);
        this.notification = notification;
    }

    public Notification getNotification() {
        return notification;
    }
}
