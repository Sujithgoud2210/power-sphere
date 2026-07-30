import { useParams, useNavigate } from 'react-router-dom';
import { Box, Button, Typography, Breadcrumbs, Link, Grid, Paper, Stack } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';
import { toast } from 'react-toastify';
import { useState, useCallback } from 'react';
import { PageHeader, LoadingSkeleton, StatusChip, ConfirmationDialog } from '@/components/common';
import { useEnergyReading, useDeleteEnergyReading } from '@/services';
import { ROUTES } from '@/constants';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import DeleteIcon from '@mui/icons-material/Delete';
import HistoryIcon from '@mui/icons-material/History';
import dayjs from 'dayjs';

export function EnergyDetailsPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [deleteOpen, setDeleteOpen] = useState(false);
  const { data: reading, isLoading, error } = useEnergyReading(id ? Number(id) : undefined);
  const deleteMutation = useDeleteEnergyReading();

  const handleDelete = useCallback(async () => {
    if (!reading) return;
    try {
      await deleteMutation.mutateAsync(reading.id);
      toast.success('Reading deleted');
      navigate(ROUTES.ENERGY);
    } catch { toast.error('Failed to delete reading'); }
  }, [reading, deleteMutation, navigate]);

  if (isLoading) return <LoadingSkeleton variant="detail" />;
  if (error || !reading) return <Typography color="error">Failed to load reading details.</Typography>;

  return (
    <Box>
      <Breadcrumbs sx={{ mb: 1 }}>
        <Link component={RouterLink} to={ROUTES.ENERGY} underline="hover" color="text.secondary">Energy Readings</Link>
        <Typography color="text.primary">Reading #{reading.id}</Typography>
      </Breadcrumbs>
      <PageHeader
        title={`Energy Reading #${reading.id}`}
        description={`Meter: ${reading.meterNumber || `#${reading.meterId}`}`}
        action={
          <Stack direction="row" spacing={1}>
            <Button variant="outlined" startIcon={<ArrowBackIcon />} onClick={() => navigate(ROUTES.ENERGY)} sx={{ borderRadius: 2 }}>Back</Button>
            {reading.meterId && (
              <Button variant="outlined" startIcon={<HistoryIcon />} onClick={() => navigate(`/energy/consumption/${reading.meterId}`)} sx={{ borderRadius: 2 }}>Consumption History</Button>
            )}
            <Button variant="outlined" color="error" startIcon={<DeleteIcon />} onClick={() => setDeleteOpen(true)} sx={{ borderRadius: 2 }}>Delete</Button>
          </Stack>
        }
      />
      <Paper elevation={0} sx={{ p: 4, border: '1px solid', borderColor: 'divider', borderRadius: 2 }}>
        <Grid container spacing={2}>
          <Grid item xs={6} md={3}><Typography variant="caption" color="text.secondary">Reading Value</Typography><Typography variant="h5" fontWeight={700}>{reading.readingValue} {reading.readingUnit}</Typography></Grid>
          <Grid item xs={6} md={3}><Typography variant="caption" color="text.secondary">Timestamp</Typography><Typography variant="body2">{dayjs(reading.readingTimestamp).format('MMM D, YYYY h:mm A')}</Typography></Grid>
          <Grid item xs={6} md={3}><Typography variant="caption" color="text.secondary">Type</Typography><Box sx={{ mt: 0.5 }}><StatusChip status={reading.readingType} /></Box></Grid>
          <Grid item xs={6} md={3}><Typography variant="caption" color="text.secondary">Quality</Typography><Box sx={{ mt: 0.5 }}><StatusChip status={reading.qualityStatus} /></Box></Grid>
          <Grid item xs={6}><Typography variant="caption" color="text.secondary">Meter ID</Typography><Typography variant="body2">{reading.meterId}</Typography></Grid>
          <Grid item xs={6}><Typography variant="caption" color="text.secondary">Source</Typography><Typography variant="body2">{reading.source || '-'}</Typography></Grid>
          <Grid item xs={12}><Typography variant="caption" color="text.secondary">Remarks</Typography><Typography variant="body2">{reading.remarks || '-'}</Typography></Grid>
          <Grid item xs={6}><Typography variant="caption" color="text.secondary">Created</Typography><Typography variant="body2">{dayjs(reading.createdAt).format('MMM D, YYYY h:mm A')}</Typography></Grid>
          <Grid item xs={6}><Typography variant="caption" color="text.secondary">Created By</Typography><Typography variant="body2">{reading.createdBy || '-'}</Typography></Grid>
        </Grid>
      </Paper>
      <ConfirmationDialog open={deleteOpen} title="Delete Reading" message={`Delete energy reading #${reading.id}?`}
        confirmLabel="Delete" onConfirm={handleDelete} onCancel={() => setDeleteOpen(false)}
        isLoading={deleteMutation.isPending} variant="delete" />
    </Box>
  );
}
