package com.powersphere.dashboard.service.impl;

import com.powersphere.dashboard.dto.response.ConsumptionTrendResponse;
import com.powersphere.dashboard.dto.response.DashboardResponse;
import com.powersphere.dashboard.dto.response.OrganizationSummaryResponse;
import com.powersphere.dashboard.dto.response.RevenueTrendResponse;
import com.powersphere.dashboard.entity.BillEntity.BillStatus;
import com.powersphere.dashboard.entity.SmartMeterEntity.MeterStatus;
import com.powersphere.dashboard.mapper.DashboardMapper;
import com.powersphere.dashboard.entity.OrganizationEntity;
import com.powersphere.dashboard.repository.*;
import com.powersphere.dashboard.service.DashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private static final Logger log = LoggerFactory.getLogger(DashboardServiceImpl.class);


    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final SmartMeterRepository smartMeterRepository;
    private final EnergyReadingRepository energyReadingRepository;
    private final BillRepository billRepository;
    private final NotificationRepository notificationRepository;
    private final DashboardMapper dashboardMapper;

    public DashboardServiceImpl(OrganizationRepository organizationRepository,
                                UserRepository userRepository,
                                SmartMeterRepository smartMeterRepository,
                                EnergyReadingRepository energyReadingRepository,
                                BillRepository billRepository,
                                NotificationRepository notificationRepository,
                                DashboardMapper dashboardMapper) {
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
        this.smartMeterRepository = smartMeterRepository;
        this.energyReadingRepository = energyReadingRepository;
        this.billRepository = billRepository;
        this.notificationRepository = notificationRepository;
        this.dashboardMapper = dashboardMapper;
    }

    @Override
    public DashboardResponse getDashboardSummary() {
        log.debug("Aggregating dashboard summary data");
        DashboardResponse response = new DashboardResponse();

        LocalDate today = LocalDate.now();
        LocalDateTime dayStart = today.atStartOfDay();
        LocalDateTime dayEnd = today.atTime(LocalTime.MAX);
        LocalDateTime monthStart = today.withDayOfMonth(1).atStartOfDay();
        LocalDateTime yearStart = today.withDayOfYear(1).atStartOfDay();

        // Organization counts
        response.setTotalOrganizations(organizationRepository.count());

        // User counts
        response.setTotalUsers(userRepository.count());
        response.setActiveUsers(userRepository.countByActiveTrue());

        // Smart meter counts
        response.setTotalSmartMeters(smartMeterRepository.count());
        response.setActiveSmartMeters(smartMeterRepository.countByMeterStatus(MeterStatus.ACTIVE));
        response.setInactiveSmartMeters(smartMeterRepository.countByMeterStatus(MeterStatus.INACTIVE));

        // Energy consumption
        response.setTodaysEnergyConsumption(energyReadingRepository.sumConsumptionBetween(dayStart, dayEnd));
        response.setMonthlyEnergyConsumption(energyReadingRepository.sumConsumptionSince(monthStart));
        response.setYearlyEnergyConsumption(energyReadingRepository.sumConsumptionSince(yearStart));

        // Revenue
        response.setTodaysRevenue(billRepository.sumRevenueByDate(today));
        response.setMonthlyRevenue(billRepository.sumRevenueSince(today.withDayOfMonth(1)));

        // Bill counts by status
        response.setPendingBills(billRepository.countByStatus(BillStatus.PENDING));
        response.setPaidBills(billRepository.countByStatus(BillStatus.PAID));
        response.setOverdueBills(billRepository.countByStatus(BillStatus.OVERDUE));

        // Notification counts
        response.setNotificationsSentToday(notificationRepository.countSentBetween(dayStart, dayEnd));
        response.setUnreadNotifications(notificationRepository.countUnread());

        log.info("Dashboard summary aggregated successfully");
        return response;
    }

    @Override
    public ConsumptionTrendResponse getConsumptionTrends(String period, LocalDate startDate, LocalDate endDate) {
        log.debug("Fetching consumption trends for period: {}", period);

        LocalDateTime start = resolveStartDateTime(period, startDate);
        LocalDateTime end = resolveEndDateTime(period, endDate);

        List<Object[]> rawResults;
        String resolvedPeriod = period != null ? period.toUpperCase() : "DAILY";

        switch (resolvedPeriod) {
            case "MONTHLY":
                rawResults = energyReadingRepository.sumConsumptionByMonth(start, end);
                return buildConsumptionTrendFromMonthResults(rawResults);
            case "YEARLY":
                rawResults = energyReadingRepository.sumConsumptionByYear(start, end);
                return buildConsumptionTrendFromYearResults(rawResults);
            case "DAILY":
            default:
                rawResults = energyReadingRepository.sumConsumptionByDay(start, end);
                return dashboardMapper.toConsumptionTrendResponse(rawResults, "DAILY");
        }
    }

    @Override
    public RevenueTrendResponse getRevenueTrends(String period, LocalDate startDate, LocalDate endDate) {
        log.debug("Fetching revenue trends for period: {}", period);

        LocalDate start = resolveStartDate(period, startDate);
        LocalDate end = resolveEndDate(period, endDate);

        List<Object[]> rawResults;
        String resolvedPeriod = period != null ? period.toUpperCase() : "DAILY";

        switch (resolvedPeriod) {
            case "MONTHLY":
                rawResults = billRepository.sumRevenueByMonth(start, end);
                return buildRevenueTrendFromMonthResults(rawResults);
            case "YEARLY":
                rawResults = billRepository.sumRevenueByYear(start, end);
                return buildRevenueTrendFromYearResults(rawResults);
            case "DAILY":
            default:
                rawResults = billRepository.sumRevenueByDay(start, end);
                return dashboardMapper.toRevenueTrendResponse(rawResults, "DAILY");
        }
    }

    @Override
    public List<OrganizationSummaryResponse> getTopConsumers(int limit, LocalDate startDate, LocalDate endDate) {
        log.debug("Fetching top {} consumers", limit);

        LocalDateTime start = startDate != null ? startDate.atStartOfDay() : LocalDate.now().minusMonths(1).atStartOfDay();
        LocalDateTime end = endDate != null ? endDate.atTime(LocalTime.MAX) : LocalDateTime.now();

        List<Object[]> consumerData;
        if (limit > 0) {
            consumerData = energyReadingRepository.topConsumers(start, end,
                    org.springframework.data.domain.PageRequest.of(0, limit));
        } else {
            consumerData = energyReadingRepository.topConsumersUnbounded(start, end);
        }

        List<OrganizationSummaryResponse> results = new ArrayList<>();
        for (Object[] row : consumerData) {
            Long meterId = (Long) row[0];
            Double totalConsumption = ((Number) row[1]).doubleValue();

            OrganizationSummaryResponse summary = new OrganizationSummaryResponse();
            summary.setOrganizationId(meterId);
            summary.setOrganizationName("Meter #" + meterId);
            summary.setTotalConsumption(totalConsumption);
            results.add(summary);
        }

        return results;
    }

    @Override
    public Map<String, Long> getMeterStatusDistribution() {
        log.debug("Fetching meter status distribution");
        List<Object[]> results = smartMeterRepository.countByStatusGrouped();
        Map<String, Long> distribution = new LinkedHashMap<>();
        for (Object[] row : results) {
            MeterStatus status = (MeterStatus) row[0];
            Long count = (Long) row[1];
            distribution.put(status.name(), count);
        }
        return distribution;
    }

    @Override
    public Map<String, Long> getBillStatusDistribution() {
        log.debug("Fetching bill status distribution");
        List<Object[]> results = billRepository.countByStatusGrouped();
        Map<String, Long> distribution = new LinkedHashMap<>();
        for (Object[] row : results) {
            BillStatus status = (BillStatus) row[0];
            Long count = (Long) row[1];
            distribution.put(status.name(), count);
        }
        return distribution;
    }

    @Override
    public List<OrganizationSummaryResponse> getOrganizationComparisons() {
        log.debug("Fetching organization comparison data");
        List<OrganizationEntity> organizations = organizationRepository.findAllActive();
        if (organizations.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> orgIds = organizations.stream()
                .map(OrganizationEntity::getOrganizationId)
                .collect(Collectors.toList());

        Map<Long, String> orgNames = organizations.stream()
                .collect(Collectors.toMap(
                        OrganizationEntity::getOrganizationId,
                        OrganizationEntity::getName
                ));

        Map<Long, Long> userCounts = aggregateToMap(userRepository.countByOrganizationIds(orgIds));
        Map<Long, Long> activeUserCounts = aggregateToMap(userRepository.countActiveByOrganizationIds(orgIds));
        Map<Long, Long> meterCounts = aggregateToMap(smartMeterRepository.countByOrganization());

        LocalDateTime monthStart = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime now = LocalDateTime.now();

        // Get consumption per organization (through meters)
        Map<Long, Double> orgConsumption = new HashMap<>();

        // Get meter-to-organization mapping and consumption per organization
        List<Object[]> meterOrgPairs = smartMeterRepository.findAllMeterIdsWithOrganizationIds();
        Map<Long, Long> meterToOrgMap = new HashMap<>();
        for (Object[] pair : meterOrgPairs) {
            Long meterId = (Long) pair[0];
            Long orgId = (Long) pair[1];
            meterToOrgMap.put(meterId, orgId);
        }

        // Get consumption per meter and aggregate by organization
        if (!meterToOrgMap.isEmpty()) {
            List<Long> allMeterIds = new ArrayList<>(meterToOrgMap.keySet());
            List<Object[]> consumptionByMeter = energyReadingRepository.sumConsumptionByMeterIdsGrouped(allMeterIds, monthStart, now);
            for (Object[] row : consumptionByMeter) {
                Long meterId = (Long) row[0];
                Double consumption = ((Number) row[1]).doubleValue();
                Long orgId = meterToOrgMap.get(meterId);
                if (orgId != null) {
                    orgConsumption.merge(orgId, consumption, Double::sum);
                }
            }
        }

        // Aggregate bill data per organization
        List<Object[]> billAgg = billRepository.aggregateByOrganizationIds(orgIds);
        Map<Long, Long> billCounts = new HashMap<>();
        Map<Long, BigDecimal> billRevenue = new HashMap<>();
        for (Object[] row : billAgg) {
            Long orgId = (Long) row[0];
            Long count = (Long) row[1];
            BigDecimal revenue = (BigDecimal) row[2];
            billCounts.put(orgId, count);
            billRevenue.put(orgId, revenue);
        }

        List<OrganizationSummaryResponse> summaries = new ArrayList<>();
        for (Long orgId : orgIds) {
            long users = userCounts.getOrDefault(orgId, 0L);
            long activeUsers = activeUserCounts.getOrDefault(orgId, 0L);
            long meters = meterCounts.getOrDefault(orgId, 0L);
            long bills = billCounts.getOrDefault(orgId, 0L);
            BigDecimal revenue = billRevenue.getOrDefault(orgId, BigDecimal.ZERO);
            double consumption = orgConsumption.getOrDefault(orgId, 0.0);

            summaries.add(dashboardMapper.toOrganizationSummaryResponse(
                    orgId, orgNames.get(orgId), users, activeUsers,
                    meters, consumption, revenue, bills
            ));
        }

        return summaries;
    }

    @Override
    public List<OrganizationSummaryResponse> getTopOrganizations(int limit, LocalDate startDate, LocalDate endDate) {
        log.debug("Fetching top {} organizations by revenue", limit);

        LocalDate start = startDate != null ? startDate : LocalDate.now().minusMonths(1);
        LocalDate end = endDate != null ? endDate : LocalDate.now();

        List<Object[]> rawResults = billRepository.topOrganizationsByRevenue(start, end);
        List<Object[]> topResults = rawResults.stream()
                .limit(limit > 0 ? limit : rawResults.size())
                .collect(Collectors.toList());

        List<Long> orgIds = topResults.stream()
                .map(row -> (Long) row[0])
                .collect(Collectors.toList());

        Map<Long, String> orgNames = !orgIds.isEmpty()
                ? organizationRepository.findNamesByIds(orgIds).stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> (String) row[1]
                ))
                : Collections.emptyMap();

        List<OrganizationSummaryResponse> results = new ArrayList<>();
        for (Object[] row : topResults) {
            Long orgId = (Long) row[0];
            BigDecimal revenue = (BigDecimal) row[1];

            OrganizationSummaryResponse summary = new OrganizationSummaryResponse();
            summary.setOrganizationId(orgId);
            summary.setOrganizationName(orgNames.getOrDefault(orgId, "Organization #" + orgId));
            summary.setTotalRevenue(revenue);
            results.add(summary);
        }

        return results;
    }

    @Override
    public List<Integer> getPeakConsumptionHours() {
        log.debug("Fetching peak consumption hours");
        List<Object[]> rawResults = energyReadingRepository.avgConsumptionByHour();
        return rawResults.stream()
                .map(row -> ((Number) row[0]).intValue())
                .collect(Collectors.toList());
    }

    // ---------- Helper methods ----------

    private LocalDateTime resolveStartDateTime(String period, LocalDate startDate) {
        if (startDate != null) {
            return startDate.atStartOfDay();
        }
        if (period != null) {
            switch (period.toUpperCase()) {
                case "YEARLY":
                    return LocalDate.now().minusYears(5).atStartOfDay();
                case "MONTHLY":
                    return LocalDate.now().minusMonths(12).atStartOfDay();
                case "DAILY":
                default:
                    return LocalDate.now().minusDays(30).atStartOfDay();
            }
        }
        return LocalDate.now().minusDays(30).atStartOfDay();
    }

    private LocalDateTime resolveEndDateTime(String period, LocalDate endDate) {
        return endDate != null ? endDate.atTime(LocalTime.MAX) : LocalDateTime.now();
    }

    private LocalDate resolveStartDate(String period, LocalDate startDate) {
        if (startDate != null) {
            return startDate;
        }
        if (period != null) {
            switch (period.toUpperCase()) {
                case "YEARLY":
                    return LocalDate.now().minusYears(5);
                case "MONTHLY":
                    return LocalDate.now().minusMonths(12);
                case "DAILY":
                default:
                    return LocalDate.now().minusDays(30);
            }
        }
        return LocalDate.now().minusDays(30);
    }

    private LocalDate resolveEndDate(String period, LocalDate endDate) {
        return endDate != null ? endDate : LocalDate.now();
    }

    private ConsumptionTrendResponse buildConsumptionTrendFromMonthResults(List<Object[]> rawResults) {
        List<String> labels = new ArrayList<>();
        List<Double> data = new ArrayList<>();
        for (Object[] row : rawResults) {
            int year = ((Number) row[0]).intValue();
            int month = ((Number) row[1]).intValue();
            labels.add(year + "-" + String.format("%02d", month));
            data.add(row[2] != null ? ((Number) row[2]).doubleValue() : 0.0);
        }
        return new ConsumptionTrendResponse(labels, data, "MONTHLY");
    }

    private ConsumptionTrendResponse buildConsumptionTrendFromYearResults(List<Object[]> rawResults) {
        List<String> labels = new ArrayList<>();
        List<Double> data = new ArrayList<>();
        for (Object[] row : rawResults) {
            int year = ((Number) row[0]).intValue();
            labels.add(String.valueOf(year));
            data.add(row[1] != null ? ((Number) row[1]).doubleValue() : 0.0);
        }
        return new ConsumptionTrendResponse(labels, data, "YEARLY");
    }

    private RevenueTrendResponse buildRevenueTrendFromMonthResults(List<Object[]> rawResults) {
        List<String> labels = new ArrayList<>();
        List<BigDecimal> data = new ArrayList<>();
        for (Object[] row : rawResults) {
            int year = ((Number) row[0]).intValue();
            int month = ((Number) row[1]).intValue();
            labels.add(year + "-" + String.format("%02d", month));
            data.add(row[2] != null ? new BigDecimal(row[2].toString()) : BigDecimal.ZERO);
        }
        return new RevenueTrendResponse(labels, data, "MONTHLY");
    }

    private RevenueTrendResponse buildRevenueTrendFromYearResults(List<Object[]> rawResults) {
        List<String> labels = new ArrayList<>();
        List<BigDecimal> data = new ArrayList<>();
        for (Object[] row : rawResults) {
            int year = ((Number) row[0]).intValue();
            labels.add(String.valueOf(year));
            data.add(row[1] != null ? new BigDecimal(row[1].toString()) : BigDecimal.ZERO);
        }
        return new RevenueTrendResponse(labels, data, "YEARLY");
    }

    private Map<Long, Long> aggregateToMap(List<Object[]> results) {
        Map<Long, Long> map = new HashMap<>();
        for (Object[] row : results) {
            map.put((Long) row[0], (Long) row[1]);
        }
        return map;
    }


}
