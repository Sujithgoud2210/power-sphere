import { Box, Button, Typography, Breadcrumbs, Link, TextField, MenuItem, Grid, Paper } from '@mui/material';
import { useNavigate, Link as RouterLink } from 'react-router-dom';
import { useState, useCallback } from 'react';
import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { toast } from 'react-toastify';
import { PageHeader } from '@/components/common';
import dayjs from 'dayjs';
import { useGenerateBill, useMeters } from '@/services';
import { ROUTES } from '@/constants';
const MONTHS = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];

const generateSchema = z.object({
  meterId: z.number({ required_error: 'Meter is required' }).min(1, 'Meter is required'),
  billingMonth: z.number({ required_error: 'Month is required' }).min(1).max(12),
  billingYear: z.number({ required_error: 'Year is required' }).min(2020).max(2100),
  dueDate: z.string().optional().or(z.literal('')),
  notes: z.string().optional().or(z.literal('')),
});

type GenerateFormData = z.infer<typeof generateSchema>;

export function GenerateBillPage() {
  const navigate = useNavigate();
  const [isSubmitting, setIsSubmitting] = useState(false);
  const { data: metersData } = useMeters({ size: 100 });
  const generateMutation = useGenerateBill();

  const { control, handleSubmit, formState: { errors } } = useForm<GenerateFormData>({
    resolver: zodResolver(generateSchema),
    defaultValues: {
      meterId: undefined as unknown as number,
      billingMonth: dayjs().month() + 1,
      billingYear: dayjs().year(),
      dueDate: '',
      notes: '',
    },
  });

  const onSubmit = useCallback(async (data: GenerateFormData) => {
    setIsSubmitting(true);
    try {
      await generateMutation.mutateAsync({
        meterId: data.meterId,
        billingMonth: data.billingMonth,
        billingYear: data.billingYear,
        dueDate: data.dueDate || undefined,
        notes: data.notes || undefined,
      });
      toast.success('Bill generated successfully');
      navigate(ROUTES.BILLING);
    } catch {
      toast.error('Failed to generate bill');
    } finally {
      setIsSubmitting(false);
    }
  }, [generateMutation, navigate]);

  return (
    <Box>
      <Breadcrumbs sx={{ mb: 1 }}>
        <Link component={RouterLink} to={ROUTES.BILLING} underline="hover" color="text.secondary">Billing</Link>
        <Typography color="text.primary">Generate Bill</Typography>
      </Breadcrumbs>

      <PageHeader title="Generate Bill" description="Create a new electricity bill for a meter" />

      <Paper elevation={0} sx={{ p: 4, border: '1px solid', borderColor: 'divider', borderRadius: 2, maxWidth: 600 }}>
        <Box component="form" onSubmit={handleSubmit(onSubmit)} noValidate>
          <Grid container spacing={2.5}>
            <Grid item xs={12}>
              <Controller name="meterId" control={control} render={({ field }) => (
                <TextField
                  {...field} fullWidth label="Meter" select required
                  value={field.value ?? ''}
                  onChange={(e) => field.onChange(Number(e.target.value))}
                  error={!!errors.meterId} helperText={errors.meterId?.message}
                  disabled={isSubmitting}
                  slotProps={{ input: { sx: { borderRadius: 2 } } }}
                >
                  <MenuItem value="" disabled>Select meter...</MenuItem>
                  {(metersData?.content ?? []).map((m) => (
                    <MenuItem key={m.id} value={m.id}>{m.meterNumber} - {m.location || m.manufacturer}</MenuItem>
                  ))}
                </TextField>
              )} />
            </Grid>
            <Grid item xs={12} sm={6}>
              <Controller name="billingMonth" control={control} render={({ field }) => (
                <TextField
                  {...field} fullWidth label="Billing Month" select required
                  value={field.value ?? ''}
                  onChange={(e) => field.onChange(Number(e.target.value))}
                  error={!!errors.billingMonth}
                  disabled={isSubmitting}
                  slotProps={{ input: { sx: { borderRadius: 2 } } }}
                >
                  {MONTHS.map((month, idx) => (
                    <MenuItem key={idx} value={idx + 1}>{month}</MenuItem>
                  ))}
                </TextField>
              )} />
            </Grid>
            <Grid item xs={12} sm={6}>
              <Controller name="billingYear" control={control} render={({ field }) => (
                <TextField
                  {...field} fullWidth label="Billing Year" type="number" required
                  value={field.value ?? ''}
                  onChange={(e) => field.onChange(Number(e.target.value))}
                  error={!!errors.billingYear} helperText={errors.billingYear?.message}
                  disabled={isSubmitting}
                  slotProps={{ input: { sx: { borderRadius: 2 } } }}
                />
              )} />
            </Grid>
            <Grid item xs={12} sm={6}>
              <Controller name="dueDate" control={control} render={({ field }) => (
                <TextField {...field} fullWidth label="Due Date" type="date" disabled={isSubmitting}
                  slotProps={{ inputLabel: { shrink: true }, input: { sx: { borderRadius: 2 } } }} />
              )} />
            </Grid>
            <Grid item xs={12}>
              <Controller name="notes" control={control} render={({ field }) => (
                <TextField {...field} fullWidth label="Notes" multiline rows={2} disabled={isSubmitting}
                  slotProps={{ input: { sx: { borderRadius: 2 } } }} />
              )} />
            </Grid>
          </Grid>

          <Box sx={{ display: 'flex', gap: 2, mt: 4, justifyContent: 'flex-end' }}>
            <Button variant="outlined" onClick={() => navigate(ROUTES.BILLING)} sx={{ borderRadius: 2 }}>Cancel</Button>
            <Button type="submit" variant="contained" disabled={isSubmitting} sx={{ borderRadius: 2, minWidth: 120 }}>
              {isSubmitting ? 'Generating...' : 'Generate Bill'}
            </Button>
          </Box>
        </Box>
      </Paper>
    </Box>
  );
}
