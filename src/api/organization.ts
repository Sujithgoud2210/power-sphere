import { apiClient } from './axios';
import type { Organization, CreateOrganizationRequest, UpdateOrganizationRequest, OrganizationFilters } from '@/types';

interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  first: boolean;
  last: boolean;
}

export const organizationApi = {
  getAll(filters: OrganizationFilters = {}): Promise<PageResponse<Organization>> {
    const params: Record<string, string | number | boolean | undefined> = {
      page: filters.page ?? 0,
      size: filters.size ?? 10,
      sortBy: filters.sortBy ?? 'createdAt',
      sortDirection: filters.sortDirection ?? 'DESC',
    };
    if (filters.search) params.q = filters.search;
    if (filters.isActive !== undefined) params.isActive = filters.isActive;
    return apiClient.get('/api/v1/organizations', { params }).then((res) => res.data.data);
  },

  getById(id: number): Promise<Organization> {
    return apiClient.get(`/api/v1/organizations/${id}`).then((res) => res.data.data);
  },

  create(data: CreateOrganizationRequest): Promise<Organization> {
    return apiClient.post('/api/v1/organizations', data).then((res) => res.data.data);
  },

  update(id: number, data: UpdateOrganizationRequest): Promise<Organization> {
    return apiClient.put(`/api/v1/organizations/${id}`, data).then((res) => res.data.data);
  },

  delete(id: number): Promise<void> {
    return apiClient.delete(`/api/v1/organizations/${id}`).then(() => undefined);
  },

  search(query: string, filters: OrganizationFilters = {}): Promise<PageResponse<Organization>> {
    const params: Record<string, string | number | boolean | undefined> = {
      q: query,
      page: filters.page ?? 0,
      size: filters.size ?? 10,
      sortBy: filters.sortBy ?? 'createdAt',
      sortDirection: filters.sortDirection ?? 'DESC',
    };
    return apiClient.get('/api/v1/organizations/search', { params }).then((res) => res.data.data);
  },
};
