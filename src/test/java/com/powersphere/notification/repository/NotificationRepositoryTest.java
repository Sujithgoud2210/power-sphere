package com.powersphere.notification.repository;

import com.powersphere.notification.entity.Notification;
import com.powersphere.notification.model.NotificationChannel;
import com.powersphere.notification.model.NotificationPriority;
import com.powersphere.notification.model.NotificationStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    void saveNotification_ShouldPersistAllFields() {
        Notification notification = Notification.builder()
                .title("Test Notification")
                .message("Test message content")
                .recipientUser(1L)
                .recipientEmail("user@example.com")
                .notificationType(NotificationChannel.EMAIL)
                .priority(NotificationPriority.HIGH)
                .status(NotificationStatus.PENDING)
                .channel(NotificationChannel.EMAIL)
                .retryCount(0)
                .billId(100L)
                .energyAlertId(200L)
                .meterEventId(300L)
                .build();

        Notification saved = notificationRepository.save(notification);

        assertNotNull(saved.getId());
        assertEquals("Test Notification", saved.getTitle());
        assertEquals(NotificationStatus.PENDING, saved.getStatus());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
    }

    @Test
    void findByStatus_ShouldReturnFilteredResults() {
        Notification pending = Notification.builder()
                .title("Pending")
                .message("Msg")
                .recipientUser(1L)
                .notificationType(NotificationChannel.EMAIL)
                .priority(NotificationPriority.LOW)
                .status(NotificationStatus.PENDING)
                .channel(NotificationChannel.EMAIL)
                .build();

        Notification sent = Notification.builder()
                .title("Sent")
                .message("Msg")
                .recipientUser(1L)
                .notificationType(NotificationChannel.EMAIL)
                .priority(NotificationPriority.LOW)
                .status(NotificationStatus.SENT)
                .channel(NotificationChannel.EMAIL)
                .build();

        notificationRepository.save(pending);
        notificationRepository.save(sent);

        Page<Notification> pendingPage = notificationRepository
                .findByStatus(NotificationStatus.PENDING, PageRequest.of(0, 10));

        assertEquals(1, pendingPage.getTotalElements());
        assertEquals("Pending", pendingPage.getContent().get(0).getTitle());
    }

    @Test
    void findByRecipientUser_ShouldReturnUserNotifications() {
        Notification notif1 = Notification.builder()
                .title("User1 Notification")
                .message("Msg")
                .recipientUser(1L)
                .notificationType(NotificationChannel.EMAIL)
                .priority(NotificationPriority.LOW)
                .status(NotificationStatus.PENDING)
                .channel(NotificationChannel.EMAIL)
                .build();

        Notification notif2 = Notification.builder()
                .title("User2 Notification")
                .message("Msg")
                .recipientUser(2L)
                .notificationType(NotificationChannel.EMAIL)
                .priority(NotificationPriority.LOW)
                .status(NotificationStatus.PENDING)
                .channel(NotificationChannel.EMAIL)
                .build();

        notificationRepository.save(notif1);
        notificationRepository.save(notif2);

        Page<Notification> userNotifications = notificationRepository
                .findByRecipientUser(1L, PageRequest.of(0, 10));

        assertEquals(1, userNotifications.getTotalElements());
    }

    @Test
    void searchNotifications_ShouldReturnMatchingResults() {
        Notification notification = Notification.builder()
                .title("Energy Alert")
                .message("High consumption detected")
                .recipientUser(1L)
                .notificationType(NotificationChannel.EMAIL)
                .priority(NotificationPriority.HIGH)
                .status(NotificationStatus.PENDING)
                .channel(NotificationChannel.EMAIL)
                .build();

        notificationRepository.save(notification);

        Page<Notification> results = notificationRepository.searchNotifications(
                "Energy",
                null,
                null,
                null,
                null,
                null,
                null,
                PageRequest.of(0, 10));

        assertEquals(1, results.getTotalElements());
        assertEquals("Energy Alert", results.getContent().get(0).getTitle());
    }

    @Test
    void countByStatus_ShouldReturnCorrectCount() {
        Notification pending = Notification.builder()
                .title("P1").message("Msg")
                .recipientUser(1L)
                .notificationType(NotificationChannel.EMAIL)
                .priority(NotificationPriority.LOW)
                .status(NotificationStatus.PENDING)
                .channel(NotificationChannel.EMAIL)
                .build();

        Notification sent = Notification.builder()
                .title("S1").message("Msg")
                .recipientUser(1L)
                .notificationType(NotificationChannel.EMAIL)
                .priority(NotificationPriority.LOW)
                .status(NotificationStatus.SENT)
                .channel(NotificationChannel.EMAIL)
                .build();

        notificationRepository.save(pending);
        notificationRepository.save(sent);

        assertEquals(1, notificationRepository.countByStatus(NotificationStatus.PENDING));
        assertEquals(1, notificationRepository.countByStatus(NotificationStatus.SENT));
    }
}
