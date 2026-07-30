import type { Priority, NotificationStatus } from './common';

export interface Notification {
  id: string;
  title: string;
  message: string;
  priority: Priority;
  status: NotificationStatus;
  category: string;
  createdAt: string;
  readAt?: string;
  actionUrl?: string;
}

export interface NotificationFilters {
  status?: NotificationStatus;
  priority?: Priority;
  category?: string;
  searchQuery?: string;
  startDate?: string;
  endDate?: string;
}

export interface NotificationSummary {
  total: number;
  unread: number;
  read: number;
  highPriority: number;
}
