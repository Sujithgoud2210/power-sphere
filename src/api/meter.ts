import { apiClient } from './axios';
import type { SmartMeter, CreateMeterRequest, UpdateMeterRequest, AssignMeterRequest, MeterFilters } from '@/types';

interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  first: boolean;
  last: boolean;
}

export const meterApi = {
  getAll(filters: MeterFilters = {}): Promise<PageResponse<SmartMeter>> {
    const params: Record<string, string | number | boolean | undefined> = {
      page: filters.page ?? 0,
      size: filters.size ?? 10,
      sortBy: filters.sortBy ?? 'createdAt',
      sortDirection: filters.sortDirection ?? 'DESC',
    };
    if (filters.search) params.q = filters.search;
    if (filters.status) params.status = filters.status;
    if (filters.meterType) params.meterType = filters.meterType;
    if (filters.organizationId) params.organizationId = filters.organizationId;
    return apiClient.get('/api/v1/meters', { params }).then((res) => res.data.data);
  },

  getById(id: string): Promise<SmartMeter> {
    return apiClient.get(`/api/v1/meters/${id}`).then((res) => res.data.data);
  },

  create(data: CreateMeterRequest): Promise<SmartMeter> {
    return apiClient.post('/api/v1/meters', data).then((res) => res.data.data);
  },

  update(id: string, data: UpdateMeterRequest): Promise<SmartMeter> {
    return apiClient.put(`/api/v1/meters/${id}`, data).then((res) => res.data.data);
  },

  delete(id: string): Promise<void> {
    return apiClient.delete(`/api/v1/meters/${id}`).then(() => undefined);
  },

  activate(id: string): Promise<SmartMeter> {
    return apiClient.put(`/api/v1/meters/${id}/activate`).then((res) => res.data.data);
  },

  deactivate(id: string): Promise<SmartMeter> {
    return apiClient.put(`/api/v1/meters/${id}/deactivate`).then((res) => res.data.data);
  },

  assign(id: string, data: AssignMeterRequest): Promise<SmartMeter> {
    return apiClient.put(`/api/v1/meters/${id}/assign`, data).then((res) => res.data.data);
  },

  search(query: string, filters: MeterFilters = {}): Promise<PageResponse<SmartMeter>> {
    const params: Record<string, string | number | boolean | undefined> = {
      q: query,
      page: filters.page ?? 0,
      size: filters.size ?? 10,
      sortBy: filters.sortBy ?? 'createdAt',
      sortDirection: filters.sortDirection ?? 'DESC',
    };
    return apiClient.get('/api/v1/meters/search', { params }).then((res) => res.data.data);
  },

  getByMeterNumber(meterNumber: string): Promise<SmartMeter> {
    return apiClient.get(`/api/v1/meters/number/${meterNumber}`).then((res) => res.data.data);
  },
};
