import { Box } from '@mui/material';
import { PageHeader, PlaceholderCard } from '@/components/common';

export function BillingPage() {
  return (
    <Box>
      <PageHeader
        title="Billing"
        description="Manage invoices, tariffs, and billing history."
      />
      <PlaceholderCard title="Billing Management" />
    </Box>
  );
}
