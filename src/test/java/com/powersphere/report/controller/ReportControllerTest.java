package com.powersphere.report.controller;

import com.powersphere.dashboard.dto.response.ApiResponse;
import com.powersphere.report.dto.ReportResponse;
import com.powersphere.report.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportControllerTest {

    @Mock
    private ReportService reportService;

    private ReportController controller;

    @BeforeEach
    void setUp() {
        controller = new ReportController(reportService);
    }

    @Test
    void testGetDailyReport_ReturnsOkWithData() {
        // Arrange
        ReportResponse mockReport = new ReportResponse();
        mockReport.setReportType("DAILY");
        mockReport.setStartDate(LocalDate.now());
        when(reportService.generateDailyReport(any())).thenReturn(mockReport);

        // Act
        ResponseEntity<ApiResponse<ReportResponse>> result = controller.getDailyReport(null);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().isSuccess()).isTrue();
        assertThat(result.getBody().getData().getReportType()).isEqualTo("DAILY");
    }

    @Test
    void testGetWeeklyReport_ReturnsOk() {
        // Arrange
        ReportResponse mockReport = new ReportResponse();
        mockReport.setReportType("WEEKLY");
        when(reportService.generateWeeklyReport(any())).thenReturn(mockReport);

        // Act
        ResponseEntity<ApiResponse<ReportResponse>> result =
                controller.getWeeklyReport(LocalDate.of(2024, 6, 10));

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getData().getReportType()).isEqualTo("WEEKLY");
    }

    @Test
    void testGetMonthlyReport_ReturnsOk() {
        // Arrange
        ReportResponse mockReport = new ReportResponse();
        mockReport.setReportType("MONTHLY");
        when(reportService.generateMonthlyReport(anyInt(), anyInt())).thenReturn(mockReport);

        // Act
        ResponseEntity<ApiResponse<ReportResponse>> result =
                controller.getMonthlyReport(2024, 6);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getData().getReportType()).isEqualTo("MONTHLY");
    }

    @Test
    void testGetYearlyReport_ReturnsOk() {
        // Arrange
        ReportResponse mockReport = new ReportResponse();
        mockReport.setReportType("YEARLY");
        when(reportService.generateYearlyReport(anyInt())).thenReturn(mockReport);

        // Act
        ResponseEntity<ApiResponse<ReportResponse>> result =
                controller.getYearlyReport(2024);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getData().getReportType()).isEqualTo("YEARLY");
    }

    @Test
    void testGetConsumptionReport_ReturnsOk() {
        // Arrange
        ReportResponse mockReport = new ReportResponse();
        mockReport.setReportType("CONSUMPTION");
        when(reportService.generateConsumptionReport(any(), any())).thenReturn(mockReport);

        // Act
        ResponseEntity<ApiResponse<ReportResponse>> result =
                controller.getConsumptionReport(null, null);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getData().getReportType()).isEqualTo("CONSUMPTION");
    }

    @Test
    void testGetBillingReport_ReturnsOk() {
        // Arrange
        ReportResponse mockReport = new ReportResponse();
        mockReport.setReportType("BILLING");
        when(reportService.generateBillingReport(any(), any())).thenReturn(mockReport);

        // Act
        ResponseEntity<ApiResponse<ReportResponse>> result =
                controller.getBillingReport(null, null);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getData().getReportType()).isEqualTo("BILLING");
    }

    @Test
    void testGetOrganizationReport_WithOrgIdReturnsFilteredReport() {
        // Arrange
        ReportResponse mockReport = new ReportResponse();
        mockReport.setReportType("ORGANIZATION");
        when(reportService.generateOrganizationReport(any())).thenReturn(mockReport);

        // Act
        ResponseEntity<ApiResponse<ReportResponse>> result =
                controller.getOrganizationReport(1L);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getData().getReportType()).isEqualTo("ORGANIZATION");
    }

    @Test
    void testGetOrganizationReport_WithoutOrgIdReturnsAllOrganizations() {
        // Arrange
        ReportResponse mockReport = new ReportResponse();
        mockReport.setReportType("ORGANIZATION");
        when(reportService.generateOrganizationReport(isNull())).thenReturn(mockReport);

        // Act
        ResponseEntity<ApiResponse<ReportResponse>> result =
                controller.getOrganizationReport(null);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getData().getReportType()).isEqualTo("ORGANIZATION");
    }
}
