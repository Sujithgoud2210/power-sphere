import React, { type ReactNode } from 'react';

interface ReportCardProps {
  title: string;
  description: string;
  icon?: ReactNode;
  onDownload?: () => void;
  onGenerate?: () => void;
  isGenerated?: boolean;
  lastGenerated?: string;
  isLoading?: boolean;
  format?: string;
}

export const ReportCard: React.FC<ReportCardProps> = ({
  title,
  description,
  icon,
  onDownload,
  onGenerate,
  isGenerated = false,
  lastGenerated,
  isLoading = false,
  format = 'PDF',
}) => {
  return (
    <div className="group rounded-xl border border-gray-200 bg-white p-5 shadow-sm transition-all duration-200 hover:shadow-md dark:border-gray-700 dark:bg-gray-800">
      <div className="flex items-start justify-between">
        <div className="flex items-start gap-3">
          {icon && (
            <div className="rounded-lg bg-indigo-50 p-2.5 text-indigo-600 dark:bg-indigo-900/30 dark:text-indigo-400">
              {icon}
            </div>
          )}
          <div>
            <h3 className="text-base font-semibold text-gray-900 dark:text-white">
              {title}
            </h3>
            <p className="mt-1 text-sm text-gray-500 dark:text-gray-400">
              {description}
            </p>
            {lastGenerated && (
              <p className="mt-1 text-xs text-gray-400 dark:text-gray-500">
                Last generated: {lastGenerated}
              </p>
            )}
          </div>
        </div>
        {format && (
          <span className="rounded-md bg-gray-100 px-2 py-1 text-[10px] font-semibold uppercase tracking-wider text-gray-600 dark:bg-gray-700 dark:text-gray-400">
            {format}
          </span>
        )}
      </div>

      <div className="mt-4 flex items-center gap-2">
        {isGenerated && onDownload && (
          <button
            type="button"
            onClick={onDownload}
            disabled={isLoading}
            className="inline-flex items-center gap-1.5 rounded-lg bg-indigo-600 px-3 py-1.5 text-xs font-medium text-white transition-all hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50 dark:focus:ring-offset-gray-800"
          >
            {isLoading ? (
              <>
                <div className="h-3 w-3 animate-spin rounded-full border-2 border-white border-t-transparent" />
                Downloading...
              </>
            ) : (
              <>
                <svg className="h-3.5 w-3.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4" />
                </svg>
                Download
              </>
            )}
          </button>
        )}
        {!isGenerated && onGenerate && (
          <button
            type="button"
            onClick={onGenerate}
            disabled={isLoading}
            className="inline-flex items-center gap-1.5 rounded-lg bg-gray-900 px-3 py-1.5 text-xs font-medium text-white transition-all hover:bg-gray-800 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50 dark:bg-gray-600 dark:hover:bg-gray-500 dark:focus:ring-offset-gray-800"
          >
            {isLoading ? (
              <>
                <div className="h-3 w-3 animate-spin rounded-full border-2 border-white border-t-transparent" />
                Generating...
              </>
            ) : (
              <>
                <svg className="h-3.5 w-3.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
                </svg>
                Generate
              </>
            )}
          </button>
        )}
      </div>
    </div>
  );
};

export default ReportCard;
