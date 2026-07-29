package com.powersphere.dashboard.mapper;

import com.powersphere.dashboard.dto.response.ConsumptionTrendResponse;
import com.powersphere.dashboard.dto.response.OrganizationSummaryResponse;
import com.powersphere.dashboard.dto.response.RevenueTrendResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DashboardMapperTest {

    private DashboardMapper dashboardMapper;

    @BeforeEach
    void setUp() {
        dashboardMapper = new DashboardMapper();
    }

    @Test
    void testToConsumptionTrendResponse_ConvertsRawResults() {
        // Arrange
        List<Object[]> rawResults = new ArrayList<>();
        rawResults.add(new Object[]{LocalDate.of(2024, 1, 1), 100.0});
        rawResults.add(new Object[]{LocalDate.of(2024, 1, 2), 150.0});
        rawResults.add(new Object[]{LocalDate.of(2024, 1, 3), 200.0});

        // Act
        ConsumptionTrendResponse response = dashboardMapper.toConsumptionTrendResponse(rawResults, "DAILY");

        // Assert
        assertThat(response.getPeriod()).isEqualTo("DAILY");
        assertThat(response.getLabels()).containsExactly("2024-01-01", "2024-01-02", "2024-01-03");
        assertThat(response.getData()).containsExactly(100.0, 150.0, 200.0);
    }

    @Test
    void testToConsumptionTrendResponse_WithNullDataReturnsZero() {
        // Arrange
        List<Object[]> rawResults = new ArrayList<>();
        rawResults.add(new Object[]{LocalDate.of(2024, 1, 1), null});

        // Act
        ConsumptionTrendResponse response = dashboardMapper.toConsumptionTrendResponse(rawResults, "MONTHLY");

        // Assert
        assertThat(response.getData()).containsExactly(0.0);
    }

    @Test
    void testToConsumptionTrendResponse_WithEmptyListReturnsEmpty() {
        // Arrange
        List<Object[]> rawResults = new ArrayList<>();

        // Act
        ConsumptionTrendResponse response = dashboardMapper.toConsumptionTrendResponse(rawResults, "YEARLY");

        // Assert
        assertThat(response.getLabels()).isEmpty();
        assertThat(response.getData()).isEmpty();
    }

    @Test
    void testToRevenueTrendResponse_ConvertsRawResults() {
        // Arrange
        List<Object[]> rawResults = new ArrayList<>();
        rawResults.add(new Object[]{LocalDate.of(2024, 1, 1), new BigDecimal("1000.00")});
        rawResults.add(new Object[]{LocalDate.of(2024, 1, 2), new BigDecimal("1500.50")});

        // Act
        RevenueTrendResponse response = dashboardMapper.toRevenueTrendResponse(rawResults, "DAILY");

        // Assert
        assertThat(response.getPeriod()).isEqualTo("DAILY");
        assertThat(response.getLabels()).containsExactly("2024-01-01", "2024-01-02");
        assertThat(response.getData()).containsExactly(
                new BigDecimal("1000.00"), new BigDecimal("1500.50")
        );
    }

    @Test
    void testToRevenueTrendResponse_WithPreComputedData() {
        // Arrange
        List<String> labels = List.of("Jan", "Feb", "Mar");
        List<BigDecimal> data = List.of(
                new BigDecimal("5000"), new BigDecimal("6200"), new BigDecimal("7100")
        );

        // Act
        RevenueTrendResponse response = dashboardMapper.toRevenueTrendResponse(labels, data, "MONTHLY");

        // Assert
        assertThat(response.getPeriod()).isEqualTo("MONTHLY");
        assertThat(response.getLabels()).containsExactly("Jan", "Feb", "Mar");
        assertThat(response.getData()).hasSize(3);
    }

    @Test
    void testToOrganizationSummaryResponse_PopulatesAllFields() {
        // Act
        OrganizationSummaryResponse response = dashboardMapper.toOrganizationSummaryResponse(
                1L, "TestOrg", 25L, 20L, 10L, 5000.0,
                new BigDecimal("100000.00"), 50L
        );

        // Assert
        assertThat(response.getOrganizationId()).isEqualTo(1L);
        assertThat(response.getOrganizationName()).isEqualTo("TestOrg");
        assertThat(response.getTotalUsers()).isEqualTo(25);
        assertThat(response.getActiveUsers()).isEqualTo(20);
        assertThat(response.getTotalMeters()).isEqualTo(10);
        assertThat(response.getTotalConsumption()).isEqualTo(5000.0);
        assertThat(response.getTotalRevenue()).isEqualByComparingTo(new BigDecimal("100000.00"));
        assertThat(response.getTotalBills()).isEqualTo(50);
    }

    @Test
    void testToOrganizationSummaryResponse_WithNullRevenueReturnsZero() {
        // Act
        OrganizationSummaryResponse response = dashboardMapper.toOrganizationSummaryResponse(
                2L, "NullRevenueOrg", 10L, 8L, 5L, 2000.0, null, 20L
        );

        // Assert
        assertThat(response.getTotalRevenue()).isEqualByComparingTo(BigDecimal.ZERO);
    }
}
