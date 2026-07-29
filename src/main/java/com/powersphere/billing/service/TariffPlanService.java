package com.powersphere.billing.service;

import com.powersphere.billing.dto.response.TariffPlanResponse;
import com.powersphere.billing.enums.ConsumerType;

import java.util.List;

/**
 * Service interface for managing tariff plans.
 */
public interface TariffPlanService {

    /**
     * Creates a new tariff plan.
     */
    TariffPlanResponse createPlan(String planName, String planCode, ConsumerType consumerType,
                                  java.math.BigDecimal fixedCharge, java.math.BigDecimal energyChargePerUnit,
                                  java.math.BigDecimal taxPercentage, java.math.BigDecimal serviceCharge,
                                  java.time.LocalDate effectiveFrom, java.time.LocalDate effectiveTo);

    /**
     * Updates an existing tariff plan.
     */
    TariffPlanResponse updatePlan(Long id, String planName,
                                  java.math.BigDecimal fixedCharge, java.math.BigDecimal energyChargePerUnit,
                                  java.math.BigDecimal taxPercentage, java.math.BigDecimal serviceCharge,
                                  java.time.LocalDate effectiveFrom, java.time.LocalDate effectiveTo,
                                  boolean active);

    /**
     * Retrieves a tariff plan by ID.
     */
    TariffPlanResponse getPlan(Long id);

    /**
     * Retrieves a tariff plan by code.
     */
    TariffPlanResponse getPlanByCode(String code);

    /**
     * Retrieves all active tariff plans.
     */
    List<TariffPlanResponse> getAllActivePlans();

    /**
     * Retrieves tariff plans by consumer type.
     */
    List<TariffPlanResponse> getPlansByConsumerType(ConsumerType consumerType);
}
