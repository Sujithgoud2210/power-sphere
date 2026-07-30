export type ThemeMode = 'light' | 'dark' | 'system';

export type Language = 'en' | 'fr' | 'es' | 'de' | 'zh';

export interface ThemeSettings {
  mode: ThemeMode;
  primaryColor?: string;
  fontSize?: 'small' | 'medium' | 'large';
  reducedMotion?: boolean;
}

export interface NotificationPreferences {
  emailNotifications: boolean;
  pushNotifications: boolean;
  smsNotifications: boolean;
  weeklyDigest: boolean;
  monthlyReport: boolean;
  billingAlerts: boolean;
  systemUpdates: boolean;
  marketingEmails: boolean;
}

export interface AccountSettings {
  twoFactorEnabled: boolean;
  sessionTimeout: number;
  defaultDashboard: 'executive' | 'operations' | 'energy' | 'revenue';
  timezone: string;
  dateFormat: 'MM/DD/YYYY' | 'DD/MM/YYYY' | 'YYYY-MM-DD';
  timeFormat: '12h' | '24h';
}

export interface SettingsData {
  theme: ThemeSettings;
  language: Language;
  notifications: NotificationPreferences;
  account: AccountSettings;
}
