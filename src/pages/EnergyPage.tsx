import { Box } from '@mui/material';
import { PageHeader, PlaceholderCard } from '@/components/common';

export function EnergyPage() {
  return (
    <Box>
      <PageHeader
        title="Energy"
        description="Track and analyze energy consumption data."
      />
      <PlaceholderCard title="Energy Monitoring" />
    </Box>
  );
}
