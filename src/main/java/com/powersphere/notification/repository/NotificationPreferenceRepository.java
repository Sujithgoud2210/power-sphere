package com.powersphere.notification.repository;

import com.powersphere.notification.entity.NotificationPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for managing {@link NotificationPreference} entities.
 * Handles user notification preferences and settings.
 */
@Repository
public interface NotificationPreferenceRepository extends JpaRepository<NotificationPreference, Long> {

    Optional<NotificationPreference> findByUserId(Long userId);

    Optional<NotificationPreference> findByUserIdAndOrganizationId(Long userId, Long organizationId);

    List<NotificationPreference> findByOrganizationId(Long organizationId);

    boolean existsByUserId(Long userId);
}
