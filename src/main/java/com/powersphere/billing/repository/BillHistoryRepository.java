package com.powersphere.billing.repository;

import com.powersphere.billing.entity.BillHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for managing BillHistory audit records.
 */
@Repository
public interface BillHistoryRepository extends JpaRepository<BillHistory, Long> {

    List<BillHistory> findByBillIdOrderByChangedAtDesc(Long billId);
}
