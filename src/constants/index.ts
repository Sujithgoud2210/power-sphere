export const DRAWER_WIDTH = 260;

export const ROUTES = {
  LOGIN: '/login',
  REGISTER: '/register',
  DASHBOARD: '/dashboard',
  ORGANIZATIONS: '/organizations',
  USERS: '/users',
  METERS: '/meters',
  ENERGY: '/energy',
  BILLING: '/billing',
  NOTIFICATIONS: '/notifications',
  REPORTS: '/reports',
  SETTINGS: '/settings',
  PROFILE: '/profile',
} as const;

export const SIDEBAR_MENU_ITEMS = [
  { label: 'Dashboard', path: ROUTES.DASHBOARD, icon: 'Dashboard' },
  { label: 'Organizations', path: ROUTES.ORGANIZATIONS, icon: 'Business' },
  { label: 'Users', path: ROUTES.USERS, icon: 'People' },
  { label: 'Meters', path: ROUTES.METERS, icon: 'Speed' },
  { label: 'Energy', path: ROUTES.ENERGY, icon: 'Bolt' },
  { label: 'Billing', path: ROUTES.BILLING, icon: 'Receipt' },
  { label: 'Notifications', path: ROUTES.NOTIFICATIONS, icon: 'Notifications' },
  { label: 'Reports', path: ROUTES.REPORTS, icon: 'Assessment' },
] as const;

export const QUERY_KEYS = {
  USER_PROFILE: 'userProfile',
} as const;
