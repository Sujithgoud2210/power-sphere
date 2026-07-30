export interface KpiData {
  totalOrganizations: number;
  totalUsers: number;
  activeUsers: number;
  totalSmartMeters: number;
  activeSmartMeters: number;
  inactiveSmartMeters: number;
  todayEnergyConsumption: number;
  monthlyConsumption: number;
  yearlyConsumption: number;
  todayRevenue: number;
  monthlyRevenue: number;
  pendingBills: number;
  paidBills: number;
  overdueBills: number;
  unreadNotifications: number;
}

export interface EnergyTrendPoint {
  date: string;
  consumption: number;
  predicted?: number;
}

export interface RevenueTrendPoint {
  date: string;
  revenue: number;
  expenses?: number;
  profit?: number;
}

export interface MonthlyUsageData {
  month: string;
  consumption: number;
  cost: number;
}

export interface BillStatusDistribution {
  status: string;
  count: number;
  amount: number;
  percentage: number;
}

export interface MeterStatusDistribution {
  status: string;
  count: number;
  percentage: number;
}

export interface TopConsumer {
  organizationId: string;
  organizationName: string;
  consumption: number;
  percentage: number;
  trend: 'up' | 'down' | 'stable';
}

export interface TopOrganization {
  organizationId: string;
  organizationName: string;
  revenue: number;
  meters: number;
  consumption: number;
}

export interface DashboardData {
  kpi: KpiData;
  energyTrend: EnergyTrendPoint[];
  revenueTrend: RevenueTrendPoint[];
  monthlyUsage: MonthlyUsageData[];
  billStatusDistribution: BillStatusDistribution[];
  meterStatusDistribution: MeterStatusDistribution[];
  topConsumers: TopConsumer[];
  topOrganizations: TopOrganization[];
}

export interface DashboardFilters {
  period?: 'today' | 'week' | 'month' | 'quarter' | 'year';
  organizationId?: string;
  startDate?: string;
  endDate?: string;
}
