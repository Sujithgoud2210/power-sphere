import { Navigate, Outlet } from 'react-router-dom';
import { useAppSelector } from '@/hooks';
import { ROUTES } from '@/constants';

export function PublicRoute() {
  const { isAuthenticated } = useAppSelector((state) => state.auth);

  if (isAuthenticated) {
    return <Navigate to={ROUTES.DASHBOARD} replace />;
  }

  return <Outlet />;
}
