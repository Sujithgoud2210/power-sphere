package com.powersphere.billing.controller;

import com.powersphere.billing.dto.response.ApiResponse;
import com.powersphere.billing.dto.response.TariffPlanResponse;
import com.powersphere.billing.enums.ConsumerType;
import com.powersphere.billing.service.TariffPlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for managing tariff plans. Tariff plans define the rate
 * structures applied when generating electricity bills.
 */
@RestController
@RequestMapping("/api/v1/tariff-plans")
public class TariffPlanController {

    private static final Logger log = LoggerFactory.getLogger(TariffPlanController.class);

    private final TariffPlanService tariffPlanService;

    public TariffPlanController(TariffPlanService tariffPlanService) {
        this.tariffPlanService = tariffPlanService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TariffPlanResponse>> createPlan(
            @RequestParam String planName,
            @RequestParam String planCode,
            @RequestParam ConsumerType consumerType,
            @RequestParam BigDecimal fixedCharge,
            @RequestParam BigDecimal energyChargePerUnit,
            @RequestParam BigDecimal taxPercentage,
            @RequestParam BigDecimal serviceCharge,
            @RequestParam LocalDate effectiveFrom,
            @RequestParam(required = false) LocalDate effectiveTo) {
        log.info("REST request to create tariff plan: code={}, name={}", planCode, planName);
        TariffPlanResponse response = tariffPlanService.createPlan(
                planName, planCode, consumerType, fixedCharge, energyChargePerUnit,
                taxPercentage, serviceCharge, effectiveFrom, effectiveTo);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TariffPlanResponse>> updatePlan(
            @PathVariable Long id,
            @RequestParam String planName,
            @RequestParam BigDecimal fixedCharge,
            @RequestParam BigDecimal energyChargePerUnit,
            @RequestParam BigDecimal taxPercentage,
            @RequestParam BigDecimal serviceCharge,
            @RequestParam LocalDate effectiveFrom,
            @RequestParam(required = false) LocalDate effectiveTo,
            @RequestParam(defaultValue = "true") boolean active) {
        log.info("REST request to update tariff plan: id={}", id);
        TariffPlanResponse response = tariffPlanService.updatePlan(
                id, planName, fixedCharge, energyChargePerUnit,
                taxPercentage, serviceCharge, effectiveFrom, effectiveTo, active);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TariffPlanResponse>> getPlan(@PathVariable Long id) {
        log.debug("REST request to get tariff plan: id={}", id);
        TariffPlanResponse response = tariffPlanService.getPlan(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<TariffPlanResponse>> getPlanByCode(@PathVariable String code) {
        log.debug("REST request to get tariff plan by code: {}", code);
        TariffPlanResponse response = tariffPlanService.getPlanByCode(code);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TariffPlanResponse>>> getAllPlans(
            @RequestParam(required = false) ConsumerType consumerType) {
        List<TariffPlanResponse> responses;
        if (consumerType != null) {
            log.debug("REST request to get plans by consumer type: {}", consumerType);
            responses = tariffPlanService.getPlansByConsumerType(consumerType);
        } else {
            log.debug("REST request to get all active plans");
            responses = tariffPlanService.getAllActivePlans();
        }
        return ResponseEntity.ok(ApiResponse.success(responses));
    }
}
