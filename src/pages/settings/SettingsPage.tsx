import React, { useState, useCallback } from 'react';
import { SettingsSection } from '../../components/settings/SettingsSection';
import type {
  ThemeMode,
  Language,
  NotificationPreferences,
  AccountSettings,
} from '../../types/settings';
import {
  useSettings,
  useUpdateTheme,
  useUpdateLanguage,
  useUpdateNotificationPreferences,
  useUpdateAccountSettings,
} from '../../hooks/useSettings';

const SettingsPage: React.FC = () => {
  const { data: settings, isLoading, error } = useSettings();
  const updateThemeMutation = useUpdateTheme();
  const updateLanguageMutation = useUpdateLanguage();
  const updateNotificationsMutation = useUpdateNotificationPreferences();
  const updateAccountMutation = useUpdateAccountSettings();

  // Local state for settings
  const [themeMode, setThemeMode] = useState<ThemeMode>('system');
  const [language, setLanguage] = useState<Language>('en');
  const [notifications, setNotifications] = useState<NotificationPreferences>({
    emailNotifications: true,
    pushNotifications: true,
    smsNotifications: false,
    weeklyDigest: true,
    monthlyReport: true,
    billingAlerts: true,
    systemUpdates: false,
    marketingEmails: false,
  });
  const [account, setAccount] = useState<AccountSettings>({
    twoFactorEnabled: false,
    sessionTimeout: 60,
    defaultDashboard: 'executive',
    timezone: 'UTC',
    dateFormat: 'MM/DD/YYYY',
    timeFormat: '12h',
  });

  const [successMessage, setSuccessMessage] = useState('');
  const [saving, setSaving] = useState<string | null>(null);

  // Sync from loaded settings
  React.useEffect(() => {
    if (settings) {
      setThemeMode(settings.theme.mode);
      setLanguage(settings.language);
      setNotifications(settings.notifications);
      setAccount(settings.account);
    }
  }, [settings]);

  const showSuccess = useCallback((message: string) => {
    setSuccessMessage(message);
    setTimeout(() => setSuccessMessage(''), 3000);
  }, []);

  const handleThemeChange = useCallback(
    async (mode: ThemeMode) => {
      setSaving('theme');
      setThemeMode(mode);
      try {
        await updateThemeMutation.mutateAsync({ mode });
        showSuccess('Theme updated successfully');
      } catch {
        // handled by mutation
      } finally {
        setSaving(null);
      }
    },
    [updateThemeMutation, showSuccess]
  );

  const handleLanguageChange = useCallback(
    async (lang: Language) => {
      setSaving('language');
      setLanguage(lang);
      try {
        await updateLanguageMutation.mutateAsync(lang);
        showSuccess('Language updated successfully');
      } catch {
        // handled by mutation
      } finally {
        setSaving(null);
      }
    },
    [updateLanguageMutation, showSuccess]
  );

  const handleNotificationToggle = useCallback(
    async (key: keyof NotificationPreferences) => {
      const updated = { ...notifications, [key]: !notifications[key] };
      setSaving('notifications');
      setNotifications(updated);
      try {
        await updateNotificationsMutation.mutateAsync(updated);
        showSuccess('Notification preferences updated');
      } catch {
        // Revert on failure
        setNotifications(notifications);
      } finally {
        setSaving(null);
      }
    },
    [notifications, updateNotificationsMutation, showSuccess]
  );

  const handleAccountChange = useCallback(
    async (key: keyof AccountSettings, value: AccountSettings[keyof AccountSettings]) => {
      const updated = { ...account, [key]: value };
      setSaving('account');
      setAccount(updated);
      try {
        await updateAccountMutation.mutateAsync(updated);
        showSuccess('Account settings updated');
      } catch {
        setAccount(account);
      } finally {
        setSaving(null);
      }
    },
    [account, updateAccountMutation, showSuccess]
  );

  // Icons
  const themeIcon = (
    <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M7 21a4 4 0 01-4-4V5a2 2 0 012-2h4a2 2 0 012 2v12a4 4 0 01-4 4zm0 0h12a2 2 0 002-2v-4a2 2 0 00-2-2h-2.343M11 7.343l1.657-1.657a2 2 0 012.828 0l2.829 2.829a2 2 0 010 2.828l-8.486 8.485M7 17h.01" />
    </svg>
  );

  const languageIcon = (
    <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3.055 11H5a2 2 0 012 2v1a2 2 0 002 2 2 2 0 012 2v2.945M8 3.935V5.5A2.5 2.5 0 0010.5 8h.5a2 2 0 012 2 2 2 0 104 0 2 2 0 012-2h1.064M15 20.488V18a2 2 0 012-2h3.064M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
    </svg>
  );

  const notificationIcon = (
    <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9" />
    </svg>
  );

  const accountIcon = (
    <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.066 2.573c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.573 1.066c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.066-2.573c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
    </svg>
  );

  const themeOptions: { value: ThemeMode; label: string; description: string; icon: React.ReactNode }[] = [
    {
      value: 'light',
      label: 'Light',
      description: 'Bright theme for daytime',
      icon: (
        <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 3v1m0 16v1m9-9h-1M4 12H3m15.364 6.364l-.707-.707M6.343 6.343l-.707-.707m12.728 0l-.707.707M6.343 17.657l-.707.707M16 12a4 4 0 11-8 0 4 4 0 018 0z" />
        </svg>
      ),
    },
    {
      value: 'dark',
      label: 'Dark',
      description: 'Dark theme for low-light environments',
      icon: (
        <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z" />
        </svg>
      ),
    },
    {
      value: 'system',
      label: 'System',
      description: 'Follow your device theme',
      icon: (
        <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9.75 17L9 20l-1 1h8l-1-1-.75-3M3 13h18M5 17h14a2 2 0 002-2V5a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
        </svg>
      ),
    },
  ];

  if (isLoading) {
    return (
      <div className="space-y-6">
        <div className="animate-pulse">
          <div className="h-8 w-48 rounded bg-gray-200 dark:bg-gray-700" />
          <div className="mt-2 h-4 w-72 rounded bg-gray-100 dark:bg-gray-700" />
        </div>
        <div className="space-y-4">
          {[1, 2, 3, 4].map((i) => (
            <div key={i} className="animate-pulse rounded-xl border border-gray-200 bg-white p-6 dark:border-gray-700 dark:bg-gray-800">
              <div className="h-5 w-40 rounded bg-gray-200 dark:bg-gray-700" />
              <div className="mt-4 space-y-3">
                <div className="h-10 w-full rounded bg-gray-100 dark:bg-gray-700" />
                <div className="h-10 w-full rounded bg-gray-100 dark:bg-gray-700" />
              </div>
            </div>
          ))}
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex flex-col items-center justify-center py-20">
        <svg className="mb-4 h-12 w-12 text-red-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M12 9v3.75m9-.75a9 9 0 11-18 0 9 9 0 0118 0zm-9 3.75h.008v.008H12v-.008z" />
        </svg>
        <p className="text-sm font-medium text-gray-900 dark:text-white">Failed to load settings</p>
        <p className="text-xs text-gray-500 dark:text-gray-400 mt-1">{(error as Error).message}</p>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-gray-900 dark:text-white">
          Settings
        </h1>
        <p className="mt-1 text-sm text-gray-500 dark:text-gray-400">
          Manage your application preferences and account settings
        </p>
      </div>

      {/* Success Toast */}
      {successMessage && (
        <div className="fixed bottom-4 right-4 z-50 animate-fade-in rounded-lg bg-green-600 px-4 py-3 text-sm font-medium text-white shadow-lg">
          <div className="flex items-center gap-2">
            <svg className="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
            </svg>
            {successMessage}
          </div>
        </div>
      )}

      <div className="rounded-xl border border-gray-200 bg-white p-6 shadow-sm dark:border-gray-700 dark:bg-gray-800">
        {/* Theme Settings */}
        <SettingsSection
          title="Theme"
          description="Customize the appearance of the application"
          icon={themeIcon}
        >
          <div className="grid grid-cols-1 gap-3 sm:grid-cols-3">
            {themeOptions.map((option) => (
              <button
                key={option.value}
                type="button"
                onClick={() => handleThemeChange(option.value)}
                disabled={saving === 'theme'}
                className={`relative flex items-center gap-3 rounded-lg border-2 p-3 transition-all focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 dark:focus:ring-offset-gray-800 ${
                  themeMode === option.value
                    ? 'border-blue-500 bg-blue-50 dark:border-blue-500 dark:bg-blue-900/20'
                    : 'border-gray-200 bg-white hover:bg-gray-50 dark:border-gray-600 dark:bg-gray-800 dark:hover:bg-gray-700'
                } ${saving === 'theme' ? 'cursor-not-allowed opacity-60' : 'cursor-pointer'}`}
              >
                <span
                  className={
                    themeMode === option.value
                      ? 'text-blue-600 dark:text-blue-400'
                      : 'text-gray-400 dark:text-gray-500'
                  }
                >
                  {option.icon}
                </span>
                <div className="text-left">
                  <span className="block text-sm font-medium text-gray-900 dark:text-white">
                    {option.label}
                  </span>
                  <span className="block text-xs text-gray-500 dark:text-gray-400">
                    {option.description}
                  </span>
                </div>
                {themeMode === option.value && (
                  <span className="absolute right-2 top-2">
                    <svg className="h-4 w-4 text-blue-600 dark:text-blue-400" fill="currentColor" viewBox="0 0 20 20">
                      <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clipRule="evenodd" />
                    </svg>
                  </span>
                )}
              </button>
            ))}
          </div>
        </SettingsSection>

        {/* Language Settings */}
        <SettingsSection
          title="Language"
          description="Select your preferred language (UI placeholder)"
          icon={languageIcon}
        >
          <select
            value={language}
            onChange={(e) => handleLanguageChange(e.target.value as Language)}
            disabled={saving === 'language'}
            className="w-full max-w-xs rounded-lg border border-gray-200 bg-white px-3 py-2 text-sm text-gray-700 transition-colors focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-500/20 disabled:cursor-not-allowed disabled:opacity-50 dark:border-gray-600 dark:bg-gray-800 dark:text-gray-300"
            aria-label="Select language"
          >
            <option value="en">English</option>
            <option value="fr">Français</option>
            <option value="es">Español</option>
            <option value="de">Deutsch</option>
            <option value="zh">中文</option>
          </select>
        </SettingsSection>

        {/* Notification Preferences */}
        <SettingsSection
          title="Notification Preferences"
          description="Control which notifications you receive"
          icon={notificationIcon}
        >
          <div className="space-y-3">
            {[
              { key: 'emailNotifications' as const, label: 'Email Notifications', description: 'Receive notifications via email' },
              { key: 'pushNotifications' as const, label: 'Push Notifications', description: 'Receive push notifications in browser' },
              { key: 'smsNotifications' as const, label: 'SMS Notifications', description: 'Receive text message alerts' },
              { key: 'weeklyDigest' as const, label: 'Weekly Digest', description: 'Weekly summary of activities' },
              { key: 'monthlyReport' as const, label: 'Monthly Report', description: 'Monthly performance report' },
              { key: 'billingAlerts' as const, label: 'Billing Alerts', description: 'Alerts for billing events and due dates' },
              { key: 'systemUpdates' as const, label: 'System Updates', description: 'System maintenance and update notices' },
              { key: 'marketingEmails' as const, label: 'Marketing Emails', description: 'Product updates and promotional content' },
            ].map(({ key, label, description }) => (
              <label
                key={key}
                className={`flex items-center justify-between rounded-lg border border-gray-100 p-3 transition-all hover:bg-gray-50 dark:border-gray-700 dark:hover:bg-gray-700/50 ${saving === 'notifications' ? 'cursor-not-allowed opacity-60' : 'cursor-pointer'}`}
              >
                <div>
                  <span className="text-sm font-medium text-gray-900 dark:text-white">
                    {label}
                  </span>
                  <p className="text-xs text-gray-500 dark:text-gray-400">{description}</p>
                </div>
                <button
                  type="button"
                  role="switch"
                  aria-checked={notifications[key]}
                  onClick={() => handleNotificationToggle(key)}
                  disabled={saving === 'notifications'}
                  className={`relative inline-flex h-5 w-9 flex-shrink-0 cursor-pointer rounded-full border-2 border-transparent transition-colors duration-200 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 disabled:cursor-not-allowed dark:focus:ring-offset-gray-800 ${
                    notifications[key] ? 'bg-blue-600' : 'bg-gray-200 dark:bg-gray-600'
                  }`}
                >
                  <span
                    className={`inline-block h-4 w-4 transform rounded-full bg-white shadow-sm ring-0 transition-transform duration-200 ${
                      notifications[key] ? 'translate-x-4' : 'translate-x-0'
                    }`}
                  />
                </button>
              </label>
            ))}
          </div>
        </SettingsSection>

        {/* Account Settings */}
        <SettingsSection
          title="Account Settings"
          description="Manage your account security and preferences"
          icon={accountIcon}
          divider={false}
        >
          <div className="space-y-4">
            {/* Two-Factor Authentication */}
            <div className="flex items-center justify-between rounded-lg border border-gray-100 p-3 dark:border-gray-700">
              <div>
                <span className="text-sm font-medium text-gray-900 dark:text-white">
                  Two-Factor Authentication
                </span>
                <p className="text-xs text-gray-500 dark:text-gray-400">
                  Add an extra layer of security to your account
                </p>
              </div>
              <button
                type="button"
                role="switch"
                aria-checked={account.twoFactorEnabled}
                onClick={() => handleAccountChange('twoFactorEnabled', !account.twoFactorEnabled)}
                disabled={saving === 'account'}
                className={`relative inline-flex h-5 w-9 flex-shrink-0 cursor-pointer rounded-full border-2 border-transparent transition-colors duration-200 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 disabled:cursor-not-allowed dark:focus:ring-offset-gray-800 ${
                  account.twoFactorEnabled ? 'bg-blue-600' : 'bg-gray-200 dark:bg-gray-600'
                }`}
              >
                <span
                  className={`inline-block h-4 w-4 transform rounded-full bg-white shadow-sm ring-0 transition-transform duration-200 ${
                    account.twoFactorEnabled ? 'translate-x-4' : 'translate-x-0'
                  }`}
                />
              </button>
            </div>

            {/* Session Timeout */}
            <div className="flex items-center justify-between rounded-lg border border-gray-100 p-3 dark:border-gray-700">
              <div>
                <span className="text-sm font-medium text-gray-900 dark:text-white">
                  Session Timeout
                </span>
                <p className="text-xs text-gray-500 dark:text-gray-400">
                  Automatically log out after inactivity
                </p>
              </div>
              <select
                value={account.sessionTimeout}
                onChange={(e) => handleAccountChange('sessionTimeout', Number(e.target.value))}
                disabled={saving === 'account'}
                className="rounded-lg border border-gray-200 bg-white px-3 py-1.5 text-xs text-gray-700 focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-500/20 disabled:cursor-not-allowed disabled:opacity-50 dark:border-gray-600 dark:bg-gray-800 dark:text-gray-300"
              >
                <option value={15}>15 minutes</option>
                <option value={30}>30 minutes</option>
                <option value={60}>1 hour</option>
                <option value={120}>2 hours</option>
                <option value={240}>4 hours</option>
              </select>
            </div>

            {/* Default Dashboard */}
            <div className="flex items-center justify-between rounded-lg border border-gray-100 p-3 dark:border-gray-700">
              <div>
                <span className="text-sm font-medium text-gray-900 dark:text-white">
                  Default Dashboard
                </span>
                <p className="text-xs text-gray-500 dark:text-gray-400">
                  Choose which dashboard to show on login
                </p>
              </div>
              <select
                value={account.defaultDashboard}
                onChange={(e) => handleAccountChange('defaultDashboard', e.target.value as AccountSettings['defaultDashboard'])}
                disabled={saving === 'account'}
                className="rounded-lg border border-gray-200 bg-white px-3 py-1.5 text-xs text-gray-700 focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-500/20 disabled:cursor-not-allowed disabled:opacity-50 dark:border-gray-600 dark:bg-gray-800 dark:text-gray-300"
              >
                <option value="executive">Executive</option>
                <option value="operations">Operations</option>
                <option value="energy">Energy</option>
                <option value="revenue">Revenue</option>
              </select>
            </div>

            {/* Timezone */}
            <div className="flex items-center justify-between rounded-lg border border-gray-100 p-3 dark:border-gray-700">
              <div>
                <span className="text-sm font-medium text-gray-900 dark:text-white">
                  Timezone
                </span>
                <p className="text-xs text-gray-500 dark:text-gray-400">
                  Your local timezone
                </p>
              </div>
              <select
                value={account.timezone}
                onChange={(e) => handleAccountChange('timezone', e.target.value)}
                disabled={saving === 'account'}
                className="rounded-lg border border-gray-200 bg-white px-3 py-1.5 text-xs text-gray-700 focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-500/20 disabled:cursor-not-allowed disabled:opacity-50 dark:border-gray-600 dark:bg-gray-800 dark:text-gray-300"
              >
                <option value="UTC">UTC</option>
                <option value="US/Eastern">US/Eastern</option>
                <option value="US/Central">US/Central</option>
                <option value="US/Mountain">US/Mountain</option>
                <option value="US/Pacific">US/Pacific</option>
                <option value="Europe/London">Europe/London</option>
                <option value="Europe/Paris">Europe/Paris</option>
                <option value="Asia/Tokyo">Asia/Tokyo</option>
                <option value="Asia/Shanghai">Asia/Shanghai</option>
              </select>
            </div>

            {/* Date Format */}
            <div className="flex items-center justify-between rounded-lg border border-gray-100 p-3 dark:border-gray-700">
              <div>
                <span className="text-sm font-medium text-gray-900 dark:text-white">
                  Date Format
                </span>
                <p className="text-xs text-gray-500 dark:text-gray-400">
                  How dates are displayed
                </p>
              </div>
              <select
                value={account.dateFormat}
                onChange={(e) => handleAccountChange('dateFormat', e.target.value as AccountSettings['dateFormat'])}
                disabled={saving === 'account'}
                className="rounded-lg border border-gray-200 bg-white px-3 py-1.5 text-xs text-gray-700 focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-500/20 disabled:cursor-not-allowed disabled:opacity-50 dark:border-gray-600 dark:bg-gray-800 dark:text-gray-300"
              >
                <option value="MM/DD/YYYY">MM/DD/YYYY</option>
                <option value="DD/MM/YYYY">DD/MM/YYYY</option>
                <option value="YYYY-MM-DD">YYYY-MM-DD</option>
              </select>
            </div>

            {/* Time Format */}
            <div className="flex items-center justify-between rounded-lg border border-gray-100 p-3 dark:border-gray-700">
              <div>
                <span className="text-sm font-medium text-gray-900 dark:text-white">
                  Time Format
                </span>
                <p className="text-xs text-gray-500 dark:text-gray-400">
                  12-hour or 24-hour clock
                </p>
              </div>
              <select
                value={account.timeFormat}
                onChange={(e) => handleAccountChange('timeFormat', e.target.value as AccountSettings['timeFormat'])}
                disabled={saving === 'account'}
                className="rounded-lg border border-gray-200 bg-white px-3 py-1.5 text-xs text-gray-700 focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-500/20 disabled:cursor-not-allowed disabled:opacity-50 dark:border-gray-600 dark:bg-gray-800 dark:text-gray-300"
              >
                <option value="12h">12-hour (AM/PM)</option>
                <option value="24h">24-hour</option>
              </select>
            </div>
          </div>
        </SettingsSection>
      </div>
    </div>
  );
};

export default SettingsPage;
