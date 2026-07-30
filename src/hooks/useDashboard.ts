import { useQuery, useQueryClient } from '@tanstack/react-query';
import type {
  DashboardData,
  DashboardFilters,
  KpiData,
  EnergyTrendPoint,
  RevenueTrendPoint,
  MonthlyUsageData,
  BillStatusDistribution,
  MeterStatusDistribution,
  TopConsumer,
  TopOrganization,
} from '../types/dashboard';
import type { DateRange } from '../types/common';
import {
  fetchDashboardData,
  fetchKpiData,
  fetchEnergyTrend,
  fetchRevenueTrend,
  fetchMonthlyUsage,
  fetchBillStatusDistribution,
  fetchMeterStatusDistribution,
  fetchTopConsumers,
  fetchTopOrganizations,
} from '../services/dashboard/dashboardService';

const DASHBOARD_REFETCH_INTERVAL = 30_000; // 30 seconds
const KPI_REFETCH_INTERVAL = 15_000; // 15 seconds

export function useDashboardData(
  dashboardType: string,
  filters?: DashboardFilters
) {
  return useQuery<DashboardData>({
    queryKey: ['dashboard', dashboardType, filters],
    queryFn: () => fetchDashboardData(dashboardType, filters),
    refetchInterval: DASHBOARD_REFETCH_INTERVAL,
    staleTime: 10_000,
    retry: 2,
    refetchOnWindowFocus: true,
  });
}

export function useKpiData(filters?: DashboardFilters) {
  return useQuery<KpiData>({
    queryKey: ['dashboard', 'kpi', filters],
    queryFn: () => fetchKpiData(filters),
    refetchInterval: KPI_REFETCH_INTERVAL,
    staleTime: 5_000,
    retry: 2,
  });
}

export function useEnergyTrend(dateRange?: DateRange) {
  return useQuery<EnergyTrendPoint[]>({
    queryKey: ['dashboard', 'energyTrend', dateRange],
    queryFn: () => fetchEnergyTrend(dateRange),
    refetchInterval: DASHBOARD_REFETCH_INTERVAL,
    staleTime: 10_000,
  });
}

export function useRevenueTrend(dateRange?: DateRange) {
  return useQuery<RevenueTrendPoint[]>({
    queryKey: ['dashboard', 'revenueTrend', dateRange],
    queryFn: () => fetchRevenueTrend(dateRange),
    refetchInterval: DASHBOARD_REFETCH_INTERVAL,
    staleTime: 10_000,
  });
}

export function useMonthlyUsage(year?: number) {
  return useQuery<MonthlyUsageData[]>({
    queryKey: ['dashboard', 'monthlyUsage', year],
    queryFn: () => fetchMonthlyUsage(year),
    refetchInterval: 60_000,
    staleTime: 30_000,
  });
}

export function useBillStatusDistribution() {
  return useQuery<BillStatusDistribution[]>({
    queryKey: ['dashboard', 'billStatus'],
    queryFn: fetchBillStatusDistribution,
    refetchInterval: 60_000,
    staleTime: 30_000,
  });
}

export function useMeterStatusDistribution() {
  return useQuery<MeterStatusDistribution[]>({
    queryKey: ['dashboard', 'meterStatus'],
    queryFn: fetchMeterStatusDistribution,
    refetchInterval: 60_000,
    staleTime: 30_000,
  });
}

export function useTopConsumers(limit?: number) {
  return useQuery<TopConsumer[]>({
    queryKey: ['dashboard', 'topConsumers', limit],
    queryFn: () => fetchTopConsumers(limit),
    refetchInterval: DASHBOARD_REFETCH_INTERVAL,
    staleTime: 10_000,
  });
}

export function useTopOrganizations(limit?: number) {
  return useQuery<TopOrganization[]>({
    queryKey: ['dashboard', 'topOrganizations', limit],
    queryFn: () => fetchTopOrganizations(limit),
    refetchInterval: DASHBOARD_REFETCH_INTERVAL,
    staleTime: 10_000,
  });
}

export function useInvalidateDashboard() {
  const queryClient = useQueryClient();
  return () => {
    queryClient.invalidateQueries({ queryKey: ['dashboard'] });
  };
}
