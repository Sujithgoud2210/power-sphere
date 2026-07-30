package com.powersphere.notification.repository;

import com.powersphere.notification.entity.AlertRule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRuleRepository extends JpaRepository<AlertRule, Long> {

    List<AlertRule> findByEventType(String eventType);

    List<AlertRule> findByIsActiveTrue();

    Page<AlertRule> findByIsActive(Boolean isActive, Pageable pageable);

    Page<AlertRule> findByEventTypeContainingIgnoreCase(String eventType, Pageable pageable);
}
