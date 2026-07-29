package com.powersphere.notification.event;

import com.powersphere.notification.entity.Notification;
import org.springframework.context.ApplicationEvent;

import java.util.Collections;
import java.util.List;

/**
 * Application event published when multiple notifications are created in a batch.
 * Enables batch processing optimization for bulk notification sending.
 */
public class NotificationBatchEvent extends ApplicationEvent {

    private final List<Notification> notifications;

    public NotificationBatchEvent(Object source, List<Notification> notifications) {
        super(source);
        this.notifications = notifications != null
                ? Collections.unmodifiableList(notifications)
                : Collections.emptyList();
    }

    public List<Notification> getNotifications() {
        return notifications;
    }
}
