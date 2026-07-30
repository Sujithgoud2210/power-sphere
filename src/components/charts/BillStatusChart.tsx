import React from 'react';
import {
  PieChart,
  Pie,
  Cell,
  Tooltip,
  ResponsiveContainer,
  Legend,
  type TooltipProps,
} from 'recharts';
import type { BillStatusDistribution } from '../../types/dashboard';

interface BillStatusChartProps {
  data: BillStatusDistribution[];
  isLoading?: boolean;
}

const COLORS: Record<string, string> = {
  pending: '#f59e0b',
  paid: '#16a34a',
  overdue: '#ef4444',
};

const CustomTooltip: React.FC<TooltipProps<number, string>> = ({
  active,
  payload,
}) => {
  if (!active || !payload?.length) return null;

  const entry = payload[0];
  return (
    <div className="rounded-lg border border-gray-200 bg-white p-3 shadow-lg dark:border-gray-700 dark:bg-gray-800">
      <p className="text-sm font-medium text-gray-900 dark:text-white capitalize">
        {entry.name}
      </p>
      <p className="text-sm" style={{ color: entry.color }}>
        {entry.payload.count} bills (${Number(entry.payload.amount).toLocaleString()})
      </p>
      <p className="text-xs text-gray-500">{Number(entry.payload.percentage).toFixed(1)}%</p>
    </div>
  );
};

export const BillStatusChart: React.FC<BillStatusChartProps> = ({
  data,
  isLoading = false,
}) => {
  if (isLoading) {
    return (
      <div className="flex h-full items-center justify-center">
        <div className="h-6 w-6 animate-spin rounded-full border-2 border-amber-600 border-t-transparent" />
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
      <PieChart>
        <Pie
          data={data}
          cx="50%"
          cy="50%"
          innerRadius={60}
          outerRadius={100}
          paddingAngle={4}
          dataKey="count"
          nameKey="status"
          animationDuration={800}
          animationBegin={0}
        >
          {data.map((entry) => (
            <Cell
              key={entry.status}
              fill={COLORS[entry.status] || '#9ca3af'}
              stroke="transparent"
            />
          ))}
        </Pie>
        <Tooltip content={<CustomTooltip />} />
        <Legend
          wrapperStyle={{ fontSize: 12, paddingTop: 8 }}
          formatter={(value: string) => (
            <span className="capitalize text-gray-700 dark:text-gray-300">
              {value}
            </span>
          )}
        />
      </PieChart>
    </ResponsiveContainer>
  );
};

export default BillStatusChart;
