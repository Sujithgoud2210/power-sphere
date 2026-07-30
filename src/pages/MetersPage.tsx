import { Box } from '@mui/material';
import { PageHeader, PlaceholderCard } from '@/components/common';

export function MetersPage() {
  return (
    <Box>
      <PageHeader
        title="Meters"
        description="Monitor and manage energy meters."
      />
      <PlaceholderCard title="Meter Management" />
    </Box>
  );
}
