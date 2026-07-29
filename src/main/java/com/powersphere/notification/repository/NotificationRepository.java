package com.powersphere.notification.repository;

import com.powersphere.notification.entity.Notification;
import com.powersphere.notification.enums.NotificationChannel;
import com.powersphere.notification.enums.NotificationStatus;
import com.powersphere.notification.enums.NotificationPriority;
import com.powersphere.notification.enums.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for managing {@link Notification} entities.
 * Provides standard CRUD operations along with custom query methods for
 * filtering, searching, and batch operations on notifications.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // --- Basic Queries ---

    Optional<Notification> findByIdAndRecipientId(Long id, Long recipientId);

    List<Notification> findByRecipientIdOrderByCreatedAtDesc(Long recipientId);

    List<Notification> findBySenderIdOrderByCreatedAtDesc(Long senderId);

    Page<Notification> findByRecipientId(Long recipientId, Pageable pageable);

    Page<Notification> findByOrganizationId(Long organizationId, Pageable pageable);

    // --- Status-based Queries ---

    List<Notification> findByStatus(NotificationStatus status);

    Page<Notification> findByStatus(NotificationStatus status, Pageable pageable);

    long countByStatus(NotificationStatus status);

    @Query("SELECT n FROM Notification n WHERE n.status = :status AND n.scheduledFor IS NULL ORDER BY n.priority, n.createdAt")
    List<Notification> findPendingByStatusOrderByPriority(@Param("status") NotificationStatus status);

    // --- Type-based Queries ---

    Page<Notification> findByType(NotificationType type, Pageable pageable);

    // --- Channel-based Queries ---

    Page<Notification> findByChannel(NotificationChannel channel, Pageable pageable);

    // --- Date Range Queries ---

    List<Notification> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);

    @Query("SELECT n FROM Notification n WHERE n.createdAt BETWEEN :from AND :to AND n.status = :status")
    List<Notification> findByDateRangeAndStatus(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("status") NotificationStatus status);

    // --- Scheduled Notifications ---

    @Query("SELECT n FROM Notification n WHERE n.scheduledFor IS NOT NULL AND n.scheduledFor <= :now AND n.status = 'PENDING'")
    List<Notification> findScheduledNotificationsDue(@Param("now") LocalDateTime now);

    // --- Retry Queries ---

    @Query("SELECT n FROM Notification n WHERE n.status = 'RETRYING' AND n.retryCount < n.maxRetries AND n.scheduledFor IS NULL")
    List<Notification> findNotificationsToRetry();

    // --- Search Query ---

    @Query("SELECT n FROM Notification n WHERE " +
           "(:recipientId IS NULL OR n.recipientId = :recipientId) AND " +
           "(:senderId IS NULL OR n.senderId = :senderId) AND " +
           "(:organizationId IS NULL OR n.organizationId = :organizationId) AND " +
           "(:type IS NULL OR n.type = :type) AND " +
           "(:status IS NULL OR n.status = :status) AND " +
           "(:priority IS NULL OR n.priority = :priority) AND " +
           "(:channel IS NULL OR n.channel = :channel) AND " +
           "(:dateFrom IS NULL OR n.createdAt >= :dateFrom) AND " +
           "(:dateTo IS NULL OR n.createdAt <= :dateTo) AND " +
           "(:query IS NULL OR LOWER(n.subject) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(n.content) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Notification> searchNotifications(
            @Param("recipientId") Long recipientId,
            @Param("senderId") Long senderId,
            @Param("organizationId") Long organizationId,
            @Param("type") NotificationType type,
            @Param("status") NotificationStatus status,
            @Param("priority") NotificationPriority priority,
            @Param("channel") NotificationChannel channel,
            @Param("dateFrom") LocalDateTime dateFrom,
            @Param("dateTo") LocalDateTime dateTo,
            @Param("query") String query,
            Pageable pageable);

    // --- Batch Operations ---

    @Modifying
    @Query("UPDATE Notification n SET n.status = 'ARCHIVED' WHERE n.id IN :ids")
    int archiveNotifications(@Param("ids") List<Long> ids);

    @Modifying
    @Query("UPDATE Notification n SET n.status = 'ARCHIVED' WHERE n.recipientId = :recipientId AND n.status = 'READ'")
    int archiveAllReadByRecipient(@Param("recipientId") Long recipientId);

    @Modifying
    @Query("DELETE FROM Notification n WHERE n.createdAt < :cutoff AND n.status IN ('ARCHIVED', 'FAILED', 'DELIVERED', 'READ')")
    int purgeOldNotifications(@Param("cutoff") LocalDateTime cutoff);

    // --- Count Queries ---

    long countByRecipientIdAndStatus(Long recipientId, NotificationStatus status);

    long countByOrganizationIdAndStatus(Long organizationId, NotificationStatus status);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.recipientId = :recipientId AND n.status = 'READ' AND n.readAt IS NULL")
    long countUnreadByRecipient(@Param("recipientId") Long recipientId);
}
