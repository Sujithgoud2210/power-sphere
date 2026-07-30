import { apiClient } from '../apiClient';
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
} from '../../types/dashboard';
import type { DateRange } from '../../types/common';

function buildFilters(filters?: DashboardFilters): Record<string, string> {
  const params: Record<string, string> = {};
  if (filters?.period) params.period = filters.period;
  if (filters?.organizationId) params.organizationId = filters.organizationId;
  if (filters?.startDate) params.startDate = filters.startDate;
  if (filters?.endDate) params.endDate = filters.endDate;
  return params;
}

function buildDateRange(dateRange?: DateRange): Record<string, string> {
  const params: Record<string, string> = {};
  if (dateRange?.startDate) params.startDate = dateRange.startDate;
  if (dateRange?.endDate) params.endDate = dateRange.endDate;
  return params;
}

export async function fetchDashboardData(
  dashboardType: string,
  filters?: DashboardFilters
): Promise<DashboardData> {
  return apiClient.get<DashboardData>(
    `/dashboard/${dashboardType}`,
    buildFilters(filters)
  );
}

export async function fetchKpiData(filters?: DashboardFilters): Promise<KpiData> {
  return apiClient.get<KpiData>('/dashboard/kpi', buildFilters(filters));
}

export async function fetchEnergyTrend(
  dateRange?: DateRange
): Promise<EnergyTrendPoint[]> {
  return apiClient.get<EnergyTrendPoint[]>(
    '/dashboard/energy-trend',
    buildDateRange(dateRange)
  );
}

export async function fetchRevenueTrend(
  dateRange?: DateRange
): Promise<RevenueTrendPoint[]> {
  return apiClient.get<RevenueTrendPoint[]>(
    '/dashboard/revenue-trend',
    buildDateRange(dateRange)
  );
}

export async function fetchMonthlyUsage(
  year?: number
): Promise<MonthlyUsageData[]> {
  const params: Record<string, string> = {};
  if (year) params.year = year.toString();
  return apiClient.get<MonthlyUsageData[]>('/dashboard/monthly-usage', params);
}

export async function fetchBillStatusDistribution(): Promise<
  BillStatusDistribution[]
> {
  return apiClient.get<BillStatusDistribution[]>('/dashboard/bill-status');
}

export async function fetchMeterStatusDistribution(): Promise<
  MeterStatusDistribution[]
> {
  return apiClient.get<MeterStatusDistribution[]>('/dashboard/meter-status');
}

export async function fetchTopConsumers(
  limit?: number
): Promise<TopConsumer[]> {
  const params: Record<string, string> = {};
  if (limit) params.limit = limit.toString();
  return apiClient.get<TopConsumer[]>('/dashboard/top-consumers', params);
}

export async function fetchTopOrganizations(
  limit?: number
): Promise<TopOrganization[]> {
  const params: Record<string, string> = {};
  if (limit) params.limit = limit.toString();
  return apiClient.get<TopOrganization[]>('/dashboard/top-organizations', params);
}
