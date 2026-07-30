export type UserRole = 'ADMIN' | 'MANAGER' | 'VIEWER' | 'USER';

export interface UserProfile {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  role: UserRole;
  avatarUrl?: string;
  phone?: string;
  departmentId?: number;
  organizationId?: number;
  isActive: boolean;
  lastLoginAt?: string;
  createdAt: string;
  updatedAt: string;
}

export interface UserState {
  profile: UserProfile | null;
  isLoading: boolean;
  error: string | null;
}
