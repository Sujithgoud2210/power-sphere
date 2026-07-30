import { Navigate, Outlet } from 'react-router-dom';
import { Box } from '@mui/material';
import { useAppSelector } from '@/hooks';
import { ROUTES } from '@/constants';
import type { UserRole } from '@/types';

interface ProtectedRouteProps {
  allowedRoles?: UserRole[];
}

export function ProtectedRoute({ allowedRoles }: ProtectedRouteProps) {
  const { isAuthenticated } = useAppSelector((state) => state.auth);
  const userProfile = useAppSelector((state) => state.user.profile);

  if (!isAuthenticated) {
    return <Navigate to={ROUTES.LOGIN} replace />;
  }

  if (allowedRoles && userProfile && !allowedRoles.includes(userProfile.role)) {
    return <Navigate to={ROUTES.DASHBOARD} replace />;
  }

  return (
    <Box sx={{ display: 'flex', minHeight: '100vh' }}>
      <Outlet />
    </Box>
  );
}
