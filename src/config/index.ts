export const config = {
  api: {
    baseUrl: import.meta.env.VITE_API_BASE_URL || '/api',
    timeout: 30_000,
  },
  auth: {
    tokenKey: 'power_sphere_access_token',
    refreshTokenKey: 'power_sphere_refresh_token',
    rememberMeKey: 'power_sphere_remember_me',
    tokenType: 'Bearer',
    autoLogoutMinutes: 60,
  },
  app: {
    name: 'PowerSphere',
    version: '1.0.0',
    company: 'PowerSphere Inc.',
  },
} as const;
