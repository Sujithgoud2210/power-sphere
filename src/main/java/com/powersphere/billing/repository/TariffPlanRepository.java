package com.powersphere.billing.repository;

import com.powersphere.billing.entity.TariffPlan;
import com.powersphere.billing.enums.ConsumerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for managing TariffPlan entities.
 */
@Repository
public interface TariffPlanRepository extends JpaRepository<TariffPlan, Long> {

    Optional<TariffPlan> findByPlanCode(String planCode);

    boolean existsByPlanCode(String planCode);

    List<TariffPlan> findByConsumerType(ConsumerType consumerType);

    List<TariffPlan> findByActiveTrue();

    @Query("SELECT t FROM TariffPlan t WHERE t.active = true " +
           "AND t.consumerType = :consumerType " +
           "AND t.effectiveFrom <= :billingDate " +
           "AND (t.effectiveTo IS NULL OR t.effectiveTo >= :billingDate)")
    Optional<TariffPlan> findActiveTariffForConsumer(
            @Param("consumerType") ConsumerType consumerType,
            @Param("billingDate") LocalDate billingDate);
}
