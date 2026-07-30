package com.powersphere.notification.repository;

import com.powersphere.notification.entity.Notification;
import com.powersphere.notification.model.NotificationChannel;
import com.powersphere.notification.model.NotificationPriority;
import com.powersphere.notification.model.NotificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findByRecipientUser(Long recipientUserId, Pageable pageable);

    Page<Notification> findByStatus(NotificationStatus status, Pageable pageable);

    Page<Notification> findByPriority(NotificationPriority priority, Pageable pageable);

    Page<Notification> findByChannel(NotificationChannel channel, Pageable pageable);

    Page<Notification> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query("SELECT n FROM Notification n WHERE " +
           "(:status IS NULL OR n.status = :status) AND " +
           "(:priority IS NULL OR n.priority = :priority) AND " +
           "(:channel IS NULL OR n.channel = :channel) AND " +
           "(:recipientUser IS NULL OR n.recipientUser = :recipientUser) AND " +
           "(:startDate IS NULL OR n.createdAt >= :startDate) AND " +
           "(:endDate IS NULL OR n.createdAt <= :endDate) AND " +
           "(:title IS NULL OR LOWER(n.title) LIKE LOWER(CONCAT('%', :title, '%')))")
    Page<Notification> searchNotifications(@Param("title") String title,
                                           @Param("status") NotificationStatus status,
                                           @Param("priority") NotificationPriority priority,
                                           @Param("channel") NotificationChannel channel,
                                           @Param("recipientUser") Long recipientUser,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate,
                                           Pageable pageable);

    List<Notification> findByScheduledTimeBeforeAndStatus(LocalDateTime dateTime, NotificationStatus status);

    long countByStatus(NotificationStatus status);

    long countByRecipientUserAndStatus(Long recipientUserId, NotificationStatus status);
}
