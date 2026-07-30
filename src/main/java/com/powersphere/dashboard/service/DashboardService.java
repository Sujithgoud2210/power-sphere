package com.powersphere.dashboard.service;

import com.powersphere.dashboard.dto.response.ConsumptionTrendResponse;
import com.powersphere.dashboard.dto.response.DashboardResponse;
import com.powersphere.dashboard.dto.response.OrganizationSummaryResponse;
import com.powersphere.dashboard.dto.response.RevenueTrendResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface DashboardService {

    /**
     * Aggregates the overall dashboard summary with counts and metrics from all modules.
     */
    DashboardResponse getDashboardSummary();

    /**
     * Retrieves consumption trend data for the specified period.
     *
     * @param period the aggregation period: DAILY, MONTHLY, or YEARLY
     * @param startDate optional start date filter
     * @param endDate   optional end date filter
     */
    ConsumptionTrendResponse getConsumptionTrends(String period, LocalDate startDate, LocalDate endDate);

    /**
     * Retrieves revenue trend data for the specified period.
     *
     * @param period the aggregation period: DAILY, MONTHLY, or YEARLY
     * @param startDate optional start date filter
     * @param endDate   optional end date filter
     */
    RevenueTrendResponse getRevenueTrends(String period, LocalDate startDate, LocalDate endDate);

    /**
     * Retrieves the top energy consumers (by meter) for the given period.
     *
     * @param limit number of top consumers to return
     * @param startDate optional start date filter
     * @param endDate   optional end date filter
     */
    List<OrganizationSummaryResponse> getTopConsumers(int limit, LocalDate startDate, LocalDate endDate);

    /**
     * Retrieves meter status distribution (counts by status).
     */
    Map<String, Long> getMeterStatusDistribution();

    /**
     * Retrieves bill status distribution (counts by status).
     */
    Map<String, Long> getBillStatusDistribution();

    /**
     * Retrieves comparative summary for all organizations.
     */
    List<OrganizationSummaryResponse> getOrganizationComparisons();

    /**
     * Retrieves top organizations by revenue.
     *
     * @param limit number of top organizations to return
     */
    List<OrganizationSummaryResponse> getTopOrganizations(int limit, LocalDate startDate, LocalDate endDate);

    /**
     * Retrieves peak consumption hours sorted by average consumption descending.
     */
    List<Integer> getPeakConsumptionHours();
}
