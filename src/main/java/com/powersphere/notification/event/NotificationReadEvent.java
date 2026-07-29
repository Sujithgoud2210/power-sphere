package com.powersphere.notification.event;

import com.powersphere.notification.entity.Notification;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NotificationReadEvent extends ApplicationEvent {

    private final Notification notification;

    public NotificationReadEvent(Object source, Notification notification) {
        super(source);
        this.notification = notification;
    }
}
