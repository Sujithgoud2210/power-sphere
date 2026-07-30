import { Box } from '@mui/material';
import { PageHeader, PlaceholderCard } from '@/components/common';

export function NotificationsPage() {
  return (
    <Box>
      <PageHeader
        title="Notifications"
        description="Configure alerts and view notification history."
      />
      <PlaceholderCard title="Notification Center" />
    </Box>
  );
}
