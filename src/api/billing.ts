import { apiClient } from './axios';
import type { Bill, GenerateBillRequest, BillFilters } from '@/types';

interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  first: boolean;
  last: boolean;
}

export const billingApi = {
  getAll(filters: BillFilters = {}): Promise<PageResponse<Bill>> {
    const params: Record<string, string | number | boolean | undefined> = {
      page: filters.page ?? 0,
      size: filters.size ?? 20,
    };
    return apiClient.get('/api/v1/bills', { params }).then((res) => res.data.data);
  },

  getById(id: number): Promise<Bill> {
    return apiClient.get(`/api/v1/bills/${id}`).then((res) => res.data.data);
  },

  getByBillNumber(billNumber: string): Promise<Bill> {
    return apiClient.get(`/api/v1/bills/number/${billNumber}`).then((res) => res.data.data);
  },

  generate(data: GenerateBillRequest): Promise<Bill> {
    return apiClient.post('/api/v1/bills/generate', data).then((res) => res.data.data);
  },

  regenerate(id: number, data: GenerateBillRequest): Promise<Bill> {
    return apiClient.post(`/api/v1/bills/${id}/regenerate`, data).then((res) => res.data.data);
  },

  update(id: number, data: Partial<Bill>): Promise<Bill> {
    return apiClient.put(`/api/v1/bills/${id}`, data).then((res) => res.data.data);
  },

  delete(id: number): Promise<void> {
    return apiClient.delete(`/api/v1/bills/${id}`).then(() => undefined);
  },

  cancel(id: number, reason?: string): Promise<Bill> {
    const params = reason ? { reason } : {};
    return apiClient.post(`/api/v1/bills/${id}/cancel`, null, { params }).then((res) => res.data.data);
  },

  search(filters: BillFilters): Promise<PageResponse<Bill>> {
    const params: Record<string, string | number | boolean | undefined> = {
      page: filters.page ?? 0,
      size: filters.size ?? 20,
      sortBy: filters.sortBy ?? 'generatedDate',
      sortDirection: filters.sortDirection ?? 'DESC',
    };
    if (filters.meterId) params.meterId = filters.meterId;
    if (filters.organizationId) params.organizationId = filters.organizationId;
    if (filters.status) params.status = filters.status;
    if (filters.billingMonth) params.billingMonth = filters.billingMonth;
    if (filters.billingYear) params.billingYear = filters.billingYear;
    if (filters.query) params.query = filters.query;
    return apiClient.get('/api/v1/bills/search', { params }).then((res) => res.data.data);
  },
};
