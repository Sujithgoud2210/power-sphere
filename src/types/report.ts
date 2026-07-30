import type { ReportType, DateRange } from './common';

export interface Report {
  id: string;
  type: ReportType;
  title: string;
  description: string;
  generatedAt: string;
  dateRange: DateRange;
  format: 'pdf' | 'csv' | 'xlsx';
  size?: string;
  url?: string;
}

export interface ReportFilters {
  type?: ReportType;
  startDate?: string;
  endDate?: string;
  format?: 'pdf' | 'csv' | 'xlsx';
}

export interface ConsumptionReportData {
  totalConsumption: number;
  averageDailyConsumption: number;
  peakConsumption: number;
  peakDate: string;
  consumptionByMeter: { meterId: string; meterName: string; consumption: number; percentage: number }[];
  consumptionByHour: { hour: number; consumption: number }[];
}

export interface BillingReportData {
  totalBilled: number;
  totalCollected: number;
  totalOutstanding: number;
  collectionRate: number;
  billsByStatus: { status: string; count: number; amount: number }[];
  billsByPeriod: { period: string; billed: number; collected: number }[];
}

export interface OrganizationReportData {
  totalOrganizations: number;
  activeOrganizations: number;
  totalMeters: number;
  totalConsumption: number;
  totalRevenue: number;
  organizationsByType: { type: string; count: number }[];
  topOrganizations: { name: string; consumption: number; revenue: number }[];
}
