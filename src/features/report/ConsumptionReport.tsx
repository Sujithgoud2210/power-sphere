import React, { useState } from 'react';
import ReportCard from '../../components/report/ReportCard';

export const ConsumptionReport: React.FC = () => {
  const [isLoading, setIsLoading] = useState(false);
  const [reportFormat, setReportFormat] = useState<'pdf' | 'csv' | 'xlsx'>('pdf');
  const [dateRange, setDateRange] = useState({
    start: new Date(Date.now() - 30 * 86400000).toISOString().slice(0, 10),
    end: new Date().toISOString().slice(0, 10),
  });

  const handleDownload = async () => {
    setIsLoading(true);
    await new Promise((resolve) => setTimeout(resolve, 2000));
    setIsLoading(false);
  };

  return (
    <div className="space-y-6">
      <div>
        <h2 className="text-lg font-semibold text-gray-900 dark:text-white">
          Consumption Report
        </h2>
        <p className="mt-1 text-sm text-gray-500 dark:text-gray-400">
          Detailed energy consumption analysis with customizable date range
        </p>
      </div>

      {/* Filters */}
      <div className="flex flex-wrap items-center gap-4 rounded-lg border border-gray-200 bg-white p-4 dark:border-gray-700 dark:bg-gray-800">
        <div>
          <label htmlFor="start-date" className="mb-1 block text-xs font-medium text-gray-600 dark:text-gray-400">
            Start Date
          </label>
          <input
            id="start-date"
            type="date"
            value={dateRange.start}
            onChange={(e) => setDateRange((prev) => ({ ...prev, start: e.target.value }))}
            className="rounded-lg border border-gray-200 bg-white px-3 py-1.5 text-sm text-gray-700 focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-500/20 dark:border-gray-600 dark:bg-gray-800 dark:text-gray-300"
          />
        </div>
        <div>
          <label htmlFor="end-date" className="mb-1 block text-xs font-medium text-gray-600 dark:text-gray-400">
            End Date
          </label>
          <input
            id="end-date"
            type="date"
            value={dateRange.end}
            onChange={(e) => setDateRange((prev) => ({ ...prev, end: e.target.value }))}
            className="rounded-lg border border-gray-200 bg-white px-3 py-1.5 text-sm text-gray-700 focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-500/20 dark:border-gray-600 dark:bg-gray-800 dark:text-gray-300"
          />
        </div>
        <div>
          <label htmlFor="report-format" className="mb-1 block text-xs font-medium text-gray-600 dark:text-gray-400">
            Format
          </label>
          <select
            id="report-format"
            value={reportFormat}
            onChange={(e) => setReportFormat(e.target.value as 'pdf' | 'csv' | 'xlsx')}
            className="rounded-lg border border-gray-200 bg-white px-3 py-1.5 text-sm text-gray-700 focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-500/20 dark:border-gray-600 dark:bg-gray-800 dark:text-gray-300"
          >
            <option value="pdf">PDF</option>
            <option value="csv">CSV</option>
            <option value="xlsx">XLSX</option>
          </select>
        </div>
        <button
          type="button"
          onClick={() => setDateRange({
            start: new Date(Date.now() - 30 * 86400000).toISOString().slice(0, 10),
            end: new Date().toISOString().slice(0, 10),
          })}
          className="mt-4 rounded-lg border border-gray-200 bg-white px-3 py-1.5 text-xs font-medium text-gray-600 hover:bg-gray-50 dark:border-gray-600 dark:bg-gray-700 dark:text-gray-400 dark:hover:bg-gray-600 sm:mt-0"
        >
          Last 30 Days
        </button>
        <button
          type="button"
          onClick={() => setDateRange({
            start: new Date(Date.now() - 7 * 86400000).toISOString().slice(0, 10),
            end: new Date().toISOString().slice(0, 10),
          })}
          className="mt-4 rounded-lg border border-gray-200 bg-white px-3 py-1.5 text-xs font-medium text-gray-600 hover:bg-gray-50 dark:border-gray-600 dark:bg-gray-700 dark:text-gray-400 dark:hover:bg-gray-600 sm:mt-0"
        >
          Last 7 Days
        </button>
      </div>

      {/* Report Cards */}
      <div className="grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-3">
        <ReportCard
          title="Detailed Consumption"
          description="Per-meter consumption breakdown with time intervals"
          icon={
            <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 10V3L4 14h7v7l9-11h-7z" />
            </svg>
          }
          format={reportFormat.toUpperCase()}
          isGenerated
          onDownload={handleDownload}
          isLoading={isLoading}
        />
        <ReportCard
          title="Peak Analysis"
          description="Peak consumption times, demand charges, and optimization opportunities"
          icon={
            <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6" />
            </svg>
          }
          format={reportFormat.toUpperCase()}
          isGenerated
          onDownload={handleDownload}
          isLoading={isLoading}
        />
        <ReportCard
          title="Trend Analysis"
          description="Consumption patterns, seasonal trends, and forecasting"
          icon={
            <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M7 12l3-3 3 3 4-4M8 21l4-4 4 4M3 4h18M4 4h16v12a1 1 0 01-1 1H5a1 1 0 01-1-1V4z" />
            </svg>
          }
          format={reportFormat.toUpperCase()}
          isGenerated
          onDownload={handleDownload}
          isLoading={isLoading}
        />
      </div>
    </div>
  );
};

export default ConsumptionReport;
