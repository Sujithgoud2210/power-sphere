import { useQuery, useMutation } from '@tanstack/react-query';
import type { Report, ReportFilters } from '../types/report';
import type { DateRange } from '../types/common';
import {
  fetchReports,
  generateReport,
  fetchConsumptionReport,
  fetchBillingReport,
  fetchOrganizationReport,
} from '../services/report/reportService';

export function useReports(
  filters?: ReportFilters,
  page: number = 1,
  pageSize: number = 20
) {
  return useQuery<{ data: Report[]; total: number }>({
    queryKey: ['reports', filters, page, pageSize],
    queryFn: () => fetchReports(filters, page, pageSize),
    staleTime: 30_000,
  });
}

export function useGenerateReport() {
  return useMutation({
    mutationFn: ({
      type,
      dateRange,
      format,
    }: {
      type: string;
      dateRange: DateRange;
      format?: 'pdf' | 'csv' | 'xlsx';
    }) => generateReport(type, dateRange, format),
  });
}

export function useConsumptionReport(dateRange: DateRange) {
  return useQuery({
    queryKey: ['report', 'consumption', dateRange],
    queryFn: () => fetchConsumptionReport(dateRange),
    enabled: !!dateRange.startDate && !!dateRange.endDate,
    staleTime: 60_000,
  });
}

export function useBillingReport(dateRange: DateRange) {
  return useQuery({
    queryKey: ['report', 'billing', dateRange],
    queryFn: () => fetchBillingReport(dateRange),
    enabled: !!dateRange.startDate && !!dateRange.endDate,
    staleTime: 60_000,
  });
}

export function useOrganizationReport(dateRange: DateRange) {
  return useQuery({
    queryKey: ['report', 'organization', dateRange],
    queryFn: () => fetchOrganizationReport(dateRange),
    enabled: !!dateRange.startDate && !!dateRange.endDate,
    staleTime: 60_000,
  });
}
