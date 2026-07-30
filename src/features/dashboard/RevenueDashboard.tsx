import React from 'react';
import DashboardGrid from '../../components/dashboard/DashboardGrid';
import KpiCard from '../../components/dashboard/KpiCard';
import ChartCard from '../../components/dashboard/ChartCard';
import RevenueTrendChart from '../../components/charts/RevenueTrendChart';
import BillStatusChart from '../../components/charts/BillStatusChart';
import MonthlyUsageChart from '../../components/charts/MonthlyUsageChart';
import {
  useKpiData,
  useRevenueTrend,
  useBillStatusDistribution,
  useMonthlyUsage,
} from '../../hooks/useDashboard';

interface RevenueDashboardProps {
  period?: 'today' | 'week' | 'month' | 'quarter' | 'year';
}

export const RevenueDashboard: React.FC<RevenueDashboardProps> = ({
  period = 'month',
}) => {
  const currentYear = new Date().getFullYear();

  const {
    data: kpi,
    isLoading: kpiLoading,
    error: kpiError,
  } = useKpiData({ period });

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
    data: monthlyUsage,
    isLoading: monthlyLoading,
    error: monthlyError,
  } = useMonthlyUsage(currentYear);

  const formatCurrency = (value: number): string =>
    `$${value.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;

  const formatNumber = (value: number): string =>
    value.toLocaleString();

  return (
    <div className="space-y-6">
      {/* KPI Cards */}
      <DashboardGrid columns={4}>
        <KpiCard
          title="Today's Revenue"
          value={formatCurrency(kpi?.todayRevenue ?? 0)}
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
          title="Monthly Revenue"
          value={formatCurrency(kpi?.monthlyRevenue ?? 0)}
          icon={
            <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6" />
            </svg>
          }
          color="primary"
          trend={{ direction: 'up', percentage: 12, label: 'vs last month' }}
          isLoading={kpiLoading}
          error={kpiError as Error | null}
        />
        <KpiCard
          title="Paid Bills"
          value={formatNumber(kpi?.paidBills ?? 0)}
          subtitle={`${formatNumber(kpi?.pendingBills ?? 0)} pending`}
          icon={
            <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          }
          color="success"
          isLoading={kpiLoading}
          error={kpiError as Error | null}
        />
        <KpiCard
          title="Overdue Bills"
          value={formatNumber(kpi?.overdueBills ?? 0)}
          subtitle={`Out of ${formatNumber((kpi?.paidBills ?? 0) + (kpi?.pendingBills ?? 0) + (kpi?.overdueBills ?? 0))} total`}
          icon={
            <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4.5c-.77-.833-2.694-.833-3.464 0L3.34 16.5c-.77.833.192 2.5 1.732 2.5z" />
            </svg>
          }
          color="error"
          trend={
            kpi?.overdueBills && kpi.overdueBills > 0
              ? { direction: 'up', percentage: 5, label: 'this month' }
              : undefined
          }
          isLoading={kpiLoading}
          error={kpiError as Error | null}
        />
      </DashboardGrid>

      {/* Charts Row */}
      <div className="grid grid-cols-1 gap-6 lg:grid-cols-2">
        <ChartCard
          title="Revenue Trend"
          subtitle="Revenue vs Expenses over time"
          isLoading={revenueLoading}
          error={revenueError as Error | null}
          height={360}
        >
          <div style={{ height: 300 }}>
            <RevenueTrendChart data={revenueTrend ?? []} />
          </div>
        </ChartCard>

        <ChartCard
          title="Bill Status Distribution"
          subtitle="Payment status breakdown"
          isLoading={billLoading}
          error={billError as Error | null}
          height={360}
        >
          <div style={{ height: 300 }}>
            <BillStatusChart data={billStatus ?? []} />
          </div>
        </ChartCard>
      </div>

      {/* Bottom Row */}
      <div className="grid grid-cols-1 gap-6">
        <ChartCard
          title="Monthly Usage vs Cost"
          subtitle={`Year ${currentYear}`}
          isLoading={monthlyLoading}
          error={monthlyError as Error | null}
          height={360}
        >
          <div style={{ height: 300 }}>
            <MonthlyUsageChart data={monthlyUsage ?? []} />
          </div>
        </ChartCard>
      </div>
    </div>
  );
};

export default RevenueDashboard;
