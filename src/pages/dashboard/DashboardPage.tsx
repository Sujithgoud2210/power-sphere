import React, { lazy, Suspense, useState, useMemo, useCallback } from 'react';
import type { DashboardType } from '../../types/common';

const ExecutiveDashboard = lazy(() =>
  import('../../features/dashboard/ExecutiveDashboard').then((m) => ({
    default: m.ExecutiveDashboard,
  }))
);

const OperationsDashboard = lazy(() =>
  import('../../features/dashboard/OperationsDashboard').then((m) => ({
    default: m.OperationsDashboard,
  }))
);

const EnergyDashboard = lazy(() =>
  import('../../features/dashboard/EnergyDashboard').then((m) => ({
    default: m.EnergyDashboard,
  }))
);

const RevenueDashboard = lazy(() =>
  import('../../features/dashboard/RevenueDashboard').then((m) => ({
    default: m.RevenueDashboard,
  }))
);

interface DashboardNavItem {
  key: DashboardType;
  label: string;
  description: string;
  icon: React.ReactNode;
}

const dashboardNavItems: DashboardNavItem[] = [
  {
    key: 'executive',
    label: 'Executive',
    description: 'High-level business overview',
    icon: (
      <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
      </svg>
    ),
  },
  {
    key: 'operations',
    label: 'Operations',
    description: 'Meter and consumption monitoring',
    icon: (
      <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 3v2m6-2v2M9 19v2m6-2v2M5 9H3m2 6H3m18-6h-2m2 6h-2M7 19h10a2 2 0 002-2V7a2 2 0 00-2-2H7a2 2 0 00-2 2v10a2 2 0 002 2zM9 9h6v6H9V9z" />
      </svg>
    ),
  },
  {
    key: 'energy',
    label: 'Energy',
    description: 'Detailed energy analytics',
    icon: (
      <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 10V3L4 14h7v7l9-11h-7z" />
      </svg>
    ),
  },
  {
    key: 'revenue',
    label: 'Revenue',
    description: 'Financial performance',
    icon: (
      <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
      </svg>
    ),
  },
];

const LoadingFallback: React.FC = () => (
  <div className="flex items-center justify-center py-20">
    <div className="flex flex-col items-center gap-3">
      <div className="h-10 w-10 animate-spin rounded-full border-3 border-gray-200 border-t-blue-600 dark:border-gray-600 dark:border-t-blue-400" />
      <p className="text-sm text-gray-500 dark:text-gray-400">Loading dashboard...</p>
    </div>
  </div>
);

const DashboardPlaceholder: React.FC = () => (
  <div className="flex h-64 items-center justify-center">
    <p className="text-gray-400 dark:text-gray-500">Select a dashboard view</p>
  </div>
);

export const DashboardPage: React.FC = () => {
  const [activeDashboard, setActiveDashboard] = useState<DashboardType>('executive');
  const [period, setPeriod] = useState<'today' | 'week' | 'month' | 'quarter' | 'year'>('month');

  const handleDashboardChange = useCallback((key: DashboardType) => {
    setActiveDashboard(key);
  }, []);

  const activeDashboardComponent = useMemo(() => {
    switch (activeDashboard) {
      case 'executive':
        return <ExecutiveDashboard period={period} />;
      case 'operations':
        return <OperationsDashboard period={period} />;
      case 'energy':
        return <EnergyDashboard period={period} />;
      case 'revenue':
        return <RevenueDashboard period={period} />;
      default:
        return <DashboardPlaceholder />;
    }
  }, [activeDashboard, period]);

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900 dark:text-white">
            Dashboard
          </h1>
          <p className="mt-1 text-sm text-gray-500 dark:text-gray-400">
            Real-time monitoring and analytics
          </p>
        </div>

        <div className="flex items-center gap-2">
          <div className="flex rounded-lg border border-gray-200 bg-white p-0.5 dark:border-gray-600 dark:bg-gray-800">
            {(['today', 'week', 'month', 'quarter', 'year'] as const).map((p) => (
              <button
                key={p}
                type="button"
                onClick={() => setPeriod(p)}
                className={`rounded-md px-3 py-1.5 text-xs font-medium capitalize transition-all focus:outline-none ${
                  period === p
                    ? 'bg-blue-600 text-white shadow-sm'
                    : 'text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200'
                }`}
              >
                {p}
              </button>
            ))}
          </div>
        </div>
      </div>

      {/* Dashboard Navigation Tabs */}
      <nav className="flex flex-wrap gap-2" aria-label="Dashboard views">
        {dashboardNavItems.map((item) => (
          <button
            key={item.key}
            type="button"
            onClick={() => handleDashboardChange(item.key)}
            className={`inline-flex items-center gap-2 rounded-lg border px-4 py-2.5 text-sm font-medium transition-all focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 dark:focus:ring-offset-gray-900 ${
              activeDashboard === item.key
                ? 'border-blue-200 bg-blue-50 text-blue-700 shadow-sm dark:border-blue-700 dark:bg-blue-900/30 dark:text-blue-400'
                : 'border-gray-200 bg-white text-gray-600 hover:bg-gray-50 hover:text-gray-900 dark:border-gray-600 dark:bg-gray-800 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-gray-200'
            }`}
          >
            <span
              className={
                activeDashboard === item.key
                  ? 'text-blue-500 dark:text-blue-400'
                  : 'text-gray-400 dark:text-gray-500'
              }
            >
              {item.icon}
            </span>
            <div className="text-left">
              <span className="block font-medium">{item.label}</span>
              <span className="block text-[10px] font-normal opacity-70">
                {item.description}
              </span>
            </div>
          </button>
        ))}
      </nav>

      {/* Dashboard Content */}
      <Suspense fallback={<LoadingFallback />}>
        {activeDashboardComponent}
      </Suspense>
    </div>
  );
};

export default DashboardPage;
