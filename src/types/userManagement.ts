import type { UserRole } from './user';

export interface ManagedUser {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  fullName: string;
  role: UserRole;
  phone?: string;
  avatarUrl?: string;
  organizationId?: number;
  organizationName?: string;
  isActive: boolean;
  emailVerified: boolean;
  lastLoginAt?: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreateManagedUserRequest {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  role: UserRole;
  phone?: string;
  organizationId?: number;
}

export interface UpdateManagedUserRequest {
  firstName?: string;
  lastName?: string;
  role?: UserRole;
  phone?: string;
  organizationId?: number;
  isActive?: boolean;
}

export interface UserFilters {
  search?: string;
  role?: UserRole;
  organizationId?: number;
  isActive?: boolean;
  page?: number;
  size?: number;
  sortBy?: string;
  sortDirection?: 'ASC' | 'DESC';
}
