import { apiClient } from '../apiClient';
import type {
  Notification,
  NotificationFilters,
  NotificationSummary,
} from '../../types/notification';

function buildFilters(
  filters?: NotificationFilters,
  page: number = 1,
  pageSize: number = 20
): Record<string, string> {
  const params: Record<string, string> = {
    page: page.toString(),
    pageSize: pageSize.toString(),
  };

  if (filters?.status) params.status = filters.status;
  if (filters?.priority) params.priority = filters.priority;
  if (filters?.category) params.category = filters.category;
  if (filters?.searchQuery) params.search = filters.searchQuery;
  if (filters?.startDate) params.startDate = filters.startDate;
  if (filters?.endDate) params.endDate = filters.endDate;

  return params;
}

export async function fetchNotifications(
  filters?: NotificationFilters,
  page: number = 1,
  pageSize: number = 20
): Promise<{ data: Notification[]; total: number }> {
  return apiClient.get<{ data: Notification[]; total: number }>(
    '/notifications',
    buildFilters(filters, page, pageSize)
  );
}

export async function fetchNotificationSummary(): Promise<NotificationSummary> {
  return apiClient.get<NotificationSummary>('/notifications/summary');
}

export async function markNotificationAsRead(
  notificationId: string
): Promise<Notification> {
  return apiClient.patch<Notification>(`/notifications/${notificationId}/read`);
}

export async function markAllNotificationsAsRead(): Promise<{ count: number }> {
  return apiClient.patch<{ count: number }>('/notifications/read-all');
}

export async function deleteNotification(
  notificationId: string
): Promise<void> {
  await apiClient.delete<void>(`/notifications/${notificationId}`);
}
