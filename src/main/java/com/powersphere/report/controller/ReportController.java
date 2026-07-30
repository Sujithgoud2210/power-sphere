package com.powersphere.report.controller;

import com.powersphere.dashboard.dto.response.ApiResponse;
import com.powersphere.report.dto.ReportResponse;
import com.powersphere.report.service.ReportService;
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

@RestController
@RequestMapping("/api/v1/reports")
@Tag(name = "Reports", description = "Generate daily, weekly, monthly, yearly, and custom analytical reports")
public class ReportController {

    private static final Logger log = LoggerFactory.getLogger(ReportController.class);

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * GET /api/v1/reports/daily
     * Generates a daily report.
     */
    @GetMapping("/daily")
    @Operation(summary = "Generate daily report",
            description = "Generates a comprehensive daily report summarizing energy consumption, billing activity, and system metrics. Defaults to today's date if not specified.",
            tags = {"Reports"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Daily report generated successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Authentication required"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    public ResponseEntity<ApiResponse<ReportResponse>> getDailyReport(
            @Parameter(description = "Report date (yyyy-MM-dd). Defaults to today if not provided.", example = "2024-01-15")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("REST request to generate daily report{}",
                date != null ? " for date: " + date : "");
        ReportResponse report = reportService.generateDailyReport(date);
        return ResponseEntity.ok(ApiResponse.success(report));
    }

    /**
     * GET /api/v1/reports/weekly
     * Generates a weekly report.
     */
    @GetMapping("/weekly")
    @Operation(summary = "Generate weekly report",
            description = "Generates a weekly report covering the 7-day period starting from the specified date. Defaults to the current week if not specified.",
            tags = {"Reports"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Weekly report generated successfully")
    })
    public ResponseEntity<ApiResponse<ReportResponse>> getWeeklyReport(
            @Parameter(description = "Start date of the week (yyyy-MM-dd). Defaults to current week start if not provided.", example = "2024-01-08")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        log.info("REST request to generate weekly report{}",
                startDate != null ? " starting: " + startDate : "");
        ReportResponse report = reportService.generateWeeklyReport(startDate);
        return ResponseEntity.ok(ApiResponse.success(report));
    }

    /**
     * GET /api/v1/reports/monthly
     * Generates a monthly report.
     */
    @GetMapping("/monthly")
    @Operation(summary = "Generate monthly report",
            description = "Generates a monthly report for the specified year and month. Defaults to the current month if not specified.",
            tags = {"Reports"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Monthly report generated successfully")
    })
    public ResponseEntity<ApiResponse<ReportResponse>> getMonthlyReport(
            @Parameter(description = "Year for the report. Defaults to current year if 0.", example = "2024")
            @RequestParam(required = false, defaultValue = "0") int year,
            @Parameter(description = "Month (1-12). Defaults to current month if not provided.", example = "1")
            @RequestParam(required = false) Integer month) {
        int reportMonth = month != null ? month : LocalDate.now().getMonthValue();
        log.info("REST request to generate monthly report for {}-{}", year, reportMonth);
        ReportResponse report = reportService.generateMonthlyReport(year, reportMonth);
        return ResponseEntity.ok(ApiResponse.success(report));
    }

    /**
     * GET /api/v1/reports/yearly
     * Generates a yearly report.
     */
    @GetMapping("/yearly")
    @Operation(summary = "Generate yearly report",
            description = "Generates an annual report summarizing the full year's energy consumption, billing, and revenue data. Defaults to the current year if not specified.",
            tags = {"Reports"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Yearly report generated successfully")
    })
    public ResponseEntity<ApiResponse<ReportResponse>> getYearlyReport(
            @Parameter(description = "Year for the report. Defaults to current year if 0.", example = "2024")
            @RequestParam(required = false, defaultValue = "0") int year) {
        log.info("REST request to generate yearly report for year: {}", year);
        ReportResponse report = reportService.generateYearlyReport(year);
        return ResponseEntity.ok(ApiResponse.success(report));
    }

    /**
     * GET /api/v1/reports/consumption
     * Generates a consumption-focused report.
     */
    @GetMapping("/consumption")
    @Operation(summary = "Generate consumption report",
            description = "Generates a consumption-focused report for a specified date range, detailing energy usage patterns, peak demand, and consumption metrics.",
            tags = {"Reports"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Consumption report generated successfully")
    })
    public ResponseEntity<ApiResponse<ReportResponse>> getConsumptionReport(
            @Parameter(description = "Start date for the report range (yyyy-MM-dd)", example = "2024-01-01")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date for the report range (yyyy-MM-dd)", example = "2024-12-31")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("REST request to generate consumption report");
        ReportResponse report = reportService.generateConsumptionReport(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(report));
    }

    /**
     * GET /api/v1/reports/billing
     * Generates a billing-focused report.
     */
    @GetMapping("/billing")
    @Operation(summary = "Generate billing report",
            description = "Generates a billing-focused report for a specified date range, detailing revenue, outstanding payments, collection rates, and billing trends.",
            tags = {"Reports"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Billing report generated successfully")
    })
    public ResponseEntity<ApiResponse<ReportResponse>> getBillingReport(
            @Parameter(description = "Start date for the report range (yyyy-MM-dd)", example = "2024-01-01")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date for the report range (yyyy-MM-dd)", example = "2024-12-31")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("REST request to generate billing report");
        ReportResponse report = reportService.generateBillingReport(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(report));
    }

    /**
     * GET /api/v1/reports/organization
     * Generates an organization-focused report.
     */
    @GetMapping("/organization")
    @Operation(summary = "Generate organization report",
            description = "Generates an organization-focused report with optional organization ID filter. Includes organization-level consumption, billing, and compliance metrics.",
            tags = {"Reports"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Organization report generated successfully")
    })
    public ResponseEntity<ApiResponse<ReportResponse>> getOrganizationReport(
            @Parameter(description = "Organization ID to filter the report. If empty, includes all organizations.", example = "1")
            @RequestParam(required = false) Long organizationId) {
        log.info("REST request to generate organization report{}",
                organizationId != null ? " for ID: " + organizationId : "");
        ReportResponse report = reportService.generateOrganizationReport(organizationId);
        return ResponseEntity.ok(ApiResponse.success(report));
    }
}
