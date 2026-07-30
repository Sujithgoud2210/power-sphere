import { apiClient } from '../apiClient';
import type {
  Report,
  ReportFilters,
  ConsumptionReportData,
  BillingReportData,
  OrganizationReportData,
} from '../../types/report';
import type { DateRange } from '../../types/common';

function buildFilters(
  filters?: ReportFilters,
  page: number = 1,
  pageSize: number = 20
): Record<string, string> {
  const params: Record<string, string> = {
    page: page.toString(),
    pageSize: pageSize.toString(),
  };

  if (filters?.type) params.type = filters.type;
  if (filters?.startDate) params.startDate = filters.startDate;
  if (filters?.endDate) params.endDate = filters.endDate;
  if (filters?.format) params.format = filters.format;

  return params;
}

export async function fetchReports(
  filters?: ReportFilters,
  page: number = 1,
  pageSize: number = 20
): Promise<{ data: Report[]; total: number }> {
  return apiClient.get<{ data: Report[]; total: number }>(
    '/reports',
    buildFilters(filters, page, pageSize)
  );
}

export async function generateReport(
  type: string,
  dateRange: DateRange,
  format: 'pdf' | 'csv' | 'xlsx' = 'pdf'
): Promise<Report> {
  return apiClient.post<Report>('/reports/generate', { type, dateRange, format });
}

export async function downloadReport(reportId: string): Promise<Blob> {
  return apiClient.downloadBlob(`/reports/${reportId}/download`);
}

export async function fetchConsumptionReport(
  dateRange: DateRange
): Promise<ConsumptionReportData> {
  return apiClient.post<ConsumptionReportData>('/reports/consumption', dateRange);
}

export async function fetchBillingReport(
  dateRange: DateRange
): Promise<BillingReportData> {
  return apiClient.post<BillingReportData>('/reports/billing', dateRange);
}

export async function fetchOrganizationReport(
  dateRange: DateRange
): Promise<OrganizationReportData> {
  return apiClient.post<OrganizationReportData>('/reports/organization', dateRange);
}

export function getReportDownloadUrl(reportId: string): string {
  return apiClient.getDownloadUrl(`/reports/${reportId}/download`);
}
