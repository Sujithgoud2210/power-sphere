import { Box } from '@mui/material';
import { PageHeader, PlaceholderCard } from '@/components/common';

export function SettingsPage() {
  return (
    <Box>
      <PageHeader
        title="Settings"
        description="Configure your application preferences."
      />
      <PlaceholderCard title="Application Settings" />
    </Box>
  );
}
