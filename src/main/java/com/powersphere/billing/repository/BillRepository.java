package com.powersphere.billing.repository;

import com.powersphere.billing.entity.Bill;
import com.powersphere.billing.enums.BillStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Spring Data JPA repository for managing Bill entities with custom search
 * and filtering capabilities.
 */
@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    Optional<Bill> findByBillNumber(String billNumber);

    boolean existsByBillNumber(String billNumber);

    Page<Bill> findByMeterId(Long meterId, Pageable pageable);

    Page<Bill> findByOrganizationId(Long organizationId, Pageable pageable);

    Page<Bill> findByStatus(BillStatus status, Pageable pageable);

    Page<Bill> findByBillingMonthAndBillingYear(int billingMonth, int billingYear, Pageable pageable);

    long countByMeterIdAndBillingMonthAndBillingYear(Long meterId, int billingMonth, int billingYear);

    @Query("SELECT b FROM Bill b WHERE " +
           "(:meterId IS NULL OR b.meterId = :meterId) AND " +
           "(:organizationId IS NULL OR b.organizationId = :organizationId) AND " +
           "(:status IS NULL OR b.status = :status) AND " +
           "(:billingMonth IS NULL OR b.billingMonth = :billingMonth) AND " +
           "(:billingYear IS NULL OR b.billingYear = :billingYear) AND " +
           "(:query IS NULL OR LOWER(b.billNumber) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(b.consumerName) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(b.meterNumber) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Bill> searchBills(
            @Param("meterId") Long meterId,
            @Param("organizationId") Long organizationId,
            @Param("status") BillStatus status,
            @Param("billingMonth") Integer billingMonth,
            @Param("billingYear") Integer billingYear,
            @Param("query") String query,
            Pageable pageable);

    @Query("SELECT COUNT(b) FROM Bill b WHERE b.status = 'OVERDUE' AND b.dueDate < :currentDate")
    long countOverdueBills(@Param("currentDate") LocalDate currentDate);
}
