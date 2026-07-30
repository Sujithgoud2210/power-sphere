import { Box } from '@mui/material';
import { PageHeader, PlaceholderCard } from '@/components/common';

export function DashboardPage() {
  return (
    <Box>
      <PageHeader
        title="Dashboard"
        description="Overview of your energy management system."
      />
      <PlaceholderCard title="Dashboard Overview" />
    </Box>
  );
}
