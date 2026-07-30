import { apiClient } from './axios';
import type { EnergyReading, CreateEnergyReadingRequest, UpdateEnergyReadingRequest, ConsumptionResponse, EnergyReadingFilters } from '@/types';

interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  first: boolean;
  last: boolean;
}

export const energyApi = {
  getAll(filters: EnergyReadingFilters = {}): Promise<PageResponse<EnergyReading>> {
    const params: Record<string, string | number | boolean | undefined> = {
      page: filters.page ?? 0,
      size: filters.size ?? 10,
    };
    if (filters.meterId) params.meterId = filters.meterId;
    if (filters.startDate) params.startDate = filters.startDate;
    if (filters.endDate) params.endDate = filters.endDate;
    return apiClient.get('/api/v1/energy-readings', { params }).then((res) => res.data.data);
  },

  getById(id: number): Promise<EnergyReading> {
    return apiClient.get(`/api/v1/energy-readings/${id}`).then((res) => res.data.data);
  },

  create(data: CreateEnergyReadingRequest): Promise<EnergyReading> {
    return apiClient.post('/api/v1/energy-readings', data).then((res) => res.data.data);
  },

  update(id: number, data: UpdateEnergyReadingRequest): Promise<EnergyReading> {
    return apiClient.put(`/api/v1/energy-readings/${id}`, data).then((res) => res.data.data);
  },

  delete(id: number): Promise<void> {
    return apiClient.delete(`/api/v1/energy-readings/${id}`).then(() => undefined);
  },

  getLatestByMeter(meterId: number): Promise<EnergyReading> {
    return apiClient.get(`/api/v1/energy-readings/latest/${meterId}`).then((res) => res.data.data);
  },

  getHistoryByMeter(meterId: number): Promise<EnergyReading[]> {
    return apiClient.get(`/api/v1/energy-readings/history/${meterId}`).then((res) => res.data.data);
  },

  search(filters: EnergyReadingFilters): Promise<PageResponse<EnergyReading>> {
    const params: Record<string, string | number | boolean | undefined> = {
      page: filters.page ?? 0,
      size: filters.size ?? 10,
      sortBy: filters.sortBy ?? 'readingTimestamp',
      sortDirection: filters.sortDirection ?? 'DESC',
    };
    if (filters.meterId) params.meterId = filters.meterId;
    if (filters.readingType) params.readingType = filters.readingType;
    if (filters.qualityStatus) params.qualityStatus = filters.qualityStatus;
    if (filters.startDate) params.startDate = filters.startDate;
    if (filters.endDate) params.endDate = filters.endDate;
    if (filters.searchKeyword) params.searchKeyword = filters.searchKeyword;
    return apiClient.get('/api/v1/energy-readings/search', { params }).then((res) => res.data.data);
  },

  getConsumption(meterId: number): Promise<ConsumptionResponse> {
    return apiClient.get(`/api/v1/energy-readings/consumption/${meterId}`).then((res) => res.data.data);
  },

  getConsumptionBetween(meterId: number, startDate: string, endDate: string): Promise<ConsumptionResponse> {
    return apiClient.get(`/api/v1/energy-readings/consumption-range/${meterId}`, {
      params: { startDate, endDate },
    }).then((res) => res.data.data);
  },

  getConsumptionHistory(meterId: number): Promise<ConsumptionResponse[]> {
    return apiClient.get(`/api/v1/energy-readings/consumption-history/${meterId}`).then((res) => res.data.data);
  },
};
