import React from 'react';

interface KpiCardProps {
  title: string;
  value: string | number;
  subtitle?: string;
  icon?: React.ReactNode;
  trend?: {
    direction: 'up' | 'down';
    percentage: number;
    label?: string;
  };
  color?: 'primary' | 'success' | 'warning' | 'error' | 'info';
  isLoading?: boolean;
  error?: Error | null;
  onClick?: () => void;
}

const colorMap: Record<string, { bg: string; text: string; border: string; iconBg: string }> = {
  primary: {
    bg: 'bg-blue-50 dark:bg-blue-900/20',
    text: 'text-blue-700 dark:text-blue-300',
    border: 'border-blue-200 dark:border-blue-800',
    iconBg: 'bg-blue-100 dark:bg-blue-800/40',
  },
  success: {
    bg: 'bg-green-50 dark:bg-green-900/20',
    text: 'text-green-700 dark:text-green-300',
    border: 'border-green-200 dark:border-green-800',
    iconBg: 'bg-green-100 dark:bg-green-800/40',
  },
  warning: {
    bg: 'bg-amber-50 dark:bg-amber-900/20',
    text: 'text-amber-700 dark:text-amber-300',
    border: 'border-amber-200 dark:border-amber-800',
    iconBg: 'bg-amber-100 dark:bg-amber-800/40',
  },
  error: {
    bg: 'bg-red-50 dark:bg-red-900/20',
    text: 'text-red-700 dark:text-red-300',
    border: 'border-red-200 dark:border-red-800',
    iconBg: 'bg-red-100 dark:bg-red-800/40',
  },
  info: {
    bg: 'bg-indigo-50 dark:bg-indigo-900/20',
    text: 'text-indigo-700 dark:text-indigo-300',
    border: 'border-indigo-200 dark:border-indigo-800',
    iconBg: 'bg-indigo-100 dark:bg-indigo-800/40',
  },
};

export const KpiCard: React.FC<KpiCardProps> = ({
  title,
  value,
  subtitle,
  icon,
  trend,
  color = 'primary',
  isLoading = false,
  error = null,
  onClick,
}) => {
  const styles = colorMap[color];

  if (error) {
    return (
      <div
        className={`rounded-xl border ${colorMap.error.border} ${colorMap.error.bg} p-5 transition-shadow hover:shadow-md`}
        role="alert"
      >
        <p className={`text-sm font-medium ${colorMap.error.text}`}>
          Failed to load {title}
        </p>
        <p className="mt-1 text-xs text-gray-500 dark:text-gray-400">
          {error.message}
        </p>
      </div>
    );
  }

  return (
    <button
      type="button"
      onClick={onClick}
      disabled={!onClick}
      className={`relative rounded-xl border ${styles.border} ${styles.bg} p-5 transition-all duration-200 ${
        onClick
          ? 'cursor-pointer hover:shadow-lg hover:-translate-y-0.5 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 dark:focus:ring-offset-gray-900'
          : 'cursor-default'
      } ${isLoading ? 'animate-pulse' : ''}`}
      aria-label={`${title}: ${isLoading ? 'Loading...' : value}`}
    >
      <div className="flex items-start justify-between">
        <div className="flex-1">
          <p className="text-sm font-medium text-gray-500 dark:text-gray-400">
            {title}
          </p>
          {isLoading ? (
            <div className="mt-2 h-8 w-24 rounded bg-gray-200 dark:bg-gray-700" />
          ) : (
            <p className={`mt-1 text-2xl font-bold tracking-tight ${styles.text}`}>
              {value}
            </p>
          )}
          {subtitle && !isLoading && (
            <p className="mt-1 text-xs text-gray-400 dark:text-gray-500">
              {subtitle}
            </p>
          )}
        </div>
        {icon && (
          <div className={`rounded-lg ${styles.iconBg} p-2.5`}>
            {icon}
          </div>
        )}
      </div>
      {trend && !isLoading && (
        <div className="mt-3 flex items-center gap-1.5">
          <span
            className={`inline-flex items-center gap-0.5 rounded-full px-2 py-0.5 text-xs font-medium ${
              trend.direction === 'up'
                ? 'bg-green-100 text-green-700 dark:bg-green-900/40 dark:text-green-400'
                : 'bg-red-100 text-red-700 dark:bg-red-900/40 dark:text-red-400'
            }`}
          >
            <svg
              className={`h-3 w-3 ${trend.direction === 'up' ? '' : 'rotate-180'}`}
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
              aria-hidden="true"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M5 10l7-7m0 0l7 7m-7-7v18"
              />
            </svg>
            {trend.percentage}%
          </span>
          {trend.label && (
            <span className="text-xs text-gray-400 dark:text-gray-500">
              {trend.label}
            </span>
          )}
        </div>
      )}
    </button>
  );
};

export default KpiCard;
