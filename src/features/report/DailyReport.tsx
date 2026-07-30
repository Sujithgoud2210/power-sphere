import React, { useState } from 'react';
import ReportCard from '../../components/report/ReportCard';

export const DailyReport: React.FC = () => {
  const [isLoading, setIsLoading] = useState(false);

  const handleDownload = async () => {
    setIsLoading(true);
    // Placeholder: simulate download
    await new Promise((resolve) => setTimeout(resolve, 1500));
    setIsLoading(false);
  };

  const today = new Date().toLocaleDateString('en-US', {
    weekday: 'long',
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  });

  return (
    <div className="space-y-6">
      <div>
        <h2 className="text-lg font-semibold text-gray-900 dark:text-white">
          Daily Report
        </h2>
        <p className="mt-1 text-sm text-gray-500 dark:text-gray-400">
          Energy consumption and billing summary for {today}
        </p>
      </div>

      <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
        <ReportCard
          title="Consumption Summary"
          description="Detailed breakdown of today's energy consumption across all meters"
          icon={
            <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 10V3L4 14h7v7l9-11h-7z" />
            </svg>
          }
          format="PDF"
          isGenerated
          onDownload={handleDownload}
          isLoading={isLoading}
          lastGenerated={today}
        />
        <ReportCard
          title="Billing Summary"
          description="Overview of today's billing activities and payment status"
          icon={
            <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          }
          format="PDF"
          isGenerated
          onDownload={handleDownload}
          isLoading={isLoading}
          lastGenerated={today}
        />
        <ReportCard
          title="Alerts & Events"
          description="List of all system alerts, notifications, and events from today"
          icon={
            <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9" />
            </svg>
          }
          format="PDF"
          isGenerated
          onDownload={handleDownload}
          isLoading={isLoading}
        />
        <ReportCard
          title="Meter Performance"
          description="Performance metrics and status updates for all smart meters"
          icon={
            <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 3v2m6-2v2M9 19v2m6-2v2M5 9H3m2 6H3m18-6h-2m2 6h-2M7 19h10a2 2 0 002-2V7a2 2 0 00-2-2H7a2 2 0 00-2 2v10a2 2 0 002 2zM9 9h6v6H9V9z" />
            </svg>
          }
          format="CSV"
          isGenerated
          onDownload={handleDownload}
          isLoading={isLoading}
        />
      </div>
    </div>
  );
};

export default DailyReport;
