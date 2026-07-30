import React, { type ReactNode } from 'react';

interface ChartCardProps {
  title: string;
  subtitle?: string;
  children: ReactNode;
  headerAction?: ReactNode;
  isLoading?: boolean;
  error?: Error | null;
  height?: number | string;
  onRetry?: () => void;
}

export const ChartCard: React.FC<ChartCardProps> = ({
  title,
  subtitle,
  children,
  headerAction,
  isLoading = false,
  error = null,
  height = 300,
  onRetry,
}) => {
  return (
    <div className="rounded-xl border border-gray-200 bg-white shadow-sm transition-all duration-200 hover:shadow-md dark:border-gray-700 dark:bg-gray-800">
      <div className="flex items-center justify-between border-b border-gray-100 px-5 py-4 dark:border-gray-700">
        <div>
          <h3 className="text-base font-semibold text-gray-900 dark:text-white">
            {title}
          </h3>
          {subtitle && (
            <p className="mt-0.5 text-sm text-gray-500 dark:text-gray-400">
              {subtitle}
            </p>
          )}
        </div>
        {headerAction && (
          <div className="flex-shrink-0">{headerAction}</div>
        )}
      </div>
      <div
        className="px-5 py-4"
        style={{ minHeight: typeof height === 'number' ? height : undefined }}
      >
        {error ? (
          <div
            className="flex h-full flex-col items-center justify-center py-8 text-center"
            role="alert"
          >
            <svg
              className="mb-3 h-10 w-10 text-red-400"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
              aria-hidden="true"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={1.5}
                d="M12 9v3.75m9-.75a9 9 0 11-18 0 9 9 0 0118 0zm-9 3.75h.008v.008H12v-.008z"
              />
            </svg>
            <p className="text-sm font-medium text-gray-900 dark:text-white">
              Failed to load chart
            </p>
            <p className="mt-1 text-xs text-gray-500 dark:text-gray-400">
              {error.message}
            </p>
            {onRetry && (
              <button
                type="button"
                onClick={onRetry}
                className="mt-3 rounded-lg bg-blue-600 px-4 py-1.5 text-xs font-medium text-white transition-colors hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 dark:focus:ring-offset-gray-800"
              >
                Retry
              </button>
            )}
          </div>
        ) : isLoading ? (
          <div className="flex h-full items-center justify-center py-8">
            <div className="flex flex-col items-center gap-3">
              <div className="h-8 w-8 animate-spin rounded-full border-3 border-gray-200 border-t-blue-600 dark:border-gray-600 dark:border-t-blue-400" />
              <span className="text-sm text-gray-400 dark:text-gray-500">
                Loading chart...
              </span>
            </div>
          </div>
        ) : (
          children
        )}
      </div>
    </div>
  );
};

export default ChartCard;
