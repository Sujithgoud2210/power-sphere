import React from 'react';
import DashboardGrid from '../../components/dashboard/DashboardGrid';
import KpiCard from '../../components/dashboard/KpiCard';
import ChartCard from '../../components/dashboard/ChartCard';
import MeterStatusChart from '../../components/charts/MeterStatusChart';
import MonthlyUsageChart from '../../components/charts/MonthlyUsageChart';
import TopConsumersChart from '../../components/charts/TopConsumersChart';
import EnergyConsumptionChart from '../../components/charts/EnergyConsumptionChart';
import {
  useKpiData,
  useMeterStatusDistribution,
  useMonthlyUsage,
  useTopConsumers,
  useEnergyTrend,
} from '../../hooks/useDashboard';

interface OperationsDashboardProps {
  period?: 'today' | 'week' | 'month' | 'quarter' | 'year';
}

export const OperationsDashboard: React.FC<OperationsDashboardProps> = ({
  period = 'month',
}) => {
  const currentYear = new Date().getFullYear();

  const {
    data: kpi,
    isLoading: kpiLoading,
    error: kpiError,
  } = useKpiData({ period });

  const {
    data: meterStatus,
    isLoading: meterLoading,
    error: meterError,
  } = useMeterStatusDistribution();

  const {
    data: monthlyUsage,
    isLoading: monthlyLoading,
    error: monthlyError,
  } = useMonthlyUsage(currentYear);

  const {
    data: topConsumers,
    isLoading: topLoading,
    error: topError,
  } = useTopConsumers(8);

  const {
    data: energyTrend,
    isLoading: energyLoading,
    error: energyError,
  } = useEnergyTrend();

  const formatNumber = (value: number): string =>
    value.toLocaleString();

  return (
    <div className="space-y-6">
      {/* KPI Cards */}
      <DashboardGrid columns={4}>
        <KpiCard
          title="Total Smart Meters"
          value={formatNumber(kpi?.totalSmartMeters ?? 0)}
          icon={
            <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 3v2m6-2v2M9 19v2m6-2v2M5 9H3m2 6H3m18-6h-2m2 6h-2M7 19h10a2 2 0 002-2V7a2 2 0 00-2-2H7a2 2 0 00-2 2v10a2 2 0 002 2zM9 9h6v6H9V9z" />
            </svg>
          }
          color="primary"
          isLoading={kpiLoading}
          error={kpiError as Error | null}
        />
        <KpiCard
          title="Active Meters"
          value={formatNumber(kpi?.activeSmartMeters ?? 0)}
          subtitle={`${kpi?.inactiveSmartMeters ?? 0} inactive`}
          icon={
            <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 10V3L4 14h7v7l9-11h-7z" />
            </svg>
          }
          color="success"
          isLoading={kpiLoading}
          error={kpiError as Error | null}
        />
        <KpiCard
          title="Today's Consumption"
          value={`${formatNumber(kpi?.todayEnergyConsumption ?? 0)} kWh`}
          subtitle={`Monthly: ${formatNumber(kpi?.monthlyConsumption ?? 0)} kWh`}
          icon={
            <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 10V3L4 14h7v7l9-11h-7z" />
            </svg>
          }
          color="warning"
          trend={
            kpi
              ? {
                  direction: 'down',
                  percentage: 12,
                  label: 'vs yesterday',
                }
              : undefined
          }
          isLoading={kpiLoading}
          error={kpiError as Error | null}
        />
        <KpiCard
          title="Unread Notifications"
          value={formatNumber(kpi?.unreadNotifications ?? 0)}
          icon={
            <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9" />
            </svg>
          }
          color="error"
          isLoading={kpiLoading}
          error={kpiError as Error | null}
        />
      </DashboardGrid>

      {/* Charts Row */}
      <div className="grid grid-cols-1 gap-6 lg:grid-cols-2">
        <ChartCard
          title="Meter Status Distribution"
          subtitle="Active, inactive, and maintenance meters"
          isLoading={meterLoading}
          error={meterError as Error | null}
          height={360}
        >
          <div style={{ height: 300 }}>
            <MeterStatusChart data={meterStatus ?? []} />
          </div>
        </ChartCard>

        <ChartCard
          title="Monthly Usage"
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

      {/* Bottom Charts Row */}
      <div className="grid grid-cols-1 gap-6 lg:grid-cols-2">
        <ChartCard
          title="Energy Consumption Trend"
          subtitle="Last 7 days"
          isLoading={energyLoading}
          error={energyError as Error | null}
          height={320}
        >
          <div style={{ height: 260 }}>
            <EnergyConsumptionChart
              data={energyTrend?.slice(-7) ?? []}
              showPredicted
            />
          </div>
        </ChartCard>

        <ChartCard
          title="Top Consumers"
          subtitle="Organizations with highest usage"
          isLoading={topLoading}
          error={topError as Error | null}
          height={360}
        >
          <div style={{ height: 300 }}>
            <TopConsumersChart data={topConsumers ?? []} />
          </div>
        </ChartCard>
      </div>
    </div>
  );
};

export default OperationsDashboard;
