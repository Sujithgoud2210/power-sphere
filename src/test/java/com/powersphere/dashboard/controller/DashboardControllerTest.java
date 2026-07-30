package com.powersphere.dashboard.controller;

import com.powersphere.dashboard.dto.response.*;
import com.powersphere.dashboard.service.DashboardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardControllerTest {

    @Mock
    private DashboardService dashboardService;

    private DashboardController controller;

    @BeforeEach
    void setUp() {
        controller = new DashboardController(dashboardService);
    }

    @Test
    void testGetDashboardSummary_ReturnsOkWithData() {
        // Arrange
        DashboardResponse mockResponse = new DashboardResponse();
        mockResponse.setTotalOrganizations(5);
        mockResponse.setTotalUsers(100);
        mockResponse.setTodaysRevenue(new BigDecimal("5000.00"));
        when(dashboardService.getDashboardSummary()).thenReturn(mockResponse);

        // Act
        ResponseEntity<ApiResponse<DashboardResponse>> result = controller.getDashboardSummary();

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().isSuccess()).isTrue();
        assertThat(result.getBody().getData().getTotalOrganizations()).isEqualTo(5);
        assertThat(result.getBody().getData().getTotalUsers()).isEqualTo(100);
    }

    @Test
    void testGetConsumptionTrends_WithoutParamsReturnsDailyData() {
        // Arrange
        ConsumptionTrendResponse mockResponse = new ConsumptionTrendResponse(
                List.of("2024-01-01"), List.of(100.0), "DAILY");
        when(dashboardService.getConsumptionTrends(anyString(), any(), any()))
                .thenReturn(mockResponse);

        // Act
        ResponseEntity<ApiResponse<ConsumptionTrendResponse>> result =
                controller.getConsumptionTrends("DAILY", null, null);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getData().getPeriod()).isEqualTo("DAILY");
        assertThat(result.getBody().getData().getData()).containsExactly(100.0);
    }

    @Test
    void testGetRevenueTrends_WithDateParamsReturnsFilteredData() {
        // Arrange
        RevenueTrendResponse mockResponse = new RevenueTrendResponse(
                List.of("2024-01-01", "2024-01-02"),
                List.of(new BigDecimal("1000"), new BigDecimal("1500")),
                "DAILY");
        when(dashboardService.getRevenueTrends(eq("DAILY"), any(), any()))
                .thenReturn(mockResponse);

        // Act
        ResponseEntity<ApiResponse<RevenueTrendResponse>> result =
                controller.getRevenueTrends("DAILY",
                        LocalDate.of(2024, 1, 1),
                        LocalDate.of(2024, 1, 31));

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getData().getLabels()).hasSize(2);
    }

    @Test
    void testGetTopConsumers_WithDefaultLimitReturns10() {
        // Arrange
        List<OrganizationSummaryResponse> mockConsumers = new ArrayList<>();
        OrganizationSummaryResponse consumer = new OrganizationSummaryResponse();
        consumer.setOrganizationId(1L);
        consumer.setTotalConsumption(5000.0);
        mockConsumers.add(consumer);
        when(dashboardService.getTopConsumers(eq(10), any(), any()))
                .thenReturn(mockConsumers);

        // Act
        ResponseEntity<ApiResponse<List<OrganizationSummaryResponse>>> result =
                controller.getTopConsumers(10, null, null);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getData()).hasSize(1);
    }

    @Test
    void testGetMeterStatusDistribution_ReturnsStatusMap() {
        // Arrange
        Map<String, Long> mockDistribution = new LinkedHashMap<>();
        mockDistribution.put("ACTIVE", 40L);
        mockDistribution.put("INACTIVE", 10L);
        when(dashboardService.getMeterStatusDistribution()).thenReturn(mockDistribution);

        // Act
        ResponseEntity<ApiResponse<Map<String, Long>>> result =
                controller.getMeterStatusDistribution();

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getData().get("ACTIVE")).isEqualTo(40L);
    }

    @Test
    void testGetBillStatusDistribution_ReturnsStatusMap() {
        // Arrange
        Map<String, Long> mockDistribution = new LinkedHashMap<>();
        mockDistribution.put("PAID", 200L);
        when(dashboardService.getBillStatusDistribution()).thenReturn(mockDistribution);

        // Act
        ResponseEntity<ApiResponse<Map<String, Long>>> result =
                controller.getBillStatusDistribution();

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getData().get("PAID")).isEqualTo(200L);
    }

    @Test
    void testGetPeakConsumptionHours_ReturnsHourList() {
        // Arrange
        when(dashboardService.getPeakConsumptionHours()).thenReturn(List.of(18, 12, 8));

        // Act
        ResponseEntity<ApiResponse<List<Integer>>> result =
                controller.getPeakConsumptionHours();

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getData()).containsExactly(18, 12, 8);
    }

    @Test
    void testGetOrganizationComparisons_ReturnsList() {
        // Arrange
        List<OrganizationSummaryResponse> mockComparisons = new ArrayList<>();
        OrganizationSummaryResponse org = new OrganizationSummaryResponse();
        org.setOrganizationId(1L);
        org.setOrganizationName("TestOrg");
        org.setTotalUsers(25L);
        mockComparisons.add(org);
        when(dashboardService.getOrganizationComparisons()).thenReturn(mockComparisons);

        // Act
        ResponseEntity<ApiResponse<List<OrganizationSummaryResponse>>> result =
                controller.getOrganizationComparisons();

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getData()).hasSize(1);
        assertThat(result.getBody().getData().get(0).getOrganizationName()).isEqualTo("TestOrg");
    }
}
