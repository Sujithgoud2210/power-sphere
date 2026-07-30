import { Box, Button, Typography, Breadcrumbs, Link, TextField, MenuItem, Grid, Paper } from '@mui/material';
import { useNavigate, Link as RouterLink } from 'react-router-dom';
import { useState, useCallback } from 'react';
import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { toast } from 'react-toastify';
import { PageHeader } from '@/components/common';
import { useCreateEnergyReading, useMeters } from '@/services';
import { ROUTES } from '@/constants';
import dayjs from 'dayjs';

const readingSchema = z.object({
  meterId: z.number({ required_error: 'Meter is required' }).min(1, 'Meter is required'),
  readingValue: z.number({ required_error: 'Reading value is required' }).positive('Must be positive'),
  readingUnit: z.string().optional().or(z.literal('')),
  readingTimestamp: z.string().min(1, 'Timestamp is required'),
  readingType: z.string().min(1, 'Reading type is required'),
  source: z.string().optional().or(z.literal('')),
  remarks: z.string().optional().or(z.literal('')),
});

type ReadingFormData = z.infer<typeof readingSchema>;

export function UploadReadingPage() {
  const navigate = useNavigate();
  const [isSubmitting, setIsSubmitting] = useState(false);
  const { data: metersData } = useMeters({ size: 100 });
  const createMutation = useCreateEnergyReading();

  const { control, handleSubmit, formState: { errors }, reset } = useForm<ReadingFormData>({
    resolver: zodResolver(readingSchema),
    defaultValues: {
      meterId: undefined as unknown as number,
      readingValue: undefined as unknown as number,
      readingUnit: 'kWh',
      readingTimestamp: dayjs().format('YYYY-MM-DDTHH:mm'),
      readingType: 'MANUAL',
      source: '',
      remarks: '',
    },
  });

  const onSubmit = useCallback(async (data: ReadingFormData) => {
    setIsSubmitting(true);
    try {
      await createMutation.mutateAsync({
        meterId: data.meterId,
        readingValue: data.readingValue,
        readingUnit: data.readingUnit || 'kWh',
        readingTimestamp: dayjs(data.readingTimestamp).toISOString(),
        readingType: data.readingType as 'MANUAL' | 'AUTOMATIC' | 'ESTIMATED' | 'REMOTE',
        source: data.source || undefined,
        remarks: data.remarks || undefined,
      });
      toast.success('Reading uploaded successfully');
      reset();
    } catch {
      toast.error('Failed to upload reading');
    } finally {
      setIsSubmitting(false);
    }
  }, [createMutation, reset]);

  return (
    <Box>
      <Breadcrumbs sx={{ mb: 1 }}>
        <Link component={RouterLink} to={ROUTES.ENERGY} underline="hover" color="text.secondary">Energy Readings</Link>
        <Typography color="text.primary">Upload Reading</Typography>
      </Breadcrumbs>

      <PageHeader title="Upload Energy Reading" description="Record a new energy meter reading" />

      <Paper elevation={0} sx={{ p: 4, border: '1px solid', borderColor: 'divider', borderRadius: 2, maxWidth: 700 }}>
        <Box component="form" onSubmit={handleSubmit(onSubmit)} noValidate>
          <Grid container spacing={2.5}>
            <Grid item xs={12}>
              <Controller name="meterId" control={control} render={({ field }) => (
                <TextField
                  {...field}
                  fullWidth label="Meter" select required
                  value={field.value ?? ''}
                  onChange={(e) => field.onChange(Number(e.target.value))}
                  error={!!errors.meterId} helperText={errors.meterId?.message}
                  disabled={isSubmitting}
                  slotProps={{ input: { sx: { borderRadius: 2 } } }}
                >
                  <MenuItem value="" disabled>Select meter...</MenuItem>
                  {(metersData?.content ?? []).map((m) => (
                    <MenuItem key={m.id} value={m.id}>{m.meterNumber} - {m.manufacturer} {m.model}</MenuItem>
                  ))}
                </TextField>
              )} />
            </Grid>
            <Grid item xs={12} sm={6}>
              <Controller name="readingValue" control={control} render={({ field }) => (
                <TextField
                  {...field}
                  fullWidth label="Reading Value" type="number" required
                  onChange={(e) => field.onChange(Number(e.target.value))}
                  error={!!errors.readingValue} helperText={errors.readingValue?.message}
                  disabled={isSubmitting}
                  slotProps={{ input: { sx: { borderRadius: 2 } } }}
                />
              )} />
            </Grid>
            <Grid item xs={12} sm={6}>
              <Controller name="readingUnit" control={control} render={({ field }) => (
                <TextField {...field} fullWidth label="Unit" disabled={isSubmitting}
                  slotProps={{ input: { sx: { borderRadius: 2 } } }} />
              )} />
            </Grid>
            <Grid item xs={12} sm={6}>
              <Controller name="readingTimestamp" control={control} render={({ field }) => (
                <TextField
                  {...field} fullWidth label="Reading Timestamp" type="datetime-local" required
                  error={!!errors.readingTimestamp} helperText={errors.readingTimestamp?.message}
                  disabled={isSubmitting}
                  slotProps={{ inputLabel: { shrink: true }, input: { sx: { borderRadius: 2 } } }}
                />
              )} />
            </Grid>
            <Grid item xs={12} sm={6}>
              <Controller name="readingType" control={control} render={({ field }) => (
                <TextField {...field} fullWidth label="Reading Type" select required disabled={isSubmitting}
                  slotProps={{ input: { sx: { borderRadius: 2 } } }}>
                  <MenuItem value="MANUAL">Manual</MenuItem>
                  <MenuItem value="AUTOMATIC">Automatic</MenuItem>
                  <MenuItem value="ESTIMATED">Estimated</MenuItem>
                  <MenuItem value="REMOTE">Remote</MenuItem>
                </TextField>
              )} />
            </Grid>
            <Grid item xs={12} sm={6}>
              <Controller name="source" control={control} render={({ field }) => (
                <TextField {...field} fullWidth label="Source" disabled={isSubmitting}
                  slotProps={{ input: { sx: { borderRadius: 2 } } }} />
              )} />
            </Grid>
            <Grid item xs={12}>
              <Controller name="remarks" control={control} render={({ field }) => (
                <TextField {...field} fullWidth label="Remarks" multiline rows={2} disabled={isSubmitting}
                  slotProps={{ input: { sx: { borderRadius: 2 } } }} />
              )} />
            </Grid>
          </Grid>

          <Box sx={{ display: 'flex', gap: 2, mt: 4, justifyContent: 'flex-end' }}>
            <Button variant="outlined" onClick={() => navigate(ROUTES.ENERGY)} sx={{ borderRadius: 2 }}>Cancel</Button>
            <Button type="submit" variant="contained" disabled={isSubmitting} sx={{ borderRadius: 2, minWidth: 120 }}>
              {isSubmitting ? 'Uploading...' : 'Upload Reading'}
            </Button>
          </Box>
        </Box>
      </Paper>
    </Box>
  );
}
