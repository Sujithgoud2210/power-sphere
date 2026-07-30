import { Routes, Route, Navigate } from 'react-router-dom';
import { ROUTES } from '@/constants';
import { ProtectedRoute, PublicRoute } from '@/components/common';
import { AppLayout } from '@/layouts';
import {
  AuthPage,
  DashboardPage,
  OrganizationListPage,
  OrganizationFormPage,
  OrganizationDetailsPage,
  UserListPage,
  UserFormPage,
  UserDetailsPage,
  MeterListPage,
  MeterFormPage,
  MeterDetailsPage,
  EnergyListPage,
  UploadReadingPage,
  EnergyDetailsPage,
  ConsumptionHistoryPage,
  BillListPage,
  BillDetailsPage,
  GenerateBillPage,
  NotificationsPage,
  ReportsPage,
  SettingsPage,
  ProfilePage,
  NotFoundPage,
} from '@/pages';

export function AppRouter() {
  return (
    <Routes>
      <Route element={<PublicRoute />}>
        <Route path={ROUTES.LOGIN} element={<AuthPage />} />
        <Route path={ROUTES.REGISTER} element={<AuthPage />} />
      </Route>
      <Route element={<ProtectedRoute />}>
        <Route element={<AppLayout />}>
          <Route path={ROUTES.DASHBOARD} element={<DashboardPage />} />
          <Route path={ROUTES.ORGANIZATIONS} element={<OrganizationListPage />} />
          <Route path={ROUTES.ORGANIZATIONS_CREATE} element={<OrganizationFormPage />} />
          <Route path={ROUTES.ORGANIZATIONS_EDIT} element={<OrganizationFormPage />} />
          <Route path={ROUTES.ORGANIZATIONS_DETAILS} element={<OrganizationDetailsPage />} />
          <Route path={ROUTES.USERS} element={<UserListPage />} />
          <Route path={ROUTES.USERS_CREATE} element={<UserFormPage />} />
          <Route path={ROUTES.USERS_EDIT} element={<UserFormPage />} />
          <Route path={ROUTES.USERS_DETAILS} element={<UserDetailsPage />} />
          <Route path={ROUTES.METERS} element={<MeterListPage />} />
          <Route path={ROUTES.METERS_CREATE} element={<MeterFormPage />} />
          <Route path={ROUTES.METERS_EDIT} element={<MeterFormPage />} />
          <Route path={ROUTES.METERS_DETAILS} element={<MeterDetailsPage />} />
          <Route path={ROUTES.ENERGY} element={<EnergyListPage />} />
          <Route path={ROUTES.ENERGY_UPLOAD} element={<UploadReadingPage />} />
          <Route path={ROUTES.ENERGY_DETAILS} element={<EnergyDetailsPage />} />
          <Route path={ROUTES.ENERGY_CONSUMPTION} element={<ConsumptionHistoryPage />} />
          <Route path={ROUTES.BILLING} element={<BillListPage />} />
          <Route path={ROUTES.BILLING_GENERATE} element={<GenerateBillPage />} />
          <Route path={ROUTES.BILLING_DETAILS} element={<BillDetailsPage />} />
          <Route path={ROUTES.NOTIFICATIONS} element={<NotificationsPage />} />
          <Route path={ROUTES.REPORTS} element={<ReportsPage />} />
          <Route path={ROUTES.SETTINGS} element={<SettingsPage />} />
          <Route path={ROUTES.PROFILE} element={<ProfilePage />} />
        </Route>
      </Route>
      <Route path="/" element={<Navigate to={ROUTES.DASHBOARD} replace />} />
      <Route path="*" element={<NotFoundPage />} />
    </Routes>
  );
}
