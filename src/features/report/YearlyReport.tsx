import React, { useState } from 'react';
import ReportCard from '../../components/report/ReportCard';

export const YearlyReport: React.FC = () => {
  const [isLoading, setIsLoading] = useState(false);

  const handleDownload = async () => {
    setIsLoading(true);
    await new Promise((resolve) => setTimeout(resolve, 1500));
    setIsLoading(false);
  };

  const currentYear = new Date().getFullYear().toString();
  const years = Array.from({ length: 5 }, (_, i) => (currentYear - i).toString());

  return (
    <div className="space-y-6">
      <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <div>
          <h2 className="text-lg font-semibold text-gray-900 dark:text-white">
            Yearly Report
          </h2>
          <p className="mt-1 text-sm text-gray-500 dark:text-gray-400">
            Annual performance and analytics
          </p>
        </div>
        <select
          className="rounded-lg border border-gray-200 bg-white px-3 py-2 text-sm text-gray-700 focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-500/20 dark:border-gray-600 dark:bg-gray-800 dark:text-gray-300"
          defaultValue={currentYear}
          aria-label="Select year"
        >
          {years.map((year) => (
            <option key={year} value={year}>
              {year}
            </option>
          ))}
        </select>
      </div>

      <div className="grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-3">
        <ReportCard
          title="Annual Executive Summary"
          description="Year-over-year comparison of all key business metrics"
          icon={
            <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
            </svg>
          }
          format="PDF"
          isGenerated
          onDownload={handleDownload}
          isLoading={isLoading}
          lastGenerated={`Year ${currentYear}`}
        />
        <ReportCard
          title="Annual Consumption Report"
          description="Yearly energy consumption trends, seasonality, and growth patterns"
          icon={
            <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 10V3L4 14h7v7l9-11h-7z" />
            </svg>
          }
          format="PDF"
          isGenerated
          onDownload={handleDownload}
          isLoading={isLoading}
        />
        <ReportCard
          title="Annual Financial Report"
          description="Comprehensive annual financial analysis with projections"
          icon={
            <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          }
          format="PDF"
          isGenerated
          onDownload={handleDownload}
          isLoading={isLoading}
        />
      </div>
    </div>
  );
};

export default YearlyReport;
