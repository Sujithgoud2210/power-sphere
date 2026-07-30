import { lazy, Suspense, type ReactNode } from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { ROUTES } from '@/constants';
import { ProtectedRoute, PublicRoute, Loading } from '@/components/common';
import { AppLayout } from '@/layouts';

// Lazy-loaded pages for code splitting
const AuthPage = lazy(() => import('@/pages').then((m) => ({ default: m.AuthPage })));
const DashboardPage = lazy(() => import('@/pages').then((m) => ({ default: m.DashboardPage })));
const OrganizationListPage = lazy(() => import('@/pages').then((m) => ({ default: m.OrganizationListPage })));
const OrganizationFormPage = lazy(() => import('@/pages').then((m) => ({ default: m.OrganizationFormPage })));
const OrganizationDetailsPage = lazy(() => import('@/pages').then((m) => ({ default: m.OrganizationDetailsPage })));
const UserListPage = lazy(() => import('@/pages').then((m) => ({ default: m.UserListPage })));
const UserFormPage = lazy(() => import('@/pages').then((m) => ({ default: m.UserFormPage })));
const UserDetailsPage = lazy(() => import('@/pages').then((m) => ({ default: m.UserDetailsPage })));
const MeterListPage = lazy(() => import('@/pages').then((m) => ({ default: m.MeterListPage })));
const MeterFormPage = lazy(() => import('@/pages').then((m) => ({ default: m.MeterFormPage })));
const MeterDetailsPage = lazy(() => import('@/pages').then((m) => ({ default: m.MeterDetailsPage })));
const EnergyListPage = lazy(() => import('@/pages').then((m) => ({ default: m.EnergyListPage })));
const UploadReadingPage = lazy(() => import('@/pages').then((m) => ({ default: m.UploadReadingPage })));
const EnergyDetailsPage = lazy(() => import('@/pages').then((m) => ({ default: m.EnergyDetailsPage })));
const ConsumptionHistoryPage = lazy(() => import('@/pages').then((m) => ({ default: m.ConsumptionHistoryPage })));
const BillListPage = lazy(() => import('@/pages').then((m) => ({ default: m.BillListPage })));
const BillDetailsPage = lazy(() => import('@/pages').then((m) => ({ default: m.BillDetailsPage })));
const GenerateBillPage = lazy(() => import('@/pages').then((m) => ({ default: m.GenerateBillPage })));
const NotificationsPage = lazy(() => import('@/pages').then((m) => ({ default: m.NotificationsPage })));
const ReportsPage = lazy(() => import('@/pages').then((m) => ({ default: m.ReportsPage })));
const SettingsPage = lazy(() => import('@/pages').then((m) => ({ default: m.SettingsPage })));
const ProfilePage = lazy(() => import('@/pages').then((m) => ({ default: m.ProfilePage })));
const NotFoundPage = lazy(() => import('@/pages').then((m) => ({ default: m.NotFoundPage })));

function LazyPage({ children }: { children: ReactNode }) {
  return <Suspense fallback={<Loading message="Loading page..." />}>{children}</Suspense>;
}

export function AppRouter() {
  return (
    <Routes>
      <Route element={<PublicRoute />}>
        <Route
          path={ROUTES.LOGIN}
          element={
            <LazyPage>
              <AuthPage />
            </LazyPage>
          }
        />
        <Route
          path={ROUTES.REGISTER}
          element={
            <LazyPage>
              <AuthPage />
            </LazyPage>
          }
        />
      </Route>
      <Route element={<ProtectedRoute />}>
        <Route element={<AppLayout />}>
          <Route
            path={ROUTES.DASHBOARD}
            element={
              <LazyPage>
                <DashboardPage />
              </LazyPage>
            }
          />
          <Route
            path={ROUTES.ORGANIZATIONS}
            element={
              <LazyPage>
                <OrganizationListPage />
              </LazyPage>
            }
          />
          <Route
            path={ROUTES.ORGANIZATIONS_CREATE}
            element={
              <LazyPage>
                <OrganizationFormPage />
              </LazyPage>
            }
          />
          <Route
            path={ROUTES.ORGANIZATIONS_EDIT}
            element={
              <LazyPage>
                <OrganizationFormPage />
              </LazyPage>
            }
          />
          <Route
            path={ROUTES.ORGANIZATIONS_DETAILS}
            element={
              <LazyPage>
                <OrganizationDetailsPage />
              </LazyPage>
            }
          />
          <Route
            path={ROUTES.USERS}
            element={
              <LazyPage>
                <UserListPage />
              </LazyPage>
            }
          />
          <Route
            path={ROUTES.USERS_CREATE}
            element={
              <LazyPage>
                <UserFormPage />
              </LazyPage>
            }
          />
          <Route
            path={ROUTES.USERS_EDIT}
            element={
              <LazyPage>
                <UserFormPage />
              </LazyPage>
            }
          />
          <Route
            path={ROUTES.USERS_DETAILS}
            element={
              <LazyPage>
                <UserDetailsPage />
              </LazyPage>
            }
          />
          <Route
            path={ROUTES.METERS}
            element={
              <LazyPage>
                <MeterListPage />
              </LazyPage>
            }
          />
          <Route
            path={ROUTES.METERS_CREATE}
            element={
              <LazyPage>
                <MeterFormPage />
              </LazyPage>
            }
          />
          <Route
            path={ROUTES.METERS_EDIT}
            element={
              <LazyPage>
                <MeterFormPage />
              </LazyPage>
            }
          />
          <Route
            path={ROUTES.METERS_DETAILS}
            element={
              <LazyPage>
                <MeterDetailsPage />
              </LazyPage>
            }
          />
          <Route
            path={ROUTES.ENERGY}
            element={
              <LazyPage>
                <EnergyListPage />
              </LazyPage>
            }
          />
          <Route
            path={ROUTES.ENERGY_UPLOAD}
            element={
              <LazyPage>
                <UploadReadingPage />
              </LazyPage>
            }
          />
          <Route
            path={ROUTES.ENERGY_DETAILS}
            element={
              <LazyPage>
                <EnergyDetailsPage />
              </LazyPage>
            }
          />
          <Route
            path={ROUTES.ENERGY_CONSUMPTION}
            element={
              <LazyPage>
                <ConsumptionHistoryPage />
              </LazyPage>
            }
          />
          <Route
            path={ROUTES.BILLING}
            element={
              <LazyPage>
                <BillListPage />
              </LazyPage>
            }
          />
          <Route
            path={ROUTES.BILLING_GENERATE}
            element={
              <LazyPage>
                <GenerateBillPage />
              </LazyPage>
            }
          />
          <Route
            path={ROUTES.BILLING_DETAILS}
            element={
              <LazyPage>
                <BillDetailsPage />
              </LazyPage>
            }
          />
          <Route
            path={ROUTES.NOTIFICATIONS}
            element={
              <LazyPage>
                <NotificationsPage />
              </LazyPage>
            }
          />
          <Route
            path={ROUTES.REPORTS}
            element={
              <LazyPage>
                <ReportsPage />
              </LazyPage>
            }
          />
          <Route
            path={ROUTES.SETTINGS}
            element={
              <LazyPage>
                <SettingsPage />
              </LazyPage>
            }
          />
          <Route
            path={ROUTES.PROFILE}
            element={
              <LazyPage>
                <ProfilePage />
              </LazyPage>
            }
          />
        </Route>
      </Route>
      <Route path="/" element={<Navigate to={ROUTES.DASHBOARD} replace />} />
      <Route
        path="*"
        element={
          <LazyPage>
            <NotFoundPage />
          </LazyPage>
        }
      />
    </Routes>
  );
}
