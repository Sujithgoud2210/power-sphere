package com.powersphere.notification.repository;

import com.powersphere.notification.entity.NotificationTemplate;
import com.powersphere.notification.enums.NotificationChannel;
import com.powersphere.notification.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for managing {@link NotificationTemplate} entities.
 * Provides methods for template lookup by code, type, and channel.
 */
@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {

    Optional<NotificationTemplate> findByCode(String code);

    boolean existsByCode(String code);

    List<NotificationTemplate> findByTypeAndChannelAndActiveTrue(
            NotificationType type, NotificationChannel channel);

    List<NotificationTemplate> findByTypeAndActiveTrue(NotificationType type);

    List<NotificationTemplate> findByOrganizationId(Long organizationId);

    List<NotificationTemplate> findByActiveTrue();
}
