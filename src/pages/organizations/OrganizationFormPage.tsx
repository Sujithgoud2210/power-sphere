import { Box, Button, Typography, Breadcrumbs, Link, TextField, Grid, Paper } from '@mui/material';
import { useNavigate, useParams, Link as RouterLink } from 'react-router-dom';
import { useState, useCallback } from 'react';
import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { toast } from 'react-toastify';
import { PageHeader } from '@/components/common';
import { useCreateOrganization, useUpdateOrganization, useOrganization } from '@/services';
import { ROUTES } from '@/constants';
import type { CreateOrganizationRequest, UpdateOrganizationRequest } from '@/types';

const orgSchema = z.object({
  name: z.string().min(1, 'Name is required').max(200, 'Name is too long'),
  code: z.string().min(1, 'Code is required').max(20, 'Code is too long'),
  email: z.string().email('Invalid email').optional().or(z.literal('')),
  phone: z.string().optional().or(z.literal('')),
  address: z.string().optional().or(z.literal('')),
  city: z.string().optional().or(z.literal('')),
  state: z.string().optional().or(z.literal('')),
  country: z.string().optional().or(z.literal('')),
  postalCode: z.string().optional().or(z.literal('')),
  website: z.string().url('Invalid URL').optional().or(z.literal('')),
});

type OrgFormData = z.infer<typeof orgSchema>;

export function OrganizationFormPage() {
  const { id } = useParams();
  const isEdit = !!id;
  const navigate = useNavigate();
  const [isSubmitting, setIsSubmitting] = useState(false);

  const { data: existingOrg } = useOrganization(isEdit ? Number(id) : undefined);
  const createMutation = useCreateOrganization();
  const updateMutation = useUpdateOrganization();

  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm<OrgFormData>({
    resolver: zodResolver(orgSchema),
    defaultValues: {
      name: existingOrg?.name ?? '', code: existingOrg?.code ?? '',
      email: existingOrg?.email ?? '', phone: existingOrg?.phone ?? '',
      address: existingOrg?.address ?? '', city: existingOrg?.city ?? '',
      state: existingOrg?.state ?? '', country: existingOrg?.country ?? '',
      postalCode: existingOrg?.postalCode ?? '', website: existingOrg?.website ?? '',
    },
  });

  const onSubmit = useCallback(
    async (data: OrgFormData) => {
      setIsSubmitting(true);
      try {
        const payload: CreateOrganizationRequest = {
          name: data.name,
          code: data.code,
          email: data.email || undefined,
          phone: data.phone || undefined,
          address: data.address || undefined,
          city: data.city || undefined,
          state: data.state || undefined,
          country: data.country || undefined,
          postalCode: data.postalCode || undefined,
          website: data.website || undefined,
        };

        if (isEdit && id) {
          await updateMutation.mutateAsync({ id: Number(id), data: payload as UpdateOrganizationRequest });
          toast.success('Organization updated successfully');
        } else {
          await createMutation.mutateAsync(payload);
          toast.success('Organization created successfully');
        }
        navigate(ROUTES.ORGANIZATIONS);
      } catch {
        toast.error(isEdit ? 'Failed to update organization' : 'Failed to create organization');
      } finally {
        setIsSubmitting(false);
      }
    },
    [isEdit, id, createMutation, updateMutation, navigate],
  );

  const renderField = (name: keyof OrgFormData, label: string, options?: { type?: string; required?: boolean; multiline?: boolean; xs?: number }) => (
    <Grid item xs={12} sm={options?.xs ?? 6}>
      <Controller
        name={name}
        control={control}
        render={({ field }) => (
          <TextField
            {...field} fullWidth label={label} type={options?.type ?? 'text'}
            required={options?.required} multiline={options?.multiline} rows={options?.multiline ? 3 : undefined}
            error={!!errors[name]} helperText={errors[name]?.message}
            disabled={isSubmitting}
            slotProps={{ input: { sx: { borderRadius: 2 } } }}
          />
        )}
      />
    </Grid>
  );

  return (
    <Box>
      <Breadcrumbs sx={{ mb: 1 }}>
        <Link component={RouterLink} to={ROUTES.ORGANIZATIONS} underline="hover" color="text.secondary">Organizations</Link>
        <Typography color="text.primary">{isEdit ? 'Edit' : 'Create'} Organization</Typography>
      </Breadcrumbs>
      <PageHeader title={isEdit ? 'Edit Organization' : 'Create Organization'} description={isEdit ? 'Update the organization details' : 'Add a new organization to the system'} />
      <Paper elevation={0} sx={{ p: 4, border: '1px solid', borderColor: 'divider', borderRadius: 2 }}>
        <Box component="form" onSubmit={handleSubmit(onSubmit)} noValidate>
          <Typography variant="subtitle1" fontWeight={600} sx={{ mb: 3 }}>General Information</Typography>
          <Grid container spacing={2.5}>
            {renderField('name', 'Organization Name', { required: true })}
            {renderField('code', 'Organization Code', { required: true })}
            {renderField('email', 'Email Address', { type: 'email' })}
            {renderField('phone', 'Phone Number')}
            {renderField('website', 'Website', { type: 'url' })}
          </Grid>
          <Typography variant="subtitle1" fontWeight={600} sx={{ mt: 4, mb: 3 }}>Address Information</Typography>
          <Grid container spacing={2.5}>
            {renderField('address', 'Street Address', { multiline: true, xs: 12 })}
            {renderField('city', 'City')}
            {renderField('state', 'State')}
            {renderField('country', 'Country')}
            {renderField('postalCode', 'Postal Code')}
          </Grid>
          <Box sx={{ display: 'flex', gap: 2, mt: 4, justifyContent: 'flex-end' }}>
            <Button variant="outlined" onClick={() => navigate(ROUTES.ORGANIZATIONS)} sx={{ borderRadius: 2 }}>Cancel</Button>
            <Button type="submit" variant="contained" disabled={isSubmitting} sx={{ borderRadius: 2, minWidth: 120 }}>
              {isSubmitting ? 'Saving...' : isEdit ? 'Update Organization' : 'Create Organization'}
            </Button>
          </Box>
        </Box>
      </Paper>
    </Box>
  );
}
