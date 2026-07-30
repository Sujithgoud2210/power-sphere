import React, { useState } from 'react';
import ReportCard from '../../components/report/ReportCard';

export const WeeklyReport: React.FC = () => {
  const [isLoading, setIsLoading] = useState(false);

  const handleDownload = async () => {
    setIsLoading(true);
    await new Promise((resolve) => setTimeout(resolve, 1500));
    setIsLoading(false);
  };

  const weekRange = (() => {
    const end = new Date();
    const start = new Date(end);
    start.setDate(start.getDate() - 7);
    return `${start.toLocaleDateString()} - ${end.toLocaleDateString()}`;
  })();

  return (
    <div className="space-y-6">
      <div>
        <h2 className="text-lg font-semibold text-gray-900 dark:text-white">
          Weekly Report
        </h2>
        <p className="mt-1 text-sm text-gray-500 dark:text-gray-400">
          {weekRange}
        </p>
      </div>

      <div className="grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-3">
        <ReportCard
          title="Energy Consumption"
          description="Weekly energy consumption trends and peak usage analysis"
          icon={
            <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 10V3L4 14h7v7l9-11h-7z" />
            </svg>
          }
          format="PDF"
          isGenerated
          onDownload={handleDownload}
          isLoading={isLoading}
          lastGenerated={weekRange}
        />
        <ReportCard
          title="Revenue Analysis"
          description="Weekly revenue breakdown with comparison to previous week"
          icon={
            <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6" />
            </svg>
          }
          format="PDF"
          isGenerated
          onDownload={handleDownload}
          isLoading={isLoading}
        />
        <ReportCard
          title="Billing Summary"
          description="Summary of billing activities, payments, and outstanding amounts"
          icon={
            <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          }
          format="XLSX"
          isGenerated
          onDownload={handleDownload}
          isLoading={isLoading}
        />
      </div>
    </div>
  );
};

export default WeeklyReport;
