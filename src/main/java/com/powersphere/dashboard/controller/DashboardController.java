package com.powersphere.dashboard.controller;

import com.powersphere.dashboard.dto.response.ApiResponse;
import com.powersphere.dashboard.dto.response.ConsumptionTrendResponse;
import com.powersphere.dashboard.dto.response.DashboardResponse;
import com.powersphere.dashboard.dto.response.OrganizationSummaryResponse;
import com.powersphere.dashboard.dto.response.RevenueTrendResponse;
import com.powersphere.dashboard.service.DashboardService;
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
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboardSummary() {
        log.info("REST request to get dashboard summary");
        DashboardResponse summary = dashboardService.getDashboardSummary();
        return ResponseEntity.ok(ApiResponse.success(summary));
    }

    /**
     * GET /api/v1/dashboard/consumption
     * Returns consumption trend data for the specified period.
     *
     * @param period    DAILY, MONTHLY, or YEARLY
     * @param startDate optional start date (yyyy-MM-dd)
     * @param endDate   optional end date (yyyy-MM-dd)
     */
    @GetMapping("/consumption")
    public ResponseEntity<ApiResponse<ConsumptionTrendResponse>> getConsumptionTrends(
            @RequestParam(required = false, defaultValue = "DAILY") String period,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("REST request to get consumption trends for period: {}", period);
        ConsumptionTrendResponse trends = dashboardService.getConsumptionTrends(period, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(trends));
    }

    /**
     * GET /api/v1/dashboard/revenue
     * Returns revenue trend data for the specified period.
     *
     * @param period    DAILY, MONTHLY, or YEARLY
     * @param startDate optional start date (yyyy-MM-dd)
     * @param endDate   optional end date (yyyy-MM-dd)
     */
    @GetMapping("/revenue")
    public ResponseEntity<ApiResponse<RevenueTrendResponse>> getRevenueTrends(
            @RequestParam(required = false, defaultValue = "DAILY") String period,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("REST request to get revenue trends for period: {}", period);
        RevenueTrendResponse trends = dashboardService.getRevenueTrends(period, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(trends));
    }

    /**
     * GET /api/v1/dashboard/top-consumers
     * Returns top energy consumers by meter.
     *
     * @param limit     number of top consumers (default 10)
     * @param startDate optional start date filter
     * @param endDate   optional end date filter
     */
    @GetMapping("/top-consumers")
    public ResponseEntity<ApiResponse<List<OrganizationSummaryResponse>>> getTopConsumers(
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("REST request to get top {} consumers", limit);
        List<OrganizationSummaryResponse> consumers = dashboardService.getTopConsumers(limit, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(consumers));
    }

    /**
     * GET /api/v1/dashboard/meter-status-distribution
     * Returns meter status distribution (counts by ACTIVE/INACTIVE).
     */
    @GetMapping("/meter-status-distribution")
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
    public ResponseEntity<ApiResponse<List<OrganizationSummaryResponse>>> getTopOrganizations(
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
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
    public ResponseEntity<ApiResponse<List<Integer>>> getPeakConsumptionHours() {
        log.info("REST request to get peak consumption hours");
        List<Integer> hours = dashboardService.getPeakConsumptionHours();
        return ResponseEntity.ok(ApiResponse.success(hours));
    }
}
