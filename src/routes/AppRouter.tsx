import { Routes, Route, Navigate } from 'react-router-dom';
import { ROUTES } from '@/constants';
import { ProtectedRoute, PublicRoute } from '@/components/common';
import { AppLayout } from '@/layouts';
import {
  AuthPage,
  DashboardPage,
  OrganizationsPage,
  UsersPage,
  MetersPage,
  EnergyPage,
  BillingPage,
  NotificationsPage,
  ReportsPage,
  SettingsPage,
  ProfilePage,
  NotFoundPage,
} from '@/pages';

export function AppRouter() {
  return (
    <Routes>
      {/* Public Routes */}
      <Route element={<PublicRoute />}>
        <Route path={ROUTES.LOGIN} element={<AuthPage />} />
        <Route path={ROUTES.REGISTER} element={<AuthPage />} />
      </Route>

      {/* Protected Routes */}
      <Route element={<ProtectedRoute />}>
        <Route element={<AppLayout />}>
          <Route path={ROUTES.DASHBOARD} element={<DashboardPage />} />
          <Route path={ROUTES.ORGANIZATIONS} element={<OrganizationsPage />} />
          <Route path={ROUTES.USERS} element={<UsersPage />} />
          <Route path={ROUTES.METERS} element={<MetersPage />} />
          <Route path={ROUTES.ENERGY} element={<EnergyPage />} />
          <Route path={ROUTES.BILLING} element={<BillingPage />} />
          <Route path={ROUTES.NOTIFICATIONS} element={<NotificationsPage />} />
          <Route path={ROUTES.REPORTS} element={<ReportsPage />} />
          <Route path={ROUTES.SETTINGS} element={<SettingsPage />} />
          <Route path={ROUTES.PROFILE} element={<ProfilePage />} />
        </Route>
      </Route>

      {/* Redirects */}
      <Route path="/" element={<Navigate to={ROUTES.DASHBOARD} replace />} />

      {/* 404 */}
      <Route path="*" element={<NotFoundPage />} />
    </Routes>
  );
}
