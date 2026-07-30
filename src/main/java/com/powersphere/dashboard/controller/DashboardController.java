package com.powersphere.dashboard.controller;

import com.powersphere.dashboard.dto.response.ApiResponse;
import com.powersphere.dashboard.dto.response.ConsumptionTrendResponse;
import com.powersphere.dashboard.dto.response.DashboardResponse;
import com.powersphere.dashboard.dto.response.OrganizationSummaryResponse;
import com.powersphere.dashboard.dto.response.RevenueTrendResponse;
import com.powersphere.dashboard.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/dashboard")
@Tag(name = "Dashboard", description = "Real-time dashboards with consumption trends, revenue analytics, and organizational comparisons")
public class DashboardController {

    private static final Logger log = LoggerFactory.getLogger(DashboardController.class);

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * GET /api/v1/dashboard/summary
     * Returns aggregated dashboard summary with counts and metrics from all modules.
     */
    @GetMapping("/summary")
    @Operation(summary = "Get dashboard summary",
            description = "Returns an aggregated dashboard summary with key metrics and counts from all modules including total users, meters, organizations, active bills, and energy consumption statistics.",
            tags = {"Dashboard"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Dashboard summary retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Authentication required"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboardSummary() {
        log.info("REST request to get dashboard summary");
        DashboardResponse summary = dashboardService.getDashboardSummary();
        return ResponseEntity.ok(ApiResponse.success(summary));
    }

    /**
     * GET /api/v1/dashboard/consumption
     * Returns consumption trend data for the specified period.
     */
    @GetMapping("/consumption")
    @Operation(summary = "Get consumption trends",
            description = "Returns energy consumption trend data for the specified period (DAILY, MONTHLY, or YEARLY) with optional date range filtering.",
            tags = {"Dashboard"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Consumption trends retrieved successfully")
    })
    public ResponseEntity<ApiResponse<ConsumptionTrendResponse>> getConsumptionTrends(
            @Parameter(description = "Aggregation period (DAILY, MONTHLY, YEARLY)", example = "MONTHLY") @RequestParam(required = false, defaultValue = "DAILY") String period,
            @Parameter(description = "Start date (yyyy-MM-dd) for filtering", example = "2024-01-01") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (yyyy-MM-dd) for filtering", example = "2024-12-31") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("REST request to get consumption trends for period: {}", period);
        ConsumptionTrendResponse trends = dashboardService.getConsumptionTrends(period, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(trends));
    }

    /**
     * GET /api/v1/dashboard/revenue
     * Returns revenue trend data for the specified period.
     */
    @GetMapping("/revenue")
    @Operation(summary = "Get revenue trends",
            description = "Returns revenue trend data for the specified period (DAILY, MONTHLY, or YEARLY) with optional date range filtering.",
            tags = {"Dashboard"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Revenue trends retrieved successfully")
    })
    public ResponseEntity<ApiResponse<RevenueTrendResponse>> getRevenueTrends(
            @Parameter(description = "Aggregation period (DAILY, MONTHLY, YEARLY)", example = "MONTHLY") @RequestParam(required = false, defaultValue = "DAILY") String period,
            @Parameter(description = "Start date (yyyy-MM-dd) for filtering", example = "2024-01-01") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (yyyy-MM-dd) for filtering", example = "2024-12-31") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("REST request to get revenue trends for period: {}", period);
        RevenueTrendResponse trends = dashboardService.getRevenueTrends(period, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(trends));
    }

    /**
     * GET /api/v1/dashboard/top-consumers
     * Returns top energy consumers by meter.
     */
    @GetMapping("/top-consumers")
    @Operation(summary = "Get top energy consumers",
            description = "Returns the top energy consumers ranked by consumption, with optional date range filtering and configurable result limit.",
            tags = {"Dashboard"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Top consumers retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<OrganizationSummaryResponse>>> getTopConsumers(
            @Parameter(description = "Number of top consumers to return", example = "10") @RequestParam(required = false, defaultValue = "10") int limit,
            @Parameter(description = "Start date (yyyy-MM-dd) for filtering", example = "2024-01-01") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (yyyy-MM-dd) for filtering", example = "2024-12-31") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("REST request to get top {} consumers", limit);
        List<OrganizationSummaryResponse> consumers = dashboardService.getTopConsumers(limit, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(consumers));
    }

    /**
     * GET /api/v1/dashboard/meter-status-distribution
     * Returns meter status distribution (counts by ACTIVE/INACTIVE).
     */
    @GetMapping("/meter-status-distribution")
    @Operation(summary = "Get meter status distribution",
            description = "Returns the distribution of smart meters by their status (ACTIVE, INACTIVE, FAULTY, etc.) as key-value pairs.",
            tags = {"Dashboard"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Meter status distribution retrieved successfully")
    })
    public ResponseEntity<ApiResponse<Map<String, Long>>> getMeterStatusDistribution() {
        log.info("REST request to get meter status distribution");
        Map<String, Long> distribution = dashboardService.getMeterStatusDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution));
    }

    /**
     * GET /api/v1/dashboard/bill-status-distribution
     * Returns bill status distribution (counts by PENDING/PAID/OVERDUE).
     */
    @GetMapping("/bill-status-distribution")
    @Operation(summary = "Get bill status distribution",
            description = "Returns the distribution of bills by their status (PENDING, PAID, OVERDUE, CANCELLED, etc.) as key-value pairs.",
            tags = {"Dashboard"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Bill status distribution retrieved successfully")
    })
    public ResponseEntity<ApiResponse<Map<String, Long>>> getBillStatusDistribution() {
        log.info("REST request to get bill status distribution");
        Map<String, Long> distribution = dashboardService.getBillStatusDistribution();
        return ResponseEntity.ok(ApiResponse.success(distribution));
    }

    /**
     * GET /api/v1/dashboard/organizations
     * Returns comparative summary for all organizations.
     */
    @GetMapping("/organizations")
    @Operation(summary = "Get organization comparisons",
            description = "Returns a comparative summary for all organizations including consumption, revenue, and billing metrics.",
            tags = {"Dashboard"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Organization comparisons retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<OrganizationSummaryResponse>>> getOrganizationComparisons() {
        log.info("REST request to get organization comparisons");
        List<OrganizationSummaryResponse> comparisons = dashboardService.getOrganizationComparisons();
        return ResponseEntity.ok(ApiResponse.success(comparisons));
    }

    /**
     * GET /api/v1/dashboard/top-organizations
     * Returns top organizations by revenue.
     */
    @GetMapping("/top-organizations")
    @Operation(summary = "Get top organizations by revenue",
            description = "Returns the top organizations ranked by revenue, with optional date range filtering and configurable result limit.",
            tags = {"Dashboard"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Top organizations retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<OrganizationSummaryResponse>>> getTopOrganizations(
            @Parameter(description = "Number of top organizations to return", example = "10") @RequestParam(required = false, defaultValue = "10") int limit,
            @Parameter(description = "Start date (yyyy-MM-dd) for filtering", example = "2024-01-01") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (yyyy-MM-dd) for filtering", example = "2024-12-31") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("REST request to get top {} organizations by revenue", limit);
        List<OrganizationSummaryResponse> organizations =
                dashboardService.getTopOrganizations(limit, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(organizations));
    }

    /**
     * GET /api/v1/dashboard/peak-hours
     * Returns peak consumption hours sorted by average consumption.
     */
    @GetMapping("/peak-hours")
    @Operation(summary = "Get peak consumption hours",
            description = "Returns the hours of the day with the highest average energy consumption, sorted by consumption level in descending order.",
            tags = {"Dashboard"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Peak consumption hours retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<Integer>>> getPeakConsumptionHours() {
        log.info("REST request to get peak consumption hours");
        List<Integer> hours = dashboardService.getPeakConsumptionHours();
        return ResponseEntity.ok(ApiResponse.success(hours));
    }
}
