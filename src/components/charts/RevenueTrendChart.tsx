import React from 'react';
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  Legend,
  type TooltipProps,
} from 'recharts';
import type { RevenueTrendPoint } from '../../types/dashboard';

interface RevenueTrendChartProps {
  data: RevenueTrendPoint[];
  isLoading?: boolean;
}

const CustomTooltip: React.FC<TooltipProps<number, string>> = ({
  active,
  payload,
  label,
}) => {
  if (!active || !payload?.length) return null;

  return (
    <div className="rounded-lg border border-gray-200 bg-white p-3 shadow-lg dark:border-gray-700 dark:bg-gray-800">
      <p className="text-sm font-medium text-gray-900 dark:text-white">
        {label}
      </p>
      {payload.map((entry, index) => (
        <p
          key={index}
          className="text-sm"
          style={{ color: entry.color }}
        >{`${entry.name}: $${Number(entry.value).toLocaleString()}`}</p>
      ))}
    </div>
  );
};

export const RevenueTrendChart: React.FC<RevenueTrendChartProps> = ({
  data,
  isLoading = false,
}) => {
  if (isLoading) {
    return (
      <div className="flex h-full items-center justify-center">
        <div className="h-6 w-6 animate-spin rounded-full border-2 border-green-600 border-t-transparent" />
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
        margin={{ top: 5, right: 20, left: 0, bottom: 5 }}
      >
        <CartesianGrid
          strokeDasharray="3 3"
          stroke="#e5e7eb"
          className="dark:[&>*]:stroke-gray-700"
        />
        <XAxis
          dataKey="date"
          tick={{ fontSize: 11, fill: '#9ca3af' }}
          tickLine={false}
          axisLine={{ stroke: '#e5e7eb' }}
        />
        <YAxis
          tick={{ fontSize: 11, fill: '#9ca3af' }}
          tickLine={false}
          axisLine={false}
          tickFormatter={(value) => `$${(value / 1000).toFixed(0)}k`}
        />
        <Tooltip content={<CustomTooltip />} />
        <Legend
          wrapperStyle={{ fontSize: 12, paddingTop: 8 }}
        />
        <Bar
          dataKey="revenue"
          name="Revenue"
          fill="#16a34a"
          radius={[4, 4, 0, 0]}
          animationDuration={1000}
        />
        <Bar
          dataKey="expenses"
          name="Expenses"
          fill="#ef4444"
          radius={[4, 4, 0, 0]}
          animationDuration={1000}
        />
      </BarChart>
    </ResponsiveContainer>
  );
};

export default RevenueTrendChart;
