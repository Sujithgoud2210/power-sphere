package com.powersphere.report.service.impl;

import com.powersphere.dashboard.dto.response.ConsumptionTrendResponse;
import com.powersphere.dashboard.dto.response.DashboardResponse;
import com.powersphere.dashboard.dto.response.OrganizationSummaryResponse;
import com.powersphere.dashboard.dto.response.RevenueTrendResponse;
import com.powersphere.dashboard.service.DashboardService;
import com.powersphere.report.dto.ReportResponse;
import com.powersphere.report.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private static final Logger log = LoggerFactory.getLogger(ReportServiceImpl.class);

    private final DashboardService dashboardService;

    public ReportServiceImpl(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @Override
    public ReportResponse generateDailyReport(LocalDate date) {
        LocalDate reportDate = date != null ? date : LocalDate.now();
        log.info("Generating daily report for date: {}", reportDate);

        ReportResponse response = createBaseReport("DAILY", reportDate, reportDate);

        DashboardResponse summary = dashboardService.getDashboardSummary();
        response.setSummary(summary);
        response.setConsumptionTrends(dashboardService.getConsumptionTrends("DAILY", reportDate, reportDate));
        response.setRevenueTrends(dashboardService.getRevenueTrends("DAILY", reportDate, reportDate));
        response.setMeterStatusDistribution(dashboardService.getMeterStatusDistribution());
        response.setBillStatusDistribution(dashboardService.getBillStatusDistribution());

        return response;
    }

    @Override
    public ReportResponse generateWeeklyReport(LocalDate startDate) {
        LocalDate weekStart = startDate != null
                ? startDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                : LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate weekEnd = weekStart.plusDays(6);

        log.info("Generating weekly report from {} to {}", weekStart, weekEnd);
        return generateReportWithTrends("WEEKLY", weekStart, weekEnd, "DAILY");
    }

    @Override
    public ReportResponse generateMonthlyReport(int year, int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }
        int reportYear = year > 0 ? year : LocalDate.now().getYear();
        LocalDate monthStart = LocalDate.of(reportYear, month, 1);
        LocalDate monthEnd = monthStart.withDayOfMonth(monthStart.lengthOfMonth());

        log.info("Generating monthly report for {}-{}", reportYear, month);
        return generateReportWithTrends("MONTHLY", monthStart, monthEnd, "DAILY");
    }

    @Override
    public ReportResponse generateYearlyReport(int year) {
        int reportYear = year > 0 ? year : LocalDate.now().getYear();
        LocalDate yearStart = LocalDate.of(reportYear, 1, 1);
        LocalDate yearEnd = LocalDate.of(reportYear, 12, 31);

        log.info("Generating yearly report for {}", reportYear);
        return generateReportWithTrends("YEARLY", yearStart, yearEnd, "MONTHLY");
    }

    @Override
    public ReportResponse generateConsumptionReport(LocalDate startDate, LocalDate endDate) {
        LocalDate start = startDate != null ? startDate : LocalDate.now().minusMonths(1);
        LocalDate end = endDate != null ? endDate : LocalDate.now();

        log.info("Generating consumption report from {} to {}", start, end);
        return generateReportWithTrends("CONSUMPTION", start, end, "DAILY");
    }

    @Override
    public ReportResponse generateBillingReport(LocalDate startDate, LocalDate endDate) {
        LocalDate start = startDate != null ? startDate : LocalDate.now().minusMonths(1);
        LocalDate end = endDate != null ? endDate : LocalDate.now();

        log.info("Generating billing report from {} to {}", start, end);
        ReportResponse response = createBaseReport("BILLING", start, end);
        response.setSummary(dashboardService.getDashboardSummary());
        response.setRevenueTrends(dashboardService.getRevenueTrends("DAILY", start, end));
        response.setBillStatusDistribution(dashboardService.getBillStatusDistribution());
        response.setTopOrganizations(dashboardService.getTopOrganizations(10, start, end));

        return response;
    }

    @Override
    public ReportResponse generateOrganizationReport(Long organizationId) {
        log.info("Generating organization report{}",
                organizationId != null ? " for organization: " + organizationId : "");

        LocalDate start = LocalDate.now().minusMonths(1);
        LocalDate end = LocalDate.now();

        ReportResponse response = createBaseReport("ORGANIZATION", start, end);

        List<OrganizationSummaryResponse> comparisons = dashboardService.getOrganizationComparisons();
        if (organizationId != null) {
            comparisons = comparisons.stream()
                    .filter(o -> o.getOrganizationId().equals(organizationId))
                    .toList();
        }
        response.setOrganizationSummaries(comparisons);
        response.setSummary(dashboardService.getDashboardSummary());
        response.setTopOrganizations(dashboardService.getTopOrganizations(10, start, end));

        return response;
    }

    // ---------- Helper methods ----------

    private ReportResponse generateReportWithTrends(String reportType, LocalDate start,
                                                      LocalDate end, String trendPeriod) {
        ReportResponse response = createBaseReport(reportType, start, end);
        response.setSummary(dashboardService.getDashboardSummary());
        response.setConsumptionTrends(dashboardService.getConsumptionTrends(trendPeriod, start, end));
        response.setRevenueTrends(dashboardService.getRevenueTrends(trendPeriod, start, end));
        response.setMeterStatusDistribution(dashboardService.getMeterStatusDistribution());
        response.setBillStatusDistribution(dashboardService.getBillStatusDistribution());
        response.setTopConsumers(dashboardService.getTopConsumers(10, start, end));
        response.setTopOrganizations(dashboardService.getTopOrganizations(10, start, end));
        response.setPeakConsumptionHours(dashboardService.getPeakConsumptionHours());

        return response;
    }

    private ReportResponse createBaseReport(String reportType, LocalDate startDate, LocalDate endDate) {
        ReportResponse response = new ReportResponse();
        response.setReportType(reportType);
        response.setStartDate(startDate);
        response.setEndDate(endDate);
        return response;
    }
}
