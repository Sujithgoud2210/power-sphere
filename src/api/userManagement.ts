import { apiClient } from './axios';
import type { ManagedUser, CreateManagedUserRequest, UpdateManagedUserRequest, UserFilters } from '@/types';

interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  first: boolean;
  last: boolean;
}

export const userManagementApi = {
  getAll(filters: UserFilters = {}): Promise<PageResponse<ManagedUser>> {
    const params: Record<string, string | number | boolean | undefined> = {
      page: filters.page ?? 0,
      size: filters.size ?? 10,
      sortBy: filters.sortBy ?? 'createdAt',
      sortDirection: filters.sortDirection ?? 'DESC',
    };
    if (filters.search) params.q = filters.search;
    if (filters.role) params.role = filters.role;
    if (filters.organizationId) params.organizationId = filters.organizationId;
    if (filters.isActive !== undefined) params.isActive = filters.isActive;
    return apiClient.get('/api/v1/users', { params }).then((res) => res.data.data);
  },

  getById(id: number): Promise<ManagedUser> {
    return apiClient.get(`/api/v1/users/${id}`).then((res) => res.data.data);
  },

  create(data: CreateManagedUserRequest): Promise<ManagedUser> {
    return apiClient.post('/api/v1/users', data).then((res) => res.data.data);
  },

  update(id: number, data: UpdateManagedUserRequest): Promise<ManagedUser> {
    return apiClient.put(`/api/v1/users/${id}`, data).then((res) => res.data.data);
  },

  delete(id: number): Promise<void> {
    return apiClient.delete(`/api/v1/users/${id}`).then(() => undefined);
  },

  assignOrganization(userId: number, organizationId: number): Promise<ManagedUser> {
    return apiClient.put(`/api/v1/users/${userId}/organization`, { organizationId }).then((res) => res.data.data);
  },

  assignRole(userId: number, role: string): Promise<ManagedUser> {
    return apiClient.put(`/api/v1/users/${userId}/role`, { role }).then((res) => res.data.data);
  },

  search(query: string, filters: UserFilters = {}): Promise<PageResponse<ManagedUser>> {
    const params: Record<string, string | number | boolean | undefined> = {
      q: query,
      page: filters.page ?? 0,
      size: filters.size ?? 10,
    };
    return apiClient.get('/api/v1/users/search', { params }).then((res) => res.data.data);
  },
};
