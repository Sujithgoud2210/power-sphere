package com.powersphere.dashboard.repository;

import com.powersphere.dashboard.entity.BillEntity;
import com.powersphere.dashboard.entity.BillEntity.BillStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<BillEntity, Long> {

    long countByStatus(BillStatus status);

    long countByOrganizationIdAndStatus(Long organizationId, BillStatus status);

    @Query("SELECT COALESCE(SUM(b.amount), 0) FROM BillEntity b WHERE b.paidDate = :date")
    BigDecimal sumRevenueByDate(@Param("date") LocalDate date);

    @Query("SELECT COALESCE(SUM(b.amount), 0) FROM BillEntity b WHERE b.paidDate >= :start AND b.paidDate < :end")
    BigDecimal sumRevenueBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT COALESCE(SUM(b.amount), 0) FROM BillEntity b WHERE b.paidDate >= :start")
    BigDecimal sumRevenueSince(@Param("start") LocalDate start);

    @Query("SELECT b.paidDate, COALESCE(SUM(b.amount), 0) FROM BillEntity b " +
            "WHERE b.status = 'PAID' AND b.paidDate >= :start AND b.paidDate < :end " +
            "GROUP BY b.paidDate ORDER BY b.paidDate ASC")
    List<Object[]> sumRevenueByDay(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query(value = "SELECT FUNCTION('YEAR', b.paidDate) as yr, FUNCTION('MONTH', b.paidDate) as mo, COALESCE(SUM(b.amount), 0) " +
            "FROM BillEntity b WHERE b.status = 'PAID' AND b.paidDate >= :start AND b.paidDate < :end " +
            "GROUP BY FUNCTION('YEAR', b.paidDate), FUNCTION('MONTH', b.paidDate) " +
            "ORDER BY yr ASC, mo ASC")
    List<Object[]> sumRevenueByMonth(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query(value = "SELECT FUNCTION('YEAR', b.paidDate) as yr, COALESCE(SUM(b.amount), 0) " +
            "FROM BillEntity b WHERE b.status = 'PAID' AND b.paidDate >= :start AND b.paidDate < :end " +
            "GROUP BY FUNCTION('YEAR', b.paidDate) ORDER BY yr ASC")
    List<Object[]> sumRevenueByYear(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT b.status, COUNT(b) FROM BillEntity b GROUP BY b.status")
    List<Object[]> countByStatusGrouped();

    @Query("SELECT b.organizationId, COALESCE(SUM(b.amount), 0) FROM BillEntity b " +
            "WHERE b.status = 'PAID' AND b.paidDate >= :start AND b.paidDate < :end " +
            "GROUP BY b.organizationId ORDER BY COALESCE(SUM(b.amount), 0) DESC")
    List<Object[]> topOrganizationsByRevenue(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT b.organizationId, COUNT(b), COALESCE(SUM(b.amount), 0) FROM BillEntity b " +
            "WHERE b.organizationId IN :orgIds GROUP BY b.organizationId")
    List<Object[]> aggregateByOrganizationIds(@Param("orgIds") List<Long> orgIds);

    @Query("SELECT b.organizationId, COUNT(b), COALESCE(SUM(b.amount), 0) FROM BillEntity b " +
            "WHERE b.organizationId IN :orgIds AND b.status = :status GROUP BY b.organizationId")
    List<Object[]> aggregateByOrganizationIdsAndStatus(@Param("orgIds") List<Long> orgIds,
                                                        @Param("status") BillStatus status);
}
