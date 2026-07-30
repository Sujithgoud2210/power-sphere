import { Box, Button, Typography, Breadcrumbs, Link, TextField, MenuItem, Grid, Paper } from '@mui/material';
import { useNavigate, useParams, Link as RouterLink } from 'react-router-dom';
import { useState, useCallback } from 'react';
import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { toast } from 'react-toastify';
import { PageHeader } from '@/components/common';
import { useCreateMeter, useUpdateMeter, useMeter } from '@/services';
import { ROUTES } from '@/constants';
import type { MeterType, ConnectionType, PhaseType } from '@/types';
import dayjs from 'dayjs';

const meterSchema = z.object({
  meterNumber: z.string().min(1, 'Meter number is required'),
  serialNumber: z.string().min(1, 'Serial number is required'),
  manufacturer: z.string().min(1, 'Manufacturer is required'),
  model: z.string().min(1, 'Model is required'),
  meterType: z.string().min(1, 'Meter type is required'),
  connectionType: z.string().min(1, 'Connection type is required'),
  phaseType: z.string().min(1, 'Phase type is required'),
  location: z.string().optional().or(z.literal('')),
  installationDate: z.string().optional().or(z.literal('')),
});

type MeterFormData = z.infer<typeof meterSchema>;

export function MeterFormPage() {
  const { id } = useParams();
  const isEdit = !!id;
  const navigate = useNavigate();
  const [isSubmitting, setIsSubmitting] = useState(false);

  const { data: existingMeter } = useMeter(isEdit ? id : undefined);
  const createMutation = useCreateMeter();
  const updateMutation = useUpdateMeter();

  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm<MeterFormData>({
    resolver: zodResolver(meterSchema),
    defaultValues: {
      meterNumber: '', serialNumber: '', manufacturer: '', model: '', meterType: 'ELECTRIC',
      connectionType: 'SINGLE_PHASE', phaseType: 'SINGLE_PHASE', location: '', installationDate: '',
    },
    values: existingMeter ? {
      meterNumber: existingMeter.meterNumber,
      serialNumber: existingMeter.serialNumber,
      manufacturer: existingMeter.manufacturer,
      model: existingMeter.model,
      meterType: existingMeter.meterType,
      connectionType: existingMeter.connectionType,
      phaseType: existingMeter.phaseType,
      location: existingMeter.location ?? '',
      installationDate: existingMeter.installationDate ? dayjs(existingMeter.installationDate).format('YYYY-MM-DD') : '',
    } : undefined,
  });

  const onSubmit = useCallback(async (data: MeterFormData) => {
    setIsSubmitting(true);
    try {
      if (isEdit && id) {
        await updateMutation.mutateAsync({
          id,
          data: {
            manufacturer: data.manufacturer,
            model: data.model,
            location: data.location || undefined,
            connectionType: data.connectionType as ConnectionType,
            phaseType: data.phaseType as PhaseType,
          },
        });
        toast.success('Meter updated successfully');
      } else {
        await createMutation.mutateAsync({
          meterNumber: data.meterNumber,
          serialNumber: data.serialNumber,
          manufacturer: data.manufacturer,
          model: data.model,
          meterType: data.meterType as MeterType,
          connectionType: data.connectionType as ConnectionType,
          phaseType: data.phaseType as PhaseType,
          location: data.location || undefined,
          installationDate: data.installationDate || undefined,
        });
        toast.success('Meter registered successfully');
      }
      navigate(ROUTES.METERS);
    } catch {
      toast.error(isEdit ? 'Failed to update meter' : 'Failed to register meter');
    } finally {
      setIsSubmitting(false);
    }
  }, [isEdit, id, createMutation, updateMutation, navigate]);

  const meterTypes = ['ELECTRIC', 'GAS', 'WATER', 'SOLAR'];
  const connectionTypes = ['SINGLE_PHASE', 'THREE_PHASE', 'LT', 'HT'];
  const phaseTypes = ['SINGLE_PHASE', 'THREE_PHASE'];

  return (
    <Box>
      <Breadcrumbs sx={{ mb: 1 }}>
        <Link component={RouterLink} to={ROUTES.METERS} underline="hover" color="text.secondary">Meters</Link>
        <Typography color="text.primary">{isEdit ? 'Edit' : 'Register'} Meter</Typography>
      </Breadcrumbs>

      <PageHeader title={isEdit ? 'Edit Meter' : 'Register Meter'}
        description={isEdit ? 'Update meter details' : 'Register a new smart meter'} />

      <Paper elevation={0} sx={{ p: 4, border: '1px solid', borderColor: 'divider', borderRadius: 2 }}>
        <Box component="form" onSubmit={handleSubmit(onSubmit)} noValidate>
          <Typography variant="subtitle1" fontWeight={600} sx={{ mb: 3 }}>Meter Information</Typography>
          <Grid container spacing={2.5}>
            <Grid item xs={12} sm={6}>
              <Controller name="meterNumber" control={control} render={({ field }) => (
                <TextField {...field} fullWidth label="Meter Number" required disabled={isSubmitting || isEdit}
                  error={!!errors.meterNumber} helperText={errors.meterNumber?.message}
                  slotProps={{ input: { sx: { borderRadius: 2 } } }} />
              )} />
            </Grid>
            <Grid item xs={12} sm={6}>
              <Controller name="serialNumber" control={control} render={({ field }) => (
                <TextField {...field} fullWidth label="Serial Number" required disabled={isSubmitting || isEdit}
                  error={!!errors.serialNumber} helperText={errors.serialNumber?.message}
                  slotProps={{ input: { sx: { borderRadius: 2 } } }} />
              )} />
            </Grid>
            <Grid item xs={12} sm={6}>
              <Controller name="manufacturer" control={control} render={({ field }) => (
                <TextField {...field} fullWidth label="Manufacturer" required disabled={isSubmitting}
                  error={!!errors.manufacturer} helperText={errors.manufacturer?.message}
                  slotProps={{ input: { sx: { borderRadius: 2 } } }} />
              )} />
            </Grid>
            <Grid item xs={12} sm={6}>
              <Controller name="model" control={control} render={({ field }) => (
                <TextField {...field} fullWidth label="Model" required disabled={isSubmitting}
                  error={!!errors.model} helperText={errors.model?.message}
                  slotProps={{ input: { sx: { borderRadius: 2 } } }} />
              )} />
            </Grid>
            <Grid item xs={12} sm={4}>
              <Controller name="meterType" control={control} render={({ field }) => (
                <TextField {...field} fullWidth label="Meter Type" select required disabled={isSubmitting}
                  error={!!errors.meterType} helperText={errors.meterType?.message}
                  slotProps={{ input: { sx: { borderRadius: 2 } } }}>
                  {meterTypes.map((t) => <MenuItem key={t} value={t}>{t}</MenuItem>)}
                </TextField>
              )} />
            </Grid>
            <Grid item xs={12} sm={4}>
              <Controller name="connectionType" control={control} render={({ field }) => (
                <TextField {...field} fullWidth label="Connection Type" select required disabled={isSubmitting}
                  error={!!errors.connectionType} helperText={errors.connectionType?.message}
                  slotProps={{ input: { sx: { borderRadius: 2 } } }}>
                  {connectionTypes.map((t) => <MenuItem key={t} value={t}>{t.replace('_', ' ')}</MenuItem>)}
                </TextField>
              )} />
            </Grid>
            <Grid item xs={12} sm={4}>
              <Controller name="phaseType" control={control} render={({ field }) => (
                <TextField {...field} fullWidth label="Phase Type" select required disabled={isSubmitting}
                  error={!!errors.phaseType} helperText={errors.phaseType?.message}
                  slotProps={{ input: { sx: { borderRadius: 2 } } }}>
                  {phaseTypes.map((t) => <MenuItem key={t} value={t}>{t.replace('_', ' ')}</MenuItem>)}
                </TextField>
              )} />
            </Grid>
            <Grid item xs={12} sm={6}>
              <Controller name="location" control={control} render={({ field }) => (
                <TextField {...field} fullWidth label="Location" disabled={isSubmitting}
                  slotProps={{ input: { sx: { borderRadius: 2 } } }} />
              )} />
            </Grid>
            <Grid item xs={12} sm={6}>
              <Controller name="installationDate" control={control} render={({ field }) => (
                <TextField {...field} fullWidth label="Installation Date" type="date" disabled={isSubmitting}
                  slotProps={{ inputLabel: { shrink: true }, input: { sx: { borderRadius: 2 } } }} />
              )} />
            </Grid>
          </Grid>

          <Box sx={{ display: 'flex', gap: 2, mt: 4, justifyContent: 'flex-end' }}>
            <Button variant="outlined" onClick={() => navigate(ROUTES.METERS)} sx={{ borderRadius: 2 }}>Cancel</Button>
            <Button type="submit" variant="contained" disabled={isSubmitting} sx={{ borderRadius: 2, minWidth: 120 }}>
              {isSubmitting ? 'Saving...' : isEdit ? 'Update Meter' : 'Register Meter'}
            </Button>
          </Box>
        </Box>
      </Paper>
    </Box>
  );
}
