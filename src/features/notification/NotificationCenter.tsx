import React, { useState, useCallback, useMemo } from 'react';
import type { NotificationFilters, Notification as NotificationType, NotificationSummary } from '../../types/notification';
import { NotificationItem } from '../../components/notification/NotificationItem';
import {
  useNotifications,
  useNotificationSummary,
  useMarkAsRead,
  useMarkAllAsRead,
  useDeleteNotification,
} from '../../hooks/useNotifications';

type FilterTab = 'all' | 'unread' | 'high' | 'medium' | 'low';

export const NotificationCenter: React.FC = () => {
  const [currentPage, setCurrentPage] = useState(1);
  const [activeTab, setActiveTab] = useState<FilterTab>('all');
  const [searchQuery, setSearchQuery] = useState('');

  const filters: NotificationFilters = useMemo(() => {
    const f: NotificationFilters = {};
    if (activeTab === 'unread') f.status = 'unread';
    if (['high', 'medium', 'low'].includes(activeTab)) {
      f.priority = activeTab as NotificationFilters['priority'];
    }
    if (searchQuery.trim()) f.searchQuery = searchQuery.trim();
    return f;
  }, [activeTab, searchQuery]);

  const {
    data: notificationsData,
    isLoading,
    error,
    refetch,
  } = useNotifications(filters, currentPage, 20);

  const { data: summary } = useNotificationSummary();
  const markAsReadMutation = useMarkAsRead();
  const markAllAsReadMutation = useMarkAllAsRead();
  const deleteMutation = useDeleteNotification();

  const notifications = notificationsData?.data ?? [];
  const totalCount = notificationsData?.total ?? 0;
  const totalPages = Math.max(1, Math.ceil(totalCount / 20));

  const handleMarkAsRead = useCallback(
    (id: string) => {
      markAsReadMutation.mutate(id);
    },
    [markAsReadMutation]
  );

  const handleMarkAllAsRead = useCallback(() => {
    markAllAsReadMutation.mutate();
  }, [markAllAsReadMutation]);

  const handleDelete = useCallback(
    (id: string) => {
      deleteMutation.mutate(id);
    },
    [deleteMutation]
  );

  const handleSearchChange = useCallback(
    (e: React.ChangeEvent<HTMLInputElement>) => {
      setSearchQuery(e.target.value);
      setCurrentPage(1);
    },
    []
  );

  const handleTabChange = useCallback((tab: FilterTab) => {
    setActiveTab(tab);
    setCurrentPage(1);
  }, []);

  const tabs: { key: FilterTab; label: string; count?: number }[] = useMemo(
    () => [
      { key: 'all', label: 'All', count: summary?.total },
      { key: 'unread', label: 'Unread', count: summary?.unread },
      { key: 'high', label: 'High Priority', count: summary?.highPriority },
      { key: 'medium', label: 'Medium' },
      { key: 'low', label: 'Low' },
    ],
    [summary]
  );

  return (
    <div className="space-y-4">
      {/* Header */}
      <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900 dark:text-white">
            Notification Center
          </h1>
          <p className="mt-1 text-sm text-gray-500 dark:text-gray-400">
            {summary?.unread ?? 0} unread notifications
          </p>
        </div>

        <div className="flex items-center gap-2">
          <button
            type="button"
            onClick={handleMarkAllAsRead}
            disabled={!summary?.unread}
            className="inline-flex items-center gap-1.5 rounded-lg border border-gray-200 bg-white px-3 py-2 text-xs font-medium text-gray-700 transition-all hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50 dark:border-gray-600 dark:bg-gray-700 dark:text-gray-300 dark:hover:bg-gray-600 dark:focus:ring-offset-gray-800"
          >
            <svg className="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 19l9-14 9 14" />
            </svg>
            Mark All Read
          </button>
        </div>
      </div>

      {/* Search and Filters */}
      <div className="flex flex-col gap-3 sm:flex-row sm:items-center">
        {/* Search */}
        <div className="relative flex-1">
          <svg
            className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-gray-400"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
          </svg>
          <input
            type="text"
            value={searchQuery}
            onChange={handleSearchChange}
            placeholder="Search notifications..."
            className="w-full rounded-lg border border-gray-200 bg-white py-2 pl-10 pr-4 text-sm text-gray-900 placeholder-gray-400 transition-colors focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-500/20 dark:border-gray-600 dark:bg-gray-800 dark:text-white dark:placeholder-gray-500"
            aria-label="Search notifications"
          />
        </div>
      </div>

      {/* Filter Tabs */}
      <div className="flex flex-wrap gap-2">
        {tabs.map((tab) => (
          <button
            key={tab.key}
            type="button"
            onClick={() => handleTabChange(tab.key)}
            className={`inline-flex items-center gap-1.5 rounded-lg px-3 py-1.5 text-xs font-medium transition-all focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-1 ${
              activeTab === tab.key
                ? 'bg-blue-600 text-white shadow-sm'
                : 'bg-gray-100 text-gray-600 hover:bg-gray-200 dark:bg-gray-700 dark:text-gray-400 dark:hover:bg-gray-600'
            }`}
          >
            {tab.label}
            {tab.count !== undefined && tab.count > 0 && (
              <span
                className={`inline-flex items-center justify-center rounded-full px-1.5 py-0.5 text-[10px] font-medium ${
                  activeTab === tab.key
                    ? 'bg-white/20 text-white'
                    : 'bg-gray-200 text-gray-600 dark:bg-gray-600 dark:text-gray-400'
                }`}
              >
                {tab.count}
              </span>
            )}
          </button>
        ))}
      </div>

      {/* Error State */}
      {error && (
        <div
          className="rounded-lg border border-red-200 bg-red-50 p-4 text-center dark:border-red-800 dark:bg-red-900/20"
          role="alert"
        >
          <p className="text-sm font-medium text-red-700 dark:text-red-400">
            Failed to load notifications
          </p>
          <p className="mt-1 text-xs text-red-500 dark:text-red-400">
            {(error as Error).message}
          </p>
          <button
            type="button"
            onClick={() => refetch()}
            className="mt-2 rounded-lg bg-red-600 px-3 py-1 text-xs font-medium text-white hover:bg-red-700"
          >
            Retry
          </button>
        </div>
      )}

      {/* Loading State */}
      {isLoading && (
        <div className="space-y-3">
          {[1, 2, 3, 4, 5].map((i) => (
            <div
              key={i}
              className="animate-pulse rounded-lg border border-gray-100 bg-white p-4 dark:border-gray-700 dark:bg-gray-800"
            >
              <div className="flex items-start gap-3">
                <div className="h-2 w-2 rounded-full bg-gray-200 dark:bg-gray-700" />
                <div className="flex-1 space-y-2">
                  <div className="h-4 w-3/4 rounded bg-gray-200 dark:bg-gray-700" />
                  <div className="h-3 w-1/2 rounded bg-gray-100 dark:bg-gray-700" />
                </div>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Empty State */}
      {!isLoading && !error && notifications.length === 0 && (
        <div className="flex flex-col items-center justify-center rounded-xl border border-dashed border-gray-300 bg-white py-16 dark:border-gray-600 dark:bg-gray-800">
          <svg
            className="mb-4 h-12 w-12 text-gray-300 dark:text-gray-600"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={1.5}
              d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9"
            />
          </svg>
          <h3 className="text-sm font-medium text-gray-900 dark:text-white">
            No notifications found
          </h3>
          <p className="mt-1 text-xs text-gray-500 dark:text-gray-400">
            {searchQuery
              ? 'Try adjusting your search or filter criteria'
              : 'You\'re all caught up!'}
          </p>
        </div>
      )}

      {/* Notification List */}
      {!isLoading && notifications.length > 0 && (
        <>
          <div className="space-y-2" role="list" aria-label="Notifications">
            {notifications.map((notification: NotificationType) => (
              <NotificationItem
                key={notification.id}
                notification={notification}
                onMarkAsRead={handleMarkAsRead}
                onDelete={handleDelete}
              />
            ))}
          </div>

          {/* Pagination */}
          {totalPages > 1 && (
            <div className="flex items-center justify-between border-t border-gray-100 pt-4 dark:border-gray-700">
              <p className="text-xs text-gray-500 dark:text-gray-400">
                Page {currentPage} of {totalPages}
                <span className="ml-1">({totalCount} total)</span>
              </p>
              <div className="flex items-center gap-1">
                <button
                  type="button"
                  onClick={() => setCurrentPage((p) => Math.max(1, p - 1))}
                  disabled={currentPage <= 1}
                  className="rounded-lg border border-gray-200 bg-white px-3 py-1.5 text-xs font-medium text-gray-700 transition-all hover:bg-gray-50 disabled:cursor-not-allowed disabled:opacity-50 dark:border-gray-600 dark:bg-gray-700 dark:text-gray-300 dark:hover:bg-gray-600"
                  aria-label="Previous page"
                >
                  Previous
                </button>
                {Array.from({ length: Math.min(5, totalPages) }, (_, i) => {
                  const start = Math.max(1, currentPage - 2);
                  const page = start + i;
                  if (page > totalPages) return null;
                  return (
                    <button
                      key={page}
                      type="button"
                      onClick={() => setCurrentPage(page)}
                      className={`rounded-lg px-3 py-1.5 text-xs font-medium transition-all ${
                        page === currentPage
                          ? 'bg-blue-600 text-white'
                          : 'border border-gray-200 bg-white text-gray-700 hover:bg-gray-50 dark:border-gray-600 dark:bg-gray-700 dark:text-gray-300 dark:hover:bg-gray-600'
                      }`}
                    >
                      {page}
                    </button>
                  );
                })}
                <button
                  type="button"
                  onClick={() => setCurrentPage((p) => Math.min(totalPages, p + 1))}
                  disabled={currentPage >= totalPages}
                  className="rounded-lg border border-gray-200 bg-white px-3 py-1.5 text-xs font-medium text-gray-700 transition-all hover:bg-gray-50 disabled:cursor-not-allowed disabled:opacity-50 dark:border-gray-600 dark:bg-gray-700 dark:text-gray-300 dark:hover:bg-gray-600"
                  aria-label="Next page"
                >
                  Next
                </button>
              </div>
            </div>
          )}
        </>
      )}
    </div>
  );
};

export default NotificationCenter;
