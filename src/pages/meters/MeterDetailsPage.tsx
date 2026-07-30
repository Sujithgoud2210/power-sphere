import { useParams, useNavigate } from 'react-router-dom';
import { Box, Button, Typography, Breadcrumbs, Link, Grid, Paper, Stack, FormControl, InputLabel, MenuItem, Select } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';
import { useState, useCallback } from 'react';
import { toast } from 'react-toastify';
import { PageHeader, LoadingSkeleton, StatusChip, ConfirmationDialog } from '@/components/common';
import { useMeter, useDeleteMeter, useActivateMeter, useDeactivateMeter, useAssignMeter, useManagedUsers } from '@/services';
import { ROUTES } from '@/constants';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import PowerIcon from '@mui/icons-material/Power';
import PowerOffIcon from '@mui/icons-material/PowerOff';
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import dayjs from 'dayjs';

export function MeterDetailsPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [deleteOpen, setDeleteOpen] = useState(false);
  const [assignUserId, setAssignUserId] = useState<number | ''>('');
  const [assigning, setAssigning] = useState(false);

  const { data: meter, isLoading, error } = useMeter(id);
  const { data: usersData } = useManagedUsers({ size: 100 });
  const deleteMutation = useDeleteMeter();
  const activateMutation = useActivateMeter();
  const deactivateMutation = useDeactivateMeter();
  const assignMutation = useAssignMeter();

  const handleActivate = useCallback(async () => {
    if (!meter) return;
    try { await activateMutation.mutateAsync(meter.id); toast.success('Meter activated'); }
    catch { toast.error('Failed to activate meter'); }
  }, [meter, activateMutation]);

  const handleDeactivate = useCallback(async () => {
    if (!meter) return;
    try { await deactivateMutation.mutateAsync(meter.id); toast.success('Meter deactivated'); }
    catch { toast.error('Failed to deactivate meter'); }
  }, [meter, deactivateMutation]);

  const handleAssign = useCallback(async () => {
    if (!meter || assignUserId === '') return;
    setAssigning(true);
    try { await assignMutation.mutateAsync({ id: meter.id, data: { userId: assignUserId as number } }); toast.success('Meter assigned'); }
    catch { toast.error('Failed to assign meter'); }
    finally { setAssigning(false); }
  }, [meter, assignUserId, assignMutation]);

  const handleDelete = useCallback(async () => {
    if (!meter) return;
    try { await deleteMutation.mutateAsync(meter.id); toast.success('Meter deleted'); navigate(ROUTES.METERS); }
    catch { toast.error('Failed to delete meter'); }
  }, [meter, deleteMutation, navigate]);

  if (isLoading) return <LoadingSkeleton variant="detail" />;
  if (error || !meter) return <Typography color="error">Failed to load meter details.</Typography>;

  return (
    <Box>
      <Breadcrumbs sx={{ mb: 1 }}>
        <Link component={RouterLink} to={ROUTES.METERS} underline="hover" color="text.secondary">Meters</Link>
        <Typography color="text.primary">{meter.meterNumber}</Typography>
      </Breadcrumbs>
      <PageHeader
        title={`Meter: ${meter.meterNumber}`}
        description={`Serial: ${meter.serialNumber}`}
        action={
          <Stack direction="row" spacing={1} flexWrap="wrap">
            <Button variant="outlined" startIcon={<ArrowBackIcon />} onClick={() => navigate(ROUTES.METERS)} sx={{ borderRadius: 2 }}>Back</Button>
            <Button variant="contained" startIcon={<EditIcon />} onClick={() => navigate(`/meters/${meter.id}/edit`)} sx={{ borderRadius: 2 }}>Edit</Button>
            {meter.status === 'ACTIVE' ? (
              <Button variant="outlined" color="warning" startIcon={<PowerOffIcon />} onClick={handleDeactivate} disabled={deactivateMutation.isPending} sx={{ borderRadius: 2 }}>Deactivate</Button>
            ) : (
              <Button variant="outlined" color="success" startIcon={<PowerIcon />} onClick={handleActivate} disabled={activateMutation.isPending} sx={{ borderRadius: 2 }}>Activate</Button>
            )}
            <Button variant="outlined" color="error" startIcon={<DeleteIcon />} onClick={() => setDeleteOpen(true)} sx={{ borderRadius: 2 }}>Delete</Button>
          </Stack>
        }
      />
      <Grid container spacing={3}>
        <Grid item xs={12} md={8}>
          <Paper elevation={0} sx={{ p: 4, border: '1px solid', borderColor: 'divider', borderRadius: 2, mb: 3 }}>
            <Typography variant="subtitle1" fontWeight={600} sx={{ mb: 3 }}>Meter Details</Typography>
            <Grid container spacing={2}>
              <Grid item xs={6}><Typography variant="caption" color="text.secondary">Status</Typography><Box sx={{ mt: 0.5 }}><StatusChip status={meter.status} /></Box></Grid>
              <Grid item xs={6}><Typography variant="caption" color="text.secondary">Type</Typography><Box sx={{ mt: 0.5 }}><StatusChip status={meter.meterType} /></Box></Grid>
              <Grid item xs={6}><Typography variant="caption" color="text.secondary">Connection</Typography><Typography variant="body2">{meter.connectionType.replace('_', ' ')}</Typography></Grid>
              <Grid item xs={6}><Typography variant="caption" color="text.secondary">Phase</Typography><Typography variant="body2">{meter.phaseType.replace('_', ' ')}</Typography></Grid>
              <Grid item xs={6}><Typography variant="caption" color="text.secondary">Manufacturer</Typography><Typography variant="body2">{meter.manufacturer}</Typography></Grid>
              <Grid item xs={6}><Typography variant="caption" color="text.secondary">Model</Typography><Typography variant="body2">{meter.model}</Typography></Grid>
              <Grid item xs={6}><Typography variant="caption" color="text.secondary">Location</Typography><Typography variant="body2">{meter.location || '-'}</Typography></Grid>
              <Grid item xs={6}><Typography variant="caption" color="text.secondary">Installed</Typography><Typography variant="body2">{meter.installationDate ? dayjs(meter.installationDate).format('MMM D, YYYY') : '-'}</Typography></Grid>
              <Grid item xs={6}><Typography variant="caption" color="text.secondary">Assigned To</Typography><Typography variant="body2">{meter.assignedUserName || 'Unassigned'}</Typography></Grid>
              <Grid item xs={6}><Typography variant="caption" color="text.secondary">Created</Typography><Typography variant="body2">{dayjs(meter.createdAt).format('MMM D, YYYY')}</Typography></Grid>
            </Grid>
          </Paper>
        </Grid>
        <Grid item xs={12} md={4}>
          <Paper elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider', borderRadius: 2, mb: 3 }}>
            <Typography variant="subtitle2" fontWeight={600} sx={{ mb: 2 }}>Assign to User</Typography>
            <FormControl fullWidth size="small" sx={{ mb: 1.5 }}>
              <InputLabel>Select user</InputLabel>
              <Select value={assignUserId} label="Select user" onChange={(e) => setAssignUserId(e.target.value as number)} sx={{ borderRadius: 2 }}>
                <MenuItem value="" disabled>Select user...</MenuItem>
                {(usersData?.content ?? []).map((u) => (
                  <MenuItem key={u.id} value={u.id}>{u.fullName} ({u.email})</MenuItem>
                ))}
              </Select>
            </FormControl>
            <Button fullWidth variant="contained" size="small" startIcon={<PersonAddIcon />}
              disabled={assignUserId === '' || assigning} onClick={handleAssign} sx={{ borderRadius: 2 }}>
              {assigning ? 'Assigning...' : 'Assign Meter'}
            </Button>
          </Paper>
        </Grid>
      </Grid>
      <ConfirmationDialog open={deleteOpen} title="Delete Meter" message={`Delete meter "${meter.meterNumber}"?`}
        confirmLabel="Delete" onConfirm={handleDelete} onCancel={() => setDeleteOpen(false)}
        isLoading={deleteMutation.isPending} variant="delete" />
    </Box>
  );
}
