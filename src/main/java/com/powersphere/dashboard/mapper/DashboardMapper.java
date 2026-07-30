package com.powersphere.dashboard.mapper;

import com.powersphere.dashboard.dto.response.ConsumptionTrendResponse;
import com.powersphere.dashboard.dto.response.DashboardResponse;
import com.powersphere.dashboard.dto.response.OrganizationSummaryResponse;
import com.powersphere.dashboard.dto.response.RevenueTrendResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * DashboardMapper provides mapping utilities for assembling dashboard response objects.
 * Uses manual mapping to avoid MapStruct processor dependency at compile time.
 */
@Component
public class DashboardMapper {

    /**
     * Builds a ConsumptionTrendResponse from raw query results.
     *
     * @param rawResults list of Object[] where each array contains [dateLabel, consumptionValue]
     * @param period     the aggregation period (DAILY, MONTHLY, YEARLY)
     * @return populated ConsumptionTrendResponse
     */
    public ConsumptionTrendResponse toConsumptionTrendResponse(List<Object[]> rawResults, String period) {
        ConsumptionTrendResponse response = new ConsumptionTrendResponse();
        response.setPeriod(period);

        List<String> labels = new ArrayList<>();
        List<Double> data = new ArrayList<>();

        for (Object[] row : rawResults) {
            labels.add(String.valueOf(row[0]));
            data.add(row[1] != null ? ((Number) row[1]).doubleValue() : 0.0);
        }

        response.setLabels(labels);
        response.setData(data);
        return response;
    }

    /**
     * Builds a RevenueTrendResponse from raw query results.
     *
     * @param rawResults list of Object[] where each array contains [dateLabel, revenueValue]
     * @param period     the aggregation period (DAILY, MONTHLY, YEARLY)
     * @return populated RevenueTrendResponse
     */
    public RevenueTrendResponse toRevenueTrendResponse(List<Object[]> rawResults, String period) {
        RevenueTrendResponse response = new RevenueTrendResponse();
        response.setPeriod(period);

        List<String> labels = new ArrayList<>();
        List<BigDecimal> data = new ArrayList<>();

        for (Object[] row : rawResults) {
            labels.add(String.valueOf(row[0]));
            data.add(row[1] != null ? new BigDecimal(row[1].toString()) : BigDecimal.ZERO);
        }

        response.setLabels(labels);
        response.setData(data);
        return response;
    }

    /**
     * Builds a RevenueTrendResponse from pre-computed label/data pairs.
     *
     * @param labels list of date/period labels
     * @param data   list of revenue values
     * @param period the aggregation period
     * @return populated RevenueTrendResponse
     */
    public RevenueTrendResponse toRevenueTrendResponse(List<String> labels, List<BigDecimal> data, String period) {
        RevenueTrendResponse response = new RevenueTrendResponse();
        response.setLabels(labels);
        response.setData(data);
        response.setPeriod(period);
        return response;
    }

    /**
     * Builds an OrganizationSummaryResponse for a single organization.
     */
    public OrganizationSummaryResponse toOrganizationSummaryResponse(
            Long organizationId, String organizationName, long totalUsers,
            long activeUsers, long totalMeters, double totalConsumption,
            BigDecimal totalRevenue, long totalBills) {

        OrganizationSummaryResponse response = new OrganizationSummaryResponse();
        response.setOrganizationId(organizationId);
        response.setOrganizationName(organizationName);
        response.setTotalUsers(totalUsers);
        response.setActiveUsers(activeUsers);
        response.setTotalMeters(totalMeters);
        response.setTotalConsumption(totalConsumption);
        response.setTotalRevenue(totalRevenue != null ? totalRevenue : BigDecimal.ZERO);
        response.setTotalBills(totalBills);
        return response;
    }
}
