import React from 'react';
import DashboardGrid from '../../components/dashboard/DashboardGrid';
import KpiCard from '../../components/dashboard/KpiCard';
import ChartCard from '../../components/dashboard/ChartCard';
import EnergyConsumptionChart from '../../components/charts/EnergyConsumptionChart';
import RevenueTrendChart from '../../components/charts/RevenueTrendChart';
import BillStatusChart from '../../components/charts/BillStatusChart';
import TopConsumersChart from '../../components/charts/TopConsumersChart';
import TopOrganizationsChart from '../../components/charts/TopOrganizationsChart';
import {
  useKpiData,
  useEnergyTrend,
  useRevenueTrend,
  useBillStatusDistribution,
  useTopConsumers,
  useTopOrganizations,
} from '../../hooks/useDashboard';

interface ExecutiveDashboardProps {
  period?: 'today' | 'week' | 'month' | 'quarter' | 'year';
}

export const ExecutiveDashboard: React.FC<ExecutiveDashboardProps> = ({
  period = 'month',
}) => {
  const {
    data: kpi,
    isLoading: kpiLoading,
    error: kpiError,
  } = useKpiData({ period });

  const {
    data: energyTrend,
    isLoading: energyLoading,
    error: energyError,
  } = useEnergyTrend();

  const {
    data: revenueTrend,
    isLoading: revenueLoading,
    error: revenueError,
  } = useRevenueTrend();

  const {
    data: billStatus,
    isLoading: billLoading,
    error: billError,
  } = useBillStatusDistribution();

  const {
    data: topConsumers,
    isLoading: topLoading,
    error: topError,
  } = useTopConsumers(5);

  const {
    data: topOrganizations,
    isLoading: orgLoading,
    error: orgError,
  } = useTopOrganizations(5);

  const formatCurrency = (value: number): string =>
    `$${value.toLocaleString('en-US', { minimumFractionDigits: 0, maximumFractionDigits: 0 })}`;

  const formatNumber = (value: number): string =>
    value.toLocaleString();

  return (
    <div className="space-y-6">
      {/* KPI Cards */}
      <DashboardGrid columns={4}>
        <KpiCard
          title="Total Organizations"
          value={kpi?.totalOrganizations ?? 0}
          icon={
            <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
            </svg>
          }
          color="primary"
          isLoading={kpiLoading}
          error={kpiError as Error | null}
        />
        <KpiCard
          title="Total Users"
          value={formatNumber(kpi?.totalUsers ?? 0)}
          subtitle={`${kpi?.activeUsers ?? 0} active`}
          icon={
            <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197m13.5-9a2.5 2.5 0 11-5 0 2.5 2.5 0 015 0z" />
            </svg>
          }
          color="info"
          isLoading={kpiLoading}
          error={kpiError as Error | null}
        />
        <KpiCard
          title="Monthly Revenue"
          value={formatCurrency(kpi?.monthlyRevenue ?? 0)}
          subtitle={`Today: ${formatCurrency(kpi?.todayRevenue ?? 0)}`}
          icon={
            <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          }
          color="success"
          isLoading={kpiLoading}
          error={kpiError as Error | null}
        />
        <KpiCard
          title="Overdue Bills"
          value={formatNumber(kpi?.overdueBills ?? 0)}
          subtitle={`${formatNumber(kpi?.pendingBills ?? 0)} pending`}
          icon={
            <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          }
          color="error"
          trend={
            kpi
              ? {
                  direction: (kpi.overdueBills / (kpi.pendingBills + kpi.paidBills + kpi.overdueBills || 1)) > 0.1 ? 'up' : 'down',
                  percentage: Math.round((kpi.overdueBills / (kpi.pendingBills + kpi.paidBills + kpi.overdueBills || 1)) * 100),
                  label: 'of total bills',
                }
              : undefined
          }
          isLoading={kpiLoading}
          error={kpiError as Error | null}
        />
      </DashboardGrid>

      {/* Charts Row */}
      <div className="grid grid-cols-1 gap-6 lg:grid-cols-2">
        <ChartCard
          title="Energy Consumption Trend"
          subtitle="Last 30 days"
          isLoading={energyLoading}
          error={energyError as Error | null}
          height={320}
        >
          <div style={{ height: 260 }}>
            <EnergyConsumptionChart data={energyTrend ?? []} />
          </div>
        </ChartCard>

        <ChartCard
          title="Revenue Trend"
          subtitle="Revenue vs Expenses"
          isLoading={revenueLoading}
          error={revenueError as Error | null}
          height={320}
        >
          <div style={{ height: 260 }}>
            <RevenueTrendChart data={revenueTrend ?? []} />
          </div>
        </ChartCard>
      </div>

      {/* Charts Row 2 */}
      <div className="grid grid-cols-1 gap-6 lg:grid-cols-2">
        <ChartCard
          title="Bill Status Distribution"
          subtitle="Overview of all bills"
          isLoading={billLoading}
          error={billError as Error | null}
          height={360}
        >
          <div style={{ height: 300 }}>
            <BillStatusChart data={billStatus ?? []} />
          </div>
        </ChartCard>

        <ChartCard
          title="Top Energy Consumers"
          subtitle="Top 5 organizations by consumption"
          isLoading={topLoading}
          error={topError as Error | null}
          height={360}
        >
          <div style={{ height: 300 }}>
            <TopConsumersChart data={topConsumers ?? []} />
          </div>
        </ChartCard>
      </div>

      {/* Charts Row 3 */}
      <div className="grid grid-cols-1">
        <ChartCard
          title="Top Organizations"
          subtitle="Top 5 organizations by consumption and revenue"
          isLoading={orgLoading}
          error={orgError as Error | null}
          height={360}
        >
          <div style={{ height: 300 }}>
            <TopOrganizationsChart data={topOrganizations ?? []} />
          </div>
        </ChartCard>
      </div>
    </div>
  );
};

export default ExecutiveDashboard;
