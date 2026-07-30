package com.powersphere.billing.repository;

import com.powersphere.billing.entity.BillItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for managing BillItem entities.
 */
@Repository
public interface BillItemRepository extends JpaRepository<BillItem, Long> {

    List<BillItem> findByBillIdOrderBySequenceAsc(Long billId);
}
