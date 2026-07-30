import React from 'react';
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  type TooltipProps,
} from 'recharts';
import type { TopOrganization } from '../../types/dashboard';

interface TopOrganizationsChartProps {
  data: TopOrganization[];
  isLoading?: boolean;
}

const CustomTooltip: React.FC<TooltipProps<number, string>> = ({
  active,
  payload,
  label,
}) => {
  if (!active || !payload?.length) return null;

  const entry = payload[0];
  const row = entry.payload as TopOrganization;

  return (
    <div className="rounded-lg border border-gray-200 bg-white p-3 shadow-lg dark:border-gray-700 dark:bg-gray-800">
      <p className="text-sm font-medium text-gray-900 dark:text-white">
        {label}
      </p>
      <p className="text-sm text-blue-500">
        {Number(entry.value).toLocaleString()} kWh consumed
      </p>
      <div className="mt-1 text-xs text-gray-500 dark:text-gray-400">
        Revenue: ${row.revenue.toLocaleString()}
      </div>
      <div className="text-xs text-gray-500 dark:text-gray-400">
        Meters: {row.meters}
      </div>
    </div>
  );
};

export const TopOrganizationsChart: React.FC<TopOrganizationsChartProps> = ({
  data,
  isLoading = false,
}) => {
  if (isLoading) {
    return (
      <div className="flex h-full items-center justify-center">
        <div className="h-6 w-6 animate-spin rounded-full border-2 border-indigo-600 border-t-transparent" />
      </div>
    );
  }

  if (!data.length) {
    return (
      <div className="flex h-full items-center justify-center">
        <p className="text-sm text-gray-400 dark:text-gray-500">No data available</p>
      </div>
    );
  }

  return (
    <ResponsiveContainer width="100%" height="100%">
      <BarChart
        data={data}
        layout="vertical"
        margin={{ top: 5, right: 30, left: 20, bottom: 5 }}
      >
        <CartesianGrid
          strokeDasharray="3 3"
          stroke="#e5e7eb"
          horizontal={false}
          className="dark:[&>*]:stroke-gray-700"
        />
        <XAxis
          type="number"
          tick={{ fontSize: 11, fill: '#9ca3af' }}
          tickLine={false}
          axisLine={{ stroke: '#e5e7eb' }}
          tickFormatter={(value) => `${(value / 1000).toFixed(0)}k`}
        />
        <YAxis
          dataKey="organizationName"
          type="category"
          tick={{ fontSize: 11, fill: '#6b7280' }}
          tickLine={false}
          axisLine={false}
          width={130}
        />
        <Tooltip content={<CustomTooltip />} />
        <Bar
          dataKey="consumption"
          name="Consumption"
          fill="#6366f1"
          radius={[0, 4, 4, 0]}
          animationDuration={1000}
        />
      </BarChart>
    </ResponsiveContainer>
  );
};

export default TopOrganizationsChart;
