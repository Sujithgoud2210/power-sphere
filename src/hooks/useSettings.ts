import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { apiClient } from '../services/apiClient';
import type {
  ThemeSettings,
  NotificationPreferences,
  AccountSettings,
  SettingsData,
  ThemeMode,
  Language,
} from '../types/settings';

async function fetchSettings(): Promise<SettingsData> {
  return apiClient.get<SettingsData>('/settings');
}

async function updateThemeSettings(theme: ThemeSettings): Promise<ThemeSettings> {
  return apiClient.put<ThemeSettings>('/settings/theme', theme);
}

async function updateLanguage(language: Language): Promise<Language> {
  return apiClient.put<Language>('/settings/language', { language });
}

async function updateNotificationPreferences(
  preferences: NotificationPreferences
): Promise<NotificationPreferences> {
  return apiClient.put<NotificationPreferences>(
    '/settings/notifications',
    preferences
  );
}

async function updateAccountSettings(
  account: AccountSettings
): Promise<AccountSettings> {
  return apiClient.put<AccountSettings>('/settings/account', account);
}

export function useSettings() {
  return useQuery<SettingsData>({
    queryKey: ['settings'],
    queryFn: fetchSettings,
    staleTime: 60_000,
  });
}

export function useUpdateTheme() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: updateThemeSettings,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['settings'] });
    },
  });
}

export function useUpdateLanguage() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: updateLanguage,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['settings'] });
    },
  });
}

export function useUpdateNotificationPreferences() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: updateNotificationPreferences,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['settings'] });
    },
  });
}

export function useUpdateAccountSettings() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: updateAccountSettings,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['settings'] });
    },
  });
}
