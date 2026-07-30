package com.powersphere.notification.listener;

import com.powersphere.notification.entity.Notification;
import com.powersphere.notification.enums.NotificationStatus;
import com.powersphere.notification.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Scheduled task that retries failed notifications according to their retry policy.
 * Runs periodically to pick up notifications in RETRYING status and attempt
 * re-delivery. Notifications that have exceeded their max retries are marked as FAILED.
 */
@Component
public class NotificationRetryListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationRetryListener.class);

    private final NotificationRepository notificationRepository;

    public NotificationRetryListener(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /**
     * Scheduled task that runs every 5 minutes to retry failed notifications.
     * Processes notifications in RETRYING status that still have retry attempts remaining.
     */
    @Scheduled(fixedRateString = "${notification.retry.interval:300000}")
    @Transactional
    public void retryFailedNotifications() {
        List<Notification> notificationsToRetry = notificationRepository.findNotificationsToRetry();

        if (notificationsToRetry.isEmpty()) {
            return;
        }

        log.info("Retrying {} failed notifications", notificationsToRetry.size());

        for (Notification notification : notificationsToRetry) {
            try {
                log.debug("Retrying notification id={}, attempt {}/{}",
                        notification.getId(),
                        notification.getRetryCount() + 1,
                        notification.getMaxRetries());

                notification.setStatus(NotificationStatus.PROCESSING);
                notification.setErrorMessage(null);
                notificationRepository.save(notification);

                // In a real implementation, this would dispatch to the channel sender
                // For now, simulate a retry attempt
                simulateRetry(notification);

            } catch (Exception e) {
                handleRetryFailure(notification, e);
            }
        }
    }

    /**
     * Scheduled task that runs every minute to process scheduled notifications
     * whose scheduled time has arrived.
     */
    @Scheduled(fixedRateString = "${notification.scheduler.interval:60000}")
    @Transactional
    public void processScheduledNotifications() {
        List<Notification> dueNotifications = notificationRepository.findScheduledNotificationsDue(LocalDateTime.now());

        if (dueNotifications.isEmpty()) {
            return;
        }

        log.info("Processing {} scheduled notifications", dueNotifications.size());

        for (Notification notification : dueNotifications) {
            try {
                notification.setScheduledFor(null);
                notification.setStatus(NotificationStatus.PROCESSING);
                notificationRepository.save(notification);

                log.info("Processing scheduled notification id={}, type={}, channel={}",
                        notification.getId(), notification.getType(), notification.getChannel());

                notification.markSent();
                notificationRepository.save(notification);

            } catch (Exception e) {
                log.error("Error processing scheduled notification id={}: {}",
                        notification.getId(), e.getMessage());
                handleRetryFailure(notification, e);
            }
        }
    }

    /**
     * Handles a retry failure by incrementing the retry count and updating the status.
     */
    private void handleRetryFailure(Notification notification, Exception e) {
        log.warn("Notification id={} retry {}/{} failed: {}",
                notification.getId(),
                notification.getRetryCount() + 1,
                notification.getMaxRetries(),
                e.getMessage());

        boolean hasRetriesLeft = notification.incrementRetry();
        notification.setErrorMessage("Retry " + notification.getRetryCount() + " failed: " + e.getMessage());

        if (!hasRetriesLeft) {
            log.warn("Notification id={} has exhausted all {} retries, marking as FAILED",
                    notification.getId(), notification.getMaxRetries());
        }

        notificationRepository.save(notification);
    }

    /**
     * Simulates a retry attempt. In production, this would delegate to channel-specific senders.
     */
    private void simulateRetry(Notification notification) {
        // Placeholder for actual channel dispatch logic
        notification.markSent();
        notificationRepository.save(notification);
        log.info("Notification id={} successfully resent via {}", notification.getId(), notification.getChannel());
    }
}
