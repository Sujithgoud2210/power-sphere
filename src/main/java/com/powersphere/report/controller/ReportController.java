package com.powersphere.report.controller;

import com.powersphere.dashboard.dto.response.ApiResponse;
import com.powersphere.report.dto.ReportResponse;
import com.powersphere.report.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private static final Logger log = LoggerFactory.getLogger(ReportController.class);

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * GET /api/v1/reports/daily
     * Generates a daily report.
     *
     * @param date optional date (yyyy-MM-dd), defaults to today
     */
    @GetMapping("/daily")
    public ResponseEntity<ApiResponse<ReportResponse>> getDailyReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("REST request to generate daily report{}",
                date != null ? " for date: " + date : "");
        ReportResponse report = reportService.generateDailyReport(date);
        return ResponseEntity.ok(ApiResponse.success(report));
    }

    /**
     * GET /api/v1/reports/weekly
     * Generates a weekly report.
     *
     * @param startDate optional start date of the week (yyyy-MM-dd)
     */
    @GetMapping("/weekly")
    public ResponseEntity<ApiResponse<ReportResponse>> getWeeklyReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        log.info("REST request to generate weekly report{}",
                startDate != null ? " starting: " + startDate : "");
        ReportResponse report = reportService.generateWeeklyReport(startDate);
        return ResponseEntity.ok(ApiResponse.success(report));
    }

    /**
     * GET /api/v1/reports/monthly
     * Generates a monthly report.
     *
     * @param year  optional year (defaults to current)
     * @param month month (1-12), defaults to current month
     */
    @GetMapping("/monthly")
    public ResponseEntity<ApiResponse<ReportResponse>> getMonthlyReport(
            @RequestParam(required = false, defaultValue = "0") int year,
            @RequestParam(required = false) Integer month) {
        int reportMonth = month != null ? month : LocalDate.now().getMonthValue();
        log.info("REST request to generate monthly report for {}-{}", year, reportMonth);
        ReportResponse report = reportService.generateMonthlyReport(year, reportMonth);
        return ResponseEntity.ok(ApiResponse.success(report));
    }

    /**
     * GET /api/v1/reports/yearly
     * Generates a yearly report.
     *
     * @param year year (defaults to current year if not provided or 0)
     */
    @GetMapping("/yearly")
    public ResponseEntity<ApiResponse<ReportResponse>> getYearlyReport(
            @RequestParam(required = false, defaultValue = "0") int year) {
        log.info("REST request to generate yearly report for year: {}", year);
        ReportResponse report = reportService.generateYearlyReport(year);
        return ResponseEntity.ok(ApiResponse.success(report));
    }

    /**
     * GET /api/v1/reports/consumption
     * Generates a consumption-focused report.
     *
     * @param startDate start date (yyyy-MM-dd)
     * @param endDate   end date (yyyy-MM-dd)
     */
    @GetMapping("/consumption")
    public ResponseEntity<ApiResponse<ReportResponse>> getConsumptionReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("REST request to generate consumption report");
        ReportResponse report = reportService.generateConsumptionReport(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(report));
    }

    /**
     * GET /api/v1/reports/billing
     * Generates a billing-focused report.
     *
     * @param startDate start date (yyyy-MM-dd)
     * @param endDate   end date (yyyy-MM-dd)
     */
    @GetMapping("/billing")
    public ResponseEntity<ApiResponse<ReportResponse>> getBillingReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("REST request to generate billing report");
        ReportResponse report = reportService.generateBillingReport(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(report));
    }

    /**
     * GET /api/v1/reports/organization
     * Generates an organization-focused report.
     *
     * @param organizationId optional organization ID filter
     */
    @GetMapping("/organization")
    public ResponseEntity<ApiResponse<ReportResponse>> getOrganizationReport(
            @RequestParam(required = false) Long organizationId) {
        log.info("REST request to generate organization report{}",
                organizationId != null ? " for ID: " + organizationId : "");
        ReportResponse report = reportService.generateOrganizationReport(organizationId);
        return ResponseEntity.ok(ApiResponse.success(report));
    }
}
