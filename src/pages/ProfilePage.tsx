import { Box } from '@mui/material';
import { PageHeader, PlaceholderCard } from '@/components/common';

export function ProfilePage() {
  return (
    <Box>
      <PageHeader
        title="Profile"
        description="Manage your personal information and preferences."
      />
      <PlaceholderCard title="User Profile" />
    </Box>
  );
}
