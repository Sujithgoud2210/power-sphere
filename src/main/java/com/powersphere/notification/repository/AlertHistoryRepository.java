package com.powersphere.notification.repository;

import com.powersphere.notification.entity.AlertHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertHistoryRepository extends JpaRepository<AlertHistory, Long> {

    List<AlertHistory> findByAlertRuleId(Long alertRuleId);

    Page<AlertHistory> findByAlertRuleId(Long alertRuleId, Pageable pageable);

    List<AlertHistory> findByIsResolved(Boolean isResolved);

    long countByAlertRuleIdAndIsResolved(Long alertRuleId, Boolean isResolved);
}
