import { Box } from '@mui/material';
import { PageHeader, PlaceholderCard } from '@/components/common';

export function ReportsPage() {
  return (
    <Box>
      <PageHeader
        title="Reports"
        description="Generate and view custom reports."
      />
      <PlaceholderCard title="Reports & Analytics" />
    </Box>
  );
}
