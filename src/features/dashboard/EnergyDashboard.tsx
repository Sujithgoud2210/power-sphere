import React from 'react';
import DashboardGrid from '../../components/dashboard/DashboardGrid';
import KpiCard from '../../components/dashboard/KpiCard';
import ChartCard from '../../components/dashboard/ChartCard';
import EnergyConsumptionChart from '../../components/charts/EnergyConsumptionChart';
import MonthlyUsageChart from '../../components/charts/MonthlyUsageChart';
import MeterStatusChart from '../../components/charts/MeterStatusChart';
import TopConsumersChart from '../../components/charts/TopConsumersChart';
import {
  useKpiData,
  useEnergyTrend,
  useMonthlyUsage,
  useMeterStatusDistribution,
  useTopConsumers,
} from '../../hooks/useDashboard';

interface EnergyDashboardProps {
  period?: 'today' | 'week' | 'month' | 'quarter' | 'year';
}

export const EnergyDashboard: React.FC<EnergyDashboardProps> = ({
  period = 'month',
}) => {
  const currentYear = new Date().getFullYear();

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
    data: monthlyUsage,
    isLoading: monthlyLoading,
    error: monthlyError,
  } = useMonthlyUsage(currentYear);

  const {
    data: meterStatus,
    isLoading: meterLoading,
    error: meterError,
  } = useMeterStatusDistribution();

  const {
    data: topConsumers,
    isLoading: topLoading,
    error: topError,
  } = useTopConsumers(5);

  const formatNumber = (value: number): string =>
    value.toLocaleString();

  const formatConsumption = (value: number): string =>
    `${value.toLocaleString()} kWh`;

  return (
    <div className="space-y-6">
      {/* KPI Cards */}
      <DashboardGrid columns={4}>
        <KpiCard
          title="Today's Consumption"
          value={formatConsumption(kpi?.todayEnergyConsumption ?? 0)}
          icon={
            <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 10V3L4 14h7v7l9-11h-7z" />
            </svg>
          }
          color="warning"
          isLoading={kpiLoading}
          error={kpiError as Error | null}
        />
        <KpiCard
          title="Monthly Consumption"
          value={formatConsumption(kpi?.monthlyConsumption ?? 0)}
          icon={
            <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
            </svg>
          }
          color="primary"
          trend={{ direction: 'up', percentage: 8, label: 'vs last month' }}
          isLoading={kpiLoading}
          error={kpiError as Error | null}
        />
        <KpiCard
          title="Yearly Consumption"
          value={formatConsumption(kpi?.yearlyConsumption ?? 0)}
          icon={
            <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
            </svg>
          }
          color="info"
          trend={{ direction: 'up', percentage: 15, label: 'vs last year' }}
          isLoading={kpiLoading}
          error={kpiError as Error | null}
        />
        <KpiCard
          title="Smart Meters"
          value={formatNumber(kpi?.totalSmartMeters ?? 0)}
          subtitle={`${kpi?.activeSmartMeters ?? 0} active`}
          icon={
            <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 3v2m6-2v2M9 19v2m6-2v2M5 9H3m2 6H3m18-6h-2m2 6h-2M7 19h10a2 2 0 002-2V7a2 2 0 00-2-2H7a2 2 0 00-2 2v10a2 2 0 002 2zM9 9h6v6H9V9z" />
            </svg>
          }
          color="success"
          isLoading={kpiLoading}
          error={kpiError as Error | null}
        />
      </DashboardGrid>

      {/* Charts Row */}
      <div className="grid grid-cols-1 gap-6">
        <ChartCard
          title="Energy Consumption Trend"
          subtitle="Actual vs predicted consumption"
          isLoading={energyLoading}
          error={energyError as Error | null}
          height={380}
        >
          <div style={{ height: 320 }}>
            <EnergyConsumptionChart
              data={energyTrend ?? []}
              showPredicted
            />
          </div>
        </ChartCard>
      </div>

      {/* Charts Row 2 */}
      <div className="grid grid-cols-1 gap-6 lg:grid-cols-2">
        <ChartCard
          title="Monthly Usage Overview"
          subtitle={`Year ${currentYear}`}
          isLoading={monthlyLoading}
          error={monthlyError as Error | null}
          height={360}
        >
          <div style={{ height: 300 }}>
            <MonthlyUsageChart data={monthlyUsage ?? []} />
          </div>
        </ChartCard>

        <ChartCard
          title="Meter Health Overview"
          subtitle="Status distribution"
          isLoading={meterLoading}
          error={meterError as Error | null}
          height={360}
        >
          <div style={{ height: 300 }}>
            <MeterStatusChart data={meterStatus ?? []} />
          </div>
        </ChartCard>
      </div>

      {/* Charts Row 3 */}
      <div className="grid grid-cols-1 gap-6 lg:grid-cols-1 xl:grid-cols-1">
        <ChartCard
          title="Top Energy Consumers"
          subtitle="Organizations with highest energy usage"
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

export default EnergyDashboard;
