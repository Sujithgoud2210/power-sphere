import { useMemo } from 'react';
import { useAppSelector } from './useAppStore';
import type { UserRole } from '@/types';

type RoleLevel = Record<UserRole, number>;

const ROLE_HIERARCHY: RoleLevel = {
  ADMIN: 100,
  MANAGER: 75,
  OPERATOR: 50,
  VIEWER: 25,
  USER: 25,
};

export function useRoleAccess() {
  const profile = useAppSelector((state) => state.user.profile);
  const userRole = profile?.role ?? 'VIEWER';

  const userLevel = useMemo(() => ROLE_HIERARCHY[userRole] ?? 0, [userRole]);

  const canAccess = useMemo(
    () => ({
      isAdmin: userRole === 'ADMIN',
      isManager: userRole === 'MANAGER' || userRole === 'ADMIN',
      isOperator: userRole === 'OPERATOR' || userRole === 'MANAGER' || userRole === 'ADMIN',
      isViewer: true,

      hasMinimumRole: (minimumRole: UserRole) => userLevel >= (ROLE_HIERARCHY[minimumRole] ?? 0),

      canManageUsers: userRole === 'ADMIN' || userRole === 'MANAGER',
      canManageOrganizations: userRole === 'ADMIN' || userRole === 'MANAGER',
      canManageMeters: userRole === 'ADMIN' || userRole === 'MANAGER' || userRole === 'OPERATOR',
      canManageBilling: userRole === 'ADMIN' || userRole === 'MANAGER',
      canManageEnergy: userRole === 'ADMIN' || userRole === 'MANAGER' || userRole === 'OPERATOR',
      canViewReports: true,
      canExportData: userRole !== 'VIEWER',
      canAccessSettings: userRole === 'ADMIN' || userRole === 'MANAGER',
    }),
    [userLevel, userRole],
  );

  return {
    userRole,
    userLevel,
    ...canAccess,
  };
}
