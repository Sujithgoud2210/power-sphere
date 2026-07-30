package com.powersphere.billing.controller;

import com.powersphere.billing.dto.response.ApiResponse;
import com.powersphere.billing.dto.response.TariffPlanResponse;
import com.powersphere.billing.enums.ConsumerType;
import com.powersphere.billing.service.TariffPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Tariff Plans", description = "Tariff rate structure management for billing calculations")
public class TariffPlanController {

    private static final Logger log = LoggerFactory.getLogger(TariffPlanController.class);

    private final TariffPlanService tariffPlanService;

    public TariffPlanController(TariffPlanService tariffPlanService) {
        this.tariffPlanService = tariffPlanService;
    }

    @PostMapping
    @Operation(summary = "Create a new tariff plan",
            description = "Creates a new tariff plan defining rate structures including fixed charges, energy charges per unit, tax percentages, and applicable period.",
            tags = {"Tariff Plans"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Tariff plan created successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"success\":true,\"message\":\"Resource created successfully\",\"data\":{\"id\":1,\"planName\":\"Residential Basic\",\"planCode\":\"RES-BASIC\",\"consumerType\":\"RESIDENTIAL\",\"fixedCharge\":100.00,\"energyChargePerUnit\":5.50,\"taxPercentage\":5.0,\"serviceCharge\":25.00,\"active\":true},\"statusCode\":201,\"timestamp\":\"2024-01-15T10:30:00\"}"))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Duplicate plan code")
    })
    public ResponseEntity<ApiResponse<TariffPlanResponse>> createPlan(
            @Parameter(description = "Display name for the tariff plan", example = "Residential Basic", required = true) @RequestParam String planName,
            @Parameter(description = "Unique code identifier for the plan", example = "RES-BASIC", required = true) @RequestParam String planCode,
            @Parameter(description = "Consumer type this plan applies to (RESIDENTIAL, COMMERCIAL, INDUSTRIAL)", example = "RESIDENTIAL", required = true) @RequestParam ConsumerType consumerType,
            @Parameter(description = "Fixed monthly charge amount", example = "100.00", required = true) @RequestParam BigDecimal fixedCharge,
            @Parameter(description = "Charge per unit of energy consumed (per kWh)", example = "5.50", required = true) @RequestParam BigDecimal energyChargePerUnit,
            @Parameter(description = "Tax percentage applied to the total bill", example = "5.0", required = true) @RequestParam BigDecimal taxPercentage,
            @Parameter(description = "Service charge amount", example = "25.00", required = true) @RequestParam BigDecimal serviceCharge,
            @Parameter(description = "Date from which this plan is effective", example = "2024-01-01", required = true) @RequestParam LocalDate effectiveFrom,
            @Parameter(description = "Date until which this plan is effective (leave empty for ongoing)", example = "2024-12-31") @RequestParam(required = false) LocalDate effectiveTo) {
        log.info("REST request to create tariff plan: code={}, name={}", planCode, planName);
        TariffPlanResponse response = tariffPlanService.createPlan(
                planName, planCode, consumerType, fixedCharge, energyChargePerUnit,
                taxPercentage, serviceCharge, effectiveFrom, effectiveTo);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing tariff plan",
            description = "Updates the rate structure and effective period of an existing tariff plan identified by its ID.",
            tags = {"Tariff Plans"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Tariff plan updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Tariff plan not found")
    })
    public ResponseEntity<ApiResponse<TariffPlanResponse>> updatePlan(
            @Parameter(description = "Tariff plan ID", example = "1", required = true) @PathVariable Long id,
            @Parameter(description = "Display name for the tariff plan", example = "Residential Premium", required = true) @RequestParam String planName,
            @Parameter(description = "Fixed monthly charge amount", example = "150.00", required = true) @RequestParam BigDecimal fixedCharge,
            @Parameter(description = "Charge per unit of energy consumed (per kWh)", example = "6.50", required = true) @RequestParam BigDecimal energyChargePerUnit,
            @Parameter(description = "Tax percentage applied to the total bill", example = "5.0", required = true) @RequestParam BigDecimal taxPercentage,
            @Parameter(description = "Service charge amount", example = "30.00", required = true) @RequestParam BigDecimal serviceCharge,
            @Parameter(description = "Date from which this plan is effective", example = "2024-06-01", required = true) @RequestParam LocalDate effectiveFrom,
            @Parameter(description = "Date until which this plan is effective (leave empty for ongoing)", example = "2024-12-31") @RequestParam(required = false) LocalDate effectiveTo,
            @Parameter(description = "Whether the plan is active", example = "true") @RequestParam(defaultValue = "true") boolean active) {
        log.info("REST request to update tariff plan: id={}", id);
        TariffPlanResponse response = tariffPlanService.updatePlan(
                id, planName, fixedCharge, energyChargePerUnit,
                taxPercentage, serviceCharge, effectiveFrom, effectiveTo, active);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get tariff plan by ID",
            description = "Retrieves detailed information about a tariff plan including rates, charges, and effective period.",
            tags = {"Tariff Plans"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Tariff plan retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Tariff plan not found")
    })
    public ResponseEntity<ApiResponse<TariffPlanResponse>> getPlan(
            @Parameter(description = "Tariff plan ID", example = "1", required = true) @PathVariable Long id) {
        log.debug("REST request to get tariff plan: id={}", id);
        TariffPlanResponse response = tariffPlanService.getPlan(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get tariff plan by code",
            description = "Retrieves tariff plan details using the unique plan code (e.g., RES-BASIC).",
            tags = {"Tariff Plans"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Tariff plan retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Tariff plan not found")
    })
    public ResponseEntity<ApiResponse<TariffPlanResponse>> getPlanByCode(
            @Parameter(description = "Unique plan code", example = "RES-BASIC", required = true) @PathVariable String code) {
        log.debug("REST request to get tariff plan by code: {}", code);
        TariffPlanResponse response = tariffPlanService.getPlanByCode(code);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @Operation(summary = "Get all active tariff plans",
            description = "Retrieves all active tariff plans. Optionally filter by consumer type to get plans applicable to a specific customer category.",
            tags = {"Tariff Plans"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Tariff plans retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<TariffPlanResponse>>> getAllPlans(
            @Parameter(description = "Filter by consumer type (RESIDENTIAL, COMMERCIAL, INDUSTRIAL)", example = "RESIDENTIAL") @RequestParam(required = false) ConsumerType consumerType) {
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
