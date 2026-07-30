package com.powersphere.notification.listener;

import com.powersphere.notification.event.NotificationCreatedEvent;
import com.powersphere.notification.event.NotificationReadEvent;
import com.powersphere.notification.event.NotificationSentEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class NotificationEventListener {

    @TransactionalEventListener
    public void handleNotificationCreated(NotificationCreatedEvent event) {
        log.info("Notification created: id={}, title='{}', channel={}",
                event.getNotification().getId(),
                event.getNotification().getTitle(),
                event.getNotification().getChannel());
        // Future integration: Email, SMS, Push, WhatsApp, Kafka, RabbitMQ, WebSocket
    }

    @EventListener
    public void handleNotificationSent(NotificationSentEvent event) {
        log.info("Notification sent: id={}, title='{}', status={}",
                event.getNotification().getId(),
                event.getNotification().getTitle(),
                event.getNotification().getStatus());
        // Future integration: Delivery receipts, WebSocket push
    }

    @EventListener
    public void handleNotificationRead(NotificationReadEvent event) {
        log.info("Notification read: id={}, title='{}'",
                event.getNotification().getId(),
                event.getNotification().getTitle());
        // Future integration: Analytics, read receipts
    }
}
