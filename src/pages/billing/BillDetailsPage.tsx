import { useParams, useNavigate } from 'react-router-dom';
import { Box, Button, Typography, Breadcrumbs, Link, Grid, Paper, Stack, Divider } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';
import { useState, useCallback } from 'react';
import { toast } from 'react-toastify';
import { PageHeader, LoadingSkeleton, StatusChip, ConfirmationDialog } from '@/components/common';
import { useBill, useCancelBill } from '@/services';
import { ROUTES } from '@/constants';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import CancelIcon from '@mui/icons-material/Cancel';
import DownloadIcon from '@mui/icons-material/Download';
import dayjs from 'dayjs';

export function BillDetailsPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [cancelOpen, setCancelOpen] = useState(false);
  const { data: bill, isLoading, error } = useBill(id ? Number(id) : undefined);
  const cancelMutation = useCancelBill();

  const handleCancel = useCallback(async () => {
    if (!bill) return;
    try {
      await cancelMutation.mutateAsync({ id: bill.id });
      toast.success('Bill cancelled');
      setCancelOpen(false);
    } catch { toast.error('Failed to cancel bill'); }
  }, [bill, cancelMutation]);

  const handleDownload = useCallback(() => {
    toast.info('Download functionality - placeholder');
  }, []);

  if (isLoading) return <LoadingSkeleton variant="detail" />;
  if (error || !bill) return <Typography color="error">Failed to load bill details.</Typography>;

  const canCancel = bill.status === 'GENERATED' || bill.status === 'SENT' || bill.status === 'DRAFT';

  return (
    <Box>
      <Breadcrumbs sx={{ mb: 1 }}>
        <Link component={RouterLink} to={ROUTES.BILLING} underline="hover" color="text.secondary">Billing</Link>
        <Typography color="text.primary">{bill.billNumber}</Typography>
      </Breadcrumbs>

      <PageHeader
        title={`Bill: ${bill.billNumber}`}
        description={`Status: ${bill.status}`}
        action={
          <Stack direction="row" spacing={1}>
            <Button variant="outlined" startIcon={<ArrowBackIcon />} onClick={() => navigate(ROUTES.BILLING)} sx={{ borderRadius: 2 }}>Back</Button>
            <Button variant="outlined" startIcon={<DownloadIcon />} onClick={handleDownload} sx={{ borderRadius: 2 }}>Download</Button>
            {canCancel && (
              <Button variant="outlined" color="error" startIcon={<CancelIcon />} onClick={() => setCancelOpen(true)} sx={{ borderRadius: 2 }}>Cancel Bill</Button>
            )}
          </Stack>
        }
      />

      <Grid container spacing={3}>
        <Grid item xs={12} md={7}>
          <Paper elevation={0} sx={{ p: 4, border: '1px solid', borderColor: 'divider', borderRadius: 2, mb: 3 }}>
            <Typography variant="subtitle1" fontWeight={600} sx={{ mb: 3 }}>Bill Summary</Typography>
            <Grid container spacing={2}>
              <Grid item xs={6}><Typography variant="caption" color="text.secondary">Bill Number</Typography><Typography variant="body2" fontWeight={600}>{bill.billNumber}</Typography></Grid>
              <Grid item xs={6}><Typography variant="caption" color="text.secondary">Status</Typography><Box sx={{ mt: 0.5 }}><StatusChip status={bill.status} /></Box></Grid>
              <Grid item xs={6}><Typography variant="caption" color="text.secondary">Meter</Typography><Typography variant="body2">{bill.meterNumber || '-'}</Typography></Grid>
              <Grid item xs={6}><Typography variant="caption" color="text.secondary">Customer</Typography><Typography variant="body2">{bill.customerName || '-'}</Typography></Grid>
              <Grid item xs={6}><Typography variant="caption" color="text.secondary">Organization</Typography><Typography variant="body2">{bill.organizationName || '-'}</Typography></Grid>
              <Grid item xs={6}><Typography variant="caption" color="text.secondary">Period</Typography><Typography variant="body2">{dayjs(bill.billingPeriodStart).format('MMM D, YYYY')} - {dayjs(bill.billingPeriodEnd).format('MMM D, YYYY')}</Typography></Grid>
              <Grid item xs={6}><Typography variant="caption" color="text.secondary">Due Date</Typography><Typography variant="body2">{bill.dueDate ? dayjs(bill.dueDate).format('MMM D, YYYY') : '-'}</Typography></Grid>
              <Grid item xs={6}><Typography variant="caption" color="text.secondary">Generated</Typography><Typography variant="body2">{dayjs(bill.generatedDate).format('MMM D, YYYY')}</Typography></Grid>
            </Grid>
          </Paper>
        </Grid>

        <Grid item xs={12} md={5}>
          <Paper elevation={0} sx={{ p: 4, border: '1px solid', borderColor: 'divider', borderRadius: 2, mb: 3 }}>
            <Typography variant="subtitle1" fontWeight={600} sx={{ mb: 3 }}>Amount Details</Typography>
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1.5 }}>
              <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                <Typography variant="body2" color="text.secondary">Units Consumed</Typography>
                <Typography variant="body2">{bill.unitsConsumed} kWh</Typography>
              </Box>
              <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                <Typography variant="body2" color="text.secondary">Unit Rate</Typography>
                <Typography variant="body2">${bill.unitRate?.toFixed(4)}</Typography>
              </Box>
              <Divider />
              <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                <Typography variant="body2">Energy Charge</Typography>
                <Typography variant="body2">${bill.energyCharge?.toFixed(2)}</Typography>
              </Box>
              <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                <Typography variant="body2" color="text.secondary">Fixed Charge</Typography>
                <Typography variant="body2">${bill.fixedCharge?.toFixed(2)}</Typography>
              </Box>
              <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                <Typography variant="body2" color="text.secondary">Tax</Typography>
                <Typography variant="body2">${bill.taxAmount?.toFixed(2)}</Typography>
              </Box>
              {bill.otherCharges ? (
                <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                  <Typography variant="body2" color="text.secondary">Other Charges</Typography>
                  <Typography variant="body2">${bill.otherCharges?.toFixed(2)}</Typography>
                </Box>
              ) : null}
              {bill.discountAmount ? (
                <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                  <Typography variant="body2" color="success.main">Discount</Typography>
                  <Typography variant="body2" color="success.main">-${bill.discountAmount?.toFixed(2)}</Typography>
                </Box>
              ) : null}
              <Divider />
              <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                <Typography variant="subtitle1" fontWeight={700}>Total Amount</Typography>
                <Typography variant="subtitle1" fontWeight={700} color="primary">
                  ${bill.totalAmount?.toFixed(2)}
                </Typography>
              </Box>
              {bill.amountPaid ? (
                <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                  <Typography variant="body2" color="success.main">Amount Paid</Typography>
                  <Typography variant="body2" color="success.main">${bill.amountPaid?.toFixed(2)}</Typography>
                </Box>
              ) : null}
              {bill.balanceDue ? (
                <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                  <Typography variant="body2" color="error">Balance Due</Typography>
                  <Typography variant="body2" color="error">${bill.balanceDue?.toFixed(2)}</Typography>
                </Box>
              ) : null}
            </Box>
          </Paper>
        </Grid>
      </Grid>

      {bill.notes && (
        <Paper elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider', borderRadius: 2 }}>
          <Typography variant="subtitle2" fontWeight={600} sx={{ mb: 1 }}>Notes</Typography>
          <Typography variant="body2" color="text.secondary">{bill.notes}</Typography>
        </Paper>
      )}

      <ConfirmationDialog open={cancelOpen} title="Cancel Bill"
        message={`Are you sure you want to cancel bill "${bill.billNumber}"?`}
        confirmLabel="Cancel Bill" onConfirm={handleCancel} onCancel={() => setCancelOpen(false)}
        isLoading={cancelMutation.isPending} variant="warning" />
    </Box>
  );
}
