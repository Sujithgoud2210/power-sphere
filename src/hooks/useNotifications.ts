import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import type { Notification, NotificationFilters, NotificationSummary } from '../types/notification';
import {
  fetchNotifications,
  fetchNotificationSummary,
  markNotificationAsRead,
  markAllNotificationsAsRead,
  deleteNotification,
} from '../services/notification/notificationService';

const NOTIFICATION_REFETCH_INTERVAL = 15_000; // 15 seconds

export function useNotifications(
  filters?: NotificationFilters,
  page: number = 1,
  pageSize: number = 20
) {
  return useQuery<{ data: Notification[]; total: number }>({
    queryKey: ['notifications', filters, page, pageSize],
    queryFn: () => fetchNotifications(filters, page, pageSize),
    refetchInterval: NOTIFICATION_REFETCH_INTERVAL,
    staleTime: 5_000,
  });
}

export function useNotificationSummary() {
  return useQuery<NotificationSummary>({
    queryKey: ['notifications', 'summary'],
    queryFn: fetchNotificationSummary,
    refetchInterval: NOTIFICATION_REFETCH_INTERVAL,
    staleTime: 5_000,
  });
}

export function useMarkAsRead() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: markNotificationAsRead,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['notifications'] });
    },
  });
}

export function useMarkAllAsRead() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: markAllNotificationsAsRead,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['notifications'] });
    },
  });
}

export function useDeleteNotification() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: deleteNotification,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['notifications'] });
    },
  });
}
