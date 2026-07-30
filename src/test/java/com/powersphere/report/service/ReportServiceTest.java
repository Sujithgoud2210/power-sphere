package com.powersphere.report.service;

import com.powersphere.dashboard.dto.response.DashboardResponse;
import com.powersphere.dashboard.dto.response.OrganizationSummaryResponse;
import com.powersphere.dashboard.service.DashboardService;
import com.powersphere.report.dto.ReportResponse;
import com.powersphere.report.service.impl.ReportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private DashboardService dashboardService;

    private ReportService reportService;

    @BeforeEach
    void setUp() {
        reportService = new ReportServiceImpl(dashboardService);
    }

    @Test
    void testGenerateDailyReport_ReturnsReportWithTodayData() {
        // Arrange
        when(dashboardService.getDashboardSummary()).thenReturn(new DashboardResponse());
        when(dashboardService.getConsumptionTrends(anyString(), any(), any()))
                .thenReturn(null);
        when(dashboardService.getRevenueTrends(anyString(), any(), any()))
                .thenReturn(null);
        when(dashboardService.getMeterStatusDistribution()).thenReturn(null);
        when(dashboardService.getBillStatusDistribution()).thenReturn(null);

        // Act
        ReportResponse report = reportService.generateDailyReport(null);

        // Assert
        assertThat(report.getReportType()).isEqualTo("DAILY");
        assertThat(report.getGeneratedDate()).isEqualTo(LocalDate.now());
        assertThat(report.getStartDate()).isEqualTo(LocalDate.now());
        assertThat(report.getEndDate()).isEqualTo(LocalDate.now());
        assertThat(report.getSummary()).isNotNull();
        verify(dashboardService).getDashboardSummary();
    }

    @Test
    void testGenerateDailyReport_WithGivenDate() {
        // Arrange
        LocalDate testDate = LocalDate.of(2024, 6, 15);
        when(dashboardService.getDashboardSummary()).thenReturn(new DashboardResponse());
        when(dashboardService.getConsumptionTrends(anyString(), any(), any())).thenReturn(null);
        when(dashboardService.getRevenueTrends(anyString(), any(), any())).thenReturn(null);
        when(dashboardService.getMeterStatusDistribution()).thenReturn(null);
        when(dashboardService.getBillStatusDistribution()).thenReturn(null);

        // Act
        ReportResponse report = reportService.generateDailyReport(testDate);

        // Assert
        assertThat(report.getStartDate()).isEqualTo(testDate);
        assertThat(report.getEndDate()).isEqualTo(testDate);
    }

    @Test
    void testGenerateWeeklyReport_StartsOnMonday() {
        // Arrange
        LocalDate friday = LocalDate.of(2024, 6, 14); // A Friday
        when(dashboardService.getDashboardSummary()).thenReturn(new DashboardResponse());
        when(dashboardService.getConsumptionTrends(anyString(), any(), any())).thenReturn(null);
        when(dashboardService.getRevenueTrends(anyString(), any(), any())).thenReturn(null);
        when(dashboardService.getMeterStatusDistribution()).thenReturn(null);
        when(dashboardService.getBillStatusDistribution()).thenReturn(null);
        when(dashboardService.getTopConsumers(anyInt(), any(), any())).thenReturn(new ArrayList<>());
        when(dashboardService.getTopOrganizations(anyInt(), any(), any())).thenReturn(new ArrayList<>());
        when(dashboardService.getPeakConsumptionHours()).thenReturn(new ArrayList<>());

        // Act
        ReportResponse report = reportService.generateWeeklyReport(friday);

        // Assert
        assertThat(report.getReportType()).isEqualTo("WEEKLY");
        assertThat(report.getStartDate().getDayOfWeek().name()).isEqualTo("MONDAY");
    }

    @Test
    void testGenerateMonthlyReport_WithInvalidMonthThrowsException() {
        assertThatThrownBy(() -> reportService.generateMonthlyReport(2024, 13))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Month must be between 1 and 12");

        assertThatThrownBy(() -> reportService.generateMonthlyReport(2024, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Month must be between 1 and 12");
    }

    @Test
    void testGenerateMonthlyReport_ReturnsCorrectDateRange() {
        // Arrange
        when(dashboardService.getDashboardSummary()).thenReturn(new DashboardResponse());
        when(dashboardService.getConsumptionTrends(anyString(), any(), any())).thenReturn(null);
        when(dashboardService.getRevenueTrends(anyString(), any(), any())).thenReturn(null);
        when(dashboardService.getMeterStatusDistribution()).thenReturn(null);
        when(dashboardService.getBillStatusDistribution()).thenReturn(null);
        when(dashboardService.getTopConsumers(anyInt(), any(), any())).thenReturn(new ArrayList<>());
        when(dashboardService.getTopOrganizations(anyInt(), any(), any())).thenReturn(new ArrayList<>());
        when(dashboardService.getPeakConsumptionHours()).thenReturn(new ArrayList<>());

        // Act
        ReportResponse report = reportService.generateMonthlyReport(2024, 2);

        // Assert
        assertThat(report.getReportType()).isEqualTo("MONTHLY");
        assertThat(report.getStartDate()).isEqualTo(LocalDate.of(2024, 2, 1));
        assertThat(report.getEndDate()).isEqualTo(LocalDate.of(2024, 2, 29)); // Leap year
    }

    @Test
    void testGenerateYearlyReport_ReturnsCorrectDateRange() {
        // Arrange
        when(dashboardService.getDashboardSummary()).thenReturn(new DashboardResponse());
        when(dashboardService.getConsumptionTrends(anyString(), any(), any())).thenReturn(null);
        when(dashboardService.getRevenueTrends(anyString(), any(), any())).thenReturn(null);
        when(dashboardService.getMeterStatusDistribution()).thenReturn(null);
        when(dashboardService.getBillStatusDistribution()).thenReturn(null);
        when(dashboardService.getTopConsumers(anyInt(), any(), any())).thenReturn(new ArrayList<>());
        when(dashboardService.getTopOrganizations(anyInt(), any(), any())).thenReturn(new ArrayList<>());
        when(dashboardService.getPeakConsumptionHours()).thenReturn(new ArrayList<>());

        // Act
        ReportResponse report = reportService.generateYearlyReport(2024);

        // Assert
        assertThat(report.getReportType()).isEqualTo("YEARLY");
        assertThat(report.getStartDate()).isEqualTo(LocalDate.of(2024, 1, 1));
        assertThat(report.getEndDate()).isEqualTo(LocalDate.of(2024, 12, 31));
    }

    @Test
    void testGenerateConsumptionReport_ReturnsConsumptionType() {
        // Arrange
        when(dashboardService.getDashboardSummary()).thenReturn(new DashboardResponse());
        when(dashboardService.getConsumptionTrends(anyString(), any(), any())).thenReturn(null);
        when(dashboardService.getRevenueTrends(anyString(), any(), any())).thenReturn(null);
        when(dashboardService.getMeterStatusDistribution()).thenReturn(null);
        when(dashboardService.getBillStatusDistribution()).thenReturn(null);
        when(dashboardService.getTopConsumers(anyInt(), any(), any())).thenReturn(new ArrayList<>());
        when(dashboardService.getTopOrganizations(anyInt(), any(), any())).thenReturn(new ArrayList<>());
        when(dashboardService.getPeakConsumptionHours()).thenReturn(new ArrayList<>());

        // Act
        ReportResponse report = reportService.generateConsumptionReport(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 31)
        );

        // Assert
        assertThat(report.getReportType()).isEqualTo("CONSUMPTION");
    }

    @Test
    void testGenerateOrganizationReport_FiltersByOrgId() {
        // Arrange
        List<OrganizationSummaryResponse> allComparisons = new ArrayList<>();
        OrganizationSummaryResponse org1 = new OrganizationSummaryResponse();
        org1.setOrganizationId(1L);
        org1.setOrganizationName("Org A");
        OrganizationSummaryResponse org2 = new OrganizationSummaryResponse();
        org2.setOrganizationId(2L);
        org2.setOrganizationName("Org B");
        allComparisons.add(org1);
        allComparisons.add(org2);
        when(dashboardService.getOrganizationComparisons()).thenReturn(allComparisons);
        when(dashboardService.getDashboardSummary()).thenReturn(new DashboardResponse());
        when(dashboardService.getTopOrganizations(anyInt(), any(), any())).thenReturn(new ArrayList<>());

        // Act
        ReportResponse report = reportService.generateOrganizationReport(1L);

        // Assert
        assertThat(report.getReportType()).isEqualTo("ORGANIZATION");
        assertThat(report.getOrganizationSummaries()).hasSize(1);
        assertThat(report.getOrganizationSummaries().get(0).getOrganizationId()).isEqualTo(1L);
    }

    @Test
    void testGenerateBillingReport_ReturnsBillingType() {
        // Arrange
        when(dashboardService.getDashboardSummary()).thenReturn(new DashboardResponse());
        when(dashboardService.getRevenueTrends(anyString(), any(), any())).thenReturn(null);
        when(dashboardService.getBillStatusDistribution()).thenReturn(null);
        when(dashboardService.getTopOrganizations(anyInt(), any(), any())).thenReturn(new ArrayList<>());

        // Act
        ReportResponse report = reportService.generateBillingReport(null, null);

        // Assert
        assertThat(report.getReportType()).isEqualTo("BILLING");
    }
}
