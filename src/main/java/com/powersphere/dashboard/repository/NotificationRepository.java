package com.powersphere.dashboard.repository;

import com.powersphere.dashboard.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    @Query("SELECT COUNT(n) FROM NotificationEntity n WHERE n.sentDate >= :start AND n.sentDate < :end")
    long countSentBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(n) FROM NotificationEntity n WHERE n.isRead = false")
    long countUnread();
}
