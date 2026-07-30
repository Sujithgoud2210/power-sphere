import { Box } from '@mui/material';
import { PageHeader, PlaceholderCard } from '@/components/common';

export function OrganizationsPage() {
  return (
    <Box>
      <PageHeader
        title="Organizations"
        description="Manage organizations, departments, and teams."
      />
      <PlaceholderCard title="Organizations Management" />
    </Box>
  );
}
