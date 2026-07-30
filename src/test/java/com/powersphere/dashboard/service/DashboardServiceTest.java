package com.powersphere.dashboard.service;

import com.powersphere.dashboard.dto.response.*;
import com.powersphere.dashboard.entity.BillEntity.BillStatus;
import com.powersphere.dashboard.entity.SmartMeterEntity.MeterStatus;
import com.powersphere.dashboard.mapper.DashboardMapper;
import com.powersphere.dashboard.repository.*;
import com.powersphere.dashboard.service.impl.DashboardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SmartMeterRepository smartMeterRepository;

    @Mock
    private EnergyReadingRepository energyReadingRepository;

    @Mock
    private BillRepository billRepository;

    @Mock
    private NotificationRepository notificationRepository;

    private DashboardService dashboardService;

    @BeforeEach
    void setUp() {
        DashboardMapper dashboardMapper = new DashboardMapper();
        dashboardService = new DashboardServiceImpl(
                organizationRepository, userRepository, smartMeterRepository,
                energyReadingRepository, billRepository, notificationRepository,
                dashboardMapper
        );
    }

    @Test
    void testGetDashboardSummary_ReturnsAllMetrics() {
        // Arrange
        when(organizationRepository.count()).thenReturn(5L);
        when(userRepository.count()).thenReturn(100L);
        when(userRepository.countByActiveTrue()).thenReturn(80L);
        when(smartMeterRepository.count()).thenReturn(50L);
        when(smartMeterRepository.countByMeterStatus(MeterStatus.ACTIVE)).thenReturn(40L);
        when(smartMeterRepository.countByMeterStatus(MeterStatus.INACTIVE)).thenReturn(10L);
        when(energyReadingRepository.sumConsumptionBetween(any(), any())).thenReturn(1500.0);
        when(energyReadingRepository.sumConsumptionSince(any())).thenReturn(45000.0, 150000.0);
        when(billRepository.sumRevenueByDate(any())).thenReturn(new BigDecimal("5000.00"));
        when(billRepository.sumRevenueSince(any())).thenReturn(new BigDecimal("125000.00"));
        when(billRepository.countByStatus(BillStatus.PENDING)).thenReturn(30L);
        when(billRepository.countByStatus(BillStatus.PAID)).thenReturn(200L);
        when(billRepository.countByStatus(BillStatus.OVERDUE)).thenReturn(15L);
        when(notificationRepository.countSentBetween(any(), any())).thenReturn(25L);
        when(notificationRepository.countUnread()).thenReturn(60L);

        // Act
        DashboardResponse response = dashboardService.getDashboardSummary();

        // Assert
        assertThat(response.getTotalOrganizations()).isEqualTo(5);
        assertThat(response.getTotalUsers()).isEqualTo(100);
        assertThat(response.getActiveUsers()).isEqualTo(80);
        assertThat(response.getTotalSmartMeters()).isEqualTo(50);
        assertThat(response.getActiveSmartMeters()).isEqualTo(40);
        assertThat(response.getInactiveSmartMeters()).isEqualTo(10);
        assertThat(response.getTodaysRevenue()).isEqualByComparingTo(new BigDecimal("5000.00"));
        assertThat(response.getMonthlyRevenue()).isEqualByComparingTo(new BigDecimal("125000.00"));
        assertThat(response.getPendingBills()).isEqualTo(30);
        assertThat(response.getPaidBills()).isEqualTo(200);
        assertThat(response.getOverdueBills()).isEqualTo(15);
        assertThat(response.getNotificationsSentToday()).isEqualTo(25);
        assertThat(response.getUnreadNotifications()).isEqualTo(60);

        verify(organizationRepository).count();
        verify(userRepository).count();
        verify(smartMeterRepository).count();
        verify(billRepository, times(3)).countByStatus(any());
    }

    @Test
    void testGetConsumptionTrends_DefaultPeriodReturnsDaily() {
        // Arrange
        List<Object[]> mockResults = new ArrayList<>();
        mockResults.add(new Object[]{LocalDate.now().minusDays(2), 100.0});
        mockResults.add(new Object[]{LocalDate.now().minusDays(1), 150.0});
        when(energyReadingRepository.sumConsumptionByDay(any(), any())).thenReturn(mockResults);

        // Act
        ConsumptionTrendResponse response = dashboardService.getConsumptionTrends("DAILY", null, null);

        // Assert
        assertThat(response.getPeriod()).isEqualTo("DAILY");
        assertThat(response.getLabels()).hasSize(2);
        assertThat(response.getData()).containsExactly(100.0, 150.0);
    }

    @Test
    void testGetConsumptionTrends_MonthlyReturnsMonthlyData() {
        // Arrange
        List<Object[]> mockResults = new ArrayList<>();
        mockResults.add(new Object[]{2024, 1, 5000.0});
        mockResults.add(new Object[]{2024, 2, 6200.0});
        when(energyReadingRepository.sumConsumptionByMonth(any(), any())).thenReturn(mockResults);

        // Act
        ConsumptionTrendResponse response = dashboardService.getConsumptionTrends("MONTHLY", null, null);

        // Assert
        assertThat(response.getPeriod()).isEqualTo("MONTHLY");
        assertThat(response.getLabels()).containsExactly("2024-01", "2024-02");
        assertThat(response.getData()).containsExactly(5000.0, 6200.0);
    }

    @Test
    void testGetRevenueTrends_DefaultPeriodReturnsDaily() {
        // Arrange
        List<Object[]> mockResults = new ArrayList<>();
        mockResults.add(new Object[]{LocalDate.now().minusDays(1), new BigDecimal("1000.00")});
        when(billRepository.sumRevenueByDay(any(), any())).thenReturn(mockResults);

        // Act
        RevenueTrendResponse response = dashboardService.getRevenueTrends("DAILY", null, null);

        // Assert
        assertThat(response.getPeriod()).isEqualTo("DAILY");
        assertThat(response.getLabels()).hasSize(1);
    }

    @Test
    void testGetTopConsumers_WithLimitReturnsLimitedResults() {
        // Arrange
        List<Object[]> mockResults = new ArrayList<>();
        mockResults.add(new Object[]{1L, 10000.0});
        mockResults.add(new Object[]{2L, 8500.0});
        mockResults.add(new Object[]{3L, 7200.0});
        when(energyReadingRepository.topConsumers(any(), any(), any())).thenReturn(mockResults);

        // Act
        List<OrganizationSummaryResponse> consumers = dashboardService.getTopConsumers(3, null, null);

        // Assert
        assertThat(consumers).hasSize(3);
        assertThat(consumers.get(0).getTotalConsumption()).isEqualTo(10000.0);
        assertThat(consumers.get(0).getOrganizationName()).contains("1");
    }

    @Test
    void testGetMeterStatusDistribution_ReturnsStatusCounts() {
        // Arrange
        List<Object[]> mockResults = new ArrayList<>();
        mockResults.add(new Object[]{MeterStatus.ACTIVE, 40L});
        mockResults.add(new Object[]{MeterStatus.INACTIVE, 10L});
        when(smartMeterRepository.countByStatusGrouped()).thenReturn(mockResults);

        // Act
        Map<String, Long> distribution = dashboardService.getMeterStatusDistribution();

        // Assert
        assertThat(distribution).hasSize(2);
        assertThat(distribution.get("ACTIVE")).isEqualTo(40L);
        assertThat(distribution.get("INACTIVE")).isEqualTo(10L);
    }

    @Test
    void testGetBillStatusDistribution_ReturnsStatusCounts() {
        // Arrange
        List<Object[]> mockResults = new ArrayList<>();
        mockResults.add(new Object[]{BillStatus.PENDING, 30L});
        mockResults.add(new Object[]{BillStatus.PAID, 200L});
        mockResults.add(new Object[]{BillStatus.OVERDUE, 15L});
        when(billRepository.countByStatusGrouped()).thenReturn(mockResults);

        // Act
        Map<String, Long> distribution = dashboardService.getBillStatusDistribution();

        // Assert
        assertThat(distribution).hasSize(3);
        assertThat(distribution.get("PAID")).isEqualTo(200L);
    }

    @Test
    void testGetPeakConsumptionHours_ReturnsHoursSortedByConsumption() {
        // Arrange
        List<Object[]> mockResults = new ArrayList<>();
        mockResults.add(new Object[]{18, 45.0});
        mockResults.add(new Object[]{12, 42.0});
        mockResults.add(new Object[]{8, 38.0});
        when(energyReadingRepository.avgConsumptionByHour()).thenReturn(mockResults);

        // Act
        List<Integer> hours = dashboardService.getPeakConsumptionHours();

        // Assert
        assertThat(hours).containsExactly(18, 12, 8);
    }

    @Test
    void testGetOrganizationComparisons_WhenNoOrganizationsReturnsEmpty() {
        // Arrange
        when(organizationRepository.findAllActive()).thenReturn(Collections.emptyList());

        // Act
        List<OrganizationSummaryResponse> comparisons = dashboardService.getOrganizationComparisons();

        // Assert
        assertThat(comparisons).isEmpty();
    }
}
