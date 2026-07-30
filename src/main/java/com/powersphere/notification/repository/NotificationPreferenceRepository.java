package com.powersphere.notification.repository;

import com.powersphere.notification.entity.NotificationPreference;
import com.powersphere.notification.model.NotificationChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationPreferenceRepository extends JpaRepository<NotificationPreference, Long> {

    List<NotificationPreference> findByUserId(Long userId);

    Optional<NotificationPreference> findByUserIdAndChannel(Long userId, NotificationChannel channel);

    List<NotificationPreference> findByUserIdAndIsEnabledTrue(Long userId);
}
