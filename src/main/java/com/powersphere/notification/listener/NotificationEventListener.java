package com.powersphere.notification.listener;

import com.powersphere.notification.entity.Notification;
import com.powersphere.notification.enums.NotificationStatus;
import com.powersphere.notification.event.NotificationBatchEvent;
import com.powersphere.notification.event.NotificationEvent;
import com.powersphere.notification.event.NotificationStatusChangeEvent;
import com.powersphere.notification.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Event listener for processing notification events asynchronously.
 * Handles notification creation, status changes, and batch processing
 * by dispatching to the appropriate delivery channels.
 */
@Component
public class NotificationEventListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationEventListener.class);

    private final NotificationRepository notificationRepository;

    public NotificationEventListener(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /**
     * Handles a single notification event asynchronously. Updates the notification
     * status to PROCESSING and dispatches it for delivery.
     */
    @Async
    @EventListener
    @Transactional
    public void handleNotificationEvent(NotificationEvent event) {
        Notification notification = event.getNotification();
        log.debug("Processing notification event: id={}, type={}, channel={}",
                notification.getId(), notification.getType(), notification.getChannel());

        try {
            // Update status to processing
            notification.setStatus(NotificationStatus.PROCESSING);
            notificationRepository.save(notification);

            // Dispatch notification to appropriate channel
            dispatchNotification(notification);

        } catch (Exception e) {
            log.error("Error processing notification id={}: {}", notification.getId(), e.getMessage(), e);
            notification.setStatus(NotificationStatus.FAILED);
            notification.setErrorMessage("Processing error: " + e.getMessage());
            notificationRepository.save(notification);
        }
    }

    /**
     * Handles batch notification events, processing each notification in the batch.
     */
    @Async
    @EventListener
    @Transactional
    public CompletableFuture<Void> handleBatchEvent(NotificationBatchEvent event) {
        List<Notification> notifications = event.getNotifications();
        log.info("Processing batch of {} notifications", notifications.size());

        for (Notification notification : notifications) {
            try {
                notification.setStatus(NotificationStatus.PROCESSING);
                notificationRepository.save(notification);

                dispatchNotification(notification);

            } catch (Exception e) {
                log.error("Error processing batch notification id={}: {}", notification.getId(), e.getMessage());
                notification.setStatus(NotificationStatus.FAILED);
                notification.setErrorMessage("Batch processing error: " + e.getMessage());
                notificationRepository.save(notification);
            }
        }

        return CompletableFuture.completedFuture(null);
    }

    /**
     * Logs notification status changes for auditing and monitoring.
     */
    @EventListener
    public void handleStatusChangeEvent(NotificationStatusChangeEvent event) {
        log.info("Notification status changed: id={}, from={}, to={}",
                event.getNotification().getId(),
                event.getPreviousStatus(),
                event.getNewStatus());
    }

    /**
     * Dispatches a notification to the appropriate channel for delivery.
     * This is a placeholder implementation that marks the notification as sent.
     * In a production system, this would integrate with email/SMS/push providers.
     */
    private void dispatchNotification(Notification notification) {
        try {
            // Simulate channel delivery logic
            log.info("Dispatching notification id={} via {} to recipientId={}",
                    notification.getId(), notification.getChannel(), notification.getRecipientId());

            // In a real implementation, this would delegate to channel-specific senders:
            // - EmailSender for EMAIL channel
            // - SmsSender for SMS channel
            // - PushNotificationSender for PUSH channel
            // - InAppNotificationService for IN_APP channel
            // - WebhookSender for WEBHOOK channel

            // Mark as sent after successful dispatch
            notification.markSent();
            notificationRepository.save(notification);

            log.info("Notification id={} sent successfully via {}", notification.getId(), notification.getChannel());

        } catch (Exception e) {
            log.error("Failed to dispatch notification id={} via {}: {}",
                    notification.getId(), notification.getChannel(), e.getMessage());
            notification.markFailed("Dispatch failed: " + e.getMessage());
            notificationRepository.save(notification);
            throw e;
        }
    }
}
