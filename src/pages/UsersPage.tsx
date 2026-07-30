import { Box } from '@mui/material';
import { PageHeader, PlaceholderCard } from '@/components/common';

export function UsersPage() {
  return (
    <Box>
      <PageHeader
        title="Users"
        description="View and manage system users."
      />
      <PlaceholderCard title="User Management" />
    </Box>
  );
}
