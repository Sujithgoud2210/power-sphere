export type Priority = 'high' | 'medium' | 'low';

export type NotificationStatus = 'read' | 'unread';

export type BillStatus = 'pending' | 'paid' | 'overdue';

export type MeterStatus = 'active' | 'inactive' | 'maintenance';

export type ReportType = 'daily' | 'weekly' | 'monthly' | 'yearly';

export type DashboardType = 'executive' | 'operations' | 'energy' | 'revenue';

export interface ApiResponse<T> {
  data: T;
  message?: string;
  success: boolean;
  timestamp: string;
}

export interface PaginatedResponse<T> extends ApiResponse<T[]> {
  total: number;
  page: number;
  pageSize: number;
  totalPages: number;
}

export interface DateRange {
  startDate: string;
  endDate: string;
}

export interface SelectOption<T = string> {
  label: string;
  value: T;
}
