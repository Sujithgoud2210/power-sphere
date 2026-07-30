export const DRAWER_WIDTH = 260;

export const ROUTES = {
  LOGIN: '/login',
  REGISTER: '/register',
  DASHBOARD: '/dashboard',
  // Organizations
  ORGANIZATIONS: '/organizations',
  ORGANIZATIONS_CREATE: '/organizations/create',
  ORGANIZATIONS_EDIT: '/organizations/:id/edit',
  ORGANIZATIONS_DETAILS: '/organizations/:id',
  // Users
  USERS: '/users',
  USERS_CREATE: '/users/create',
  USERS_EDIT: '/users/:id/edit',
  USERS_DETAILS: '/users/:id',
  // Meters
  METERS: '/meters',
  METERS_CREATE: '/meters/create',
  METERS_EDIT: '/meters/:id/edit',
  METERS_DETAILS: '/meters/:id',
  // Energy
  ENERGY: '/energy',
  ENERGY_UPLOAD: '/energy/upload',
  ENERGY_DETAILS: '/energy/:id',
  ENERGY_CONSUMPTION: '/energy/consumption/:meterId',
  // Billing
  BILLING: '/billing',
  BILLING_GENERATE: '/billing/generate',
  BILLING_DETAILS: '/billing/:id',
  // Others
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
  ORGANIZATIONS: 'organizations',
  USERS: 'users',
  METERS: 'meters',
  ENERGY_READINGS: 'energyReadings',
  BILLS: 'bills',
} as const;
