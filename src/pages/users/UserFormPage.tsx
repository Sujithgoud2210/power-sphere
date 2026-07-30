import { Box, Button, Typography, Breadcrumbs, Link, TextField, MenuItem, Grid, Paper } from '@mui/material';
import { useNavigate, useParams, Link as RouterLink } from 'react-router-dom';
import { useState, useCallback } from 'react';
import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { toast } from 'react-toastify';
import { PageHeader } from '@/components/common';
import { useCreateManagedUser, useUpdateManagedUser, useManagedUser, useOrganizations } from '@/services';
import { ROUTES } from '@/constants';
import type { UserRole } from '@/types';

const userSchema = z.object({
  firstName: z.string().min(1, 'First name is required').max(50),
  lastName: z.string().min(1, 'Last name is required').max(50),
  email: z.string().min(1, 'Email is required').email('Invalid email'),
  password: z.string().min(8, 'Password must be at least 8 characters').optional().or(z.literal('')),
  role: z.string().min(1, 'Role is required'),
  phone: z.string().optional().or(z.literal('')),
  organizationId: z.number().optional().or(z.literal('')),
});

type UserFormData = z.infer<typeof userSchema>;

const roles: UserRole[] = ['ADMIN', 'MANAGER', 'VIEWER', 'USER'];

export function UserFormPage() {
  const { id } = useParams();
  const isEdit = !!id;
  const navigate = useNavigate();
  const [isSubmitting, setIsSubmitting] = useState(false);

  const { data: existingUser } = useManagedUser(isEdit ? Number(id) : undefined);
  const { data: orgsData } = useOrganizations({ size: 100 });
  const createMutation = useCreateManagedUser();
  const updateMutation = useUpdateManagedUser();

  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm<UserFormData>({
    resolver: zodResolver(userSchema),
    defaultValues: {
      firstName: '',
      lastName: '',
      email: '',
      password: '',
      role: 'VIEWER',
      phone: '',
      organizationId: '',
    },
    values: existingUser
      ? {
          firstName: existingUser.firstName,
          lastName: existingUser.lastName,
          email: existingUser.email,
          password: '',
          role: existingUser.role,
          phone: existingUser.phone ?? '',
          organizationId: existingUser.organizationId ?? '',
        }
      : undefined,
  });

  const onSubmit = useCallback(
    async (data: UserFormData) => {
      setIsSubmitting(true);
      try {
        if (isEdit && id) {
          await updateMutation.mutateAsync({
            id: Number(id),
            data: {
              firstName: data.firstName,
              lastName: data.lastName,
              role: data.role as UserRole,
              phone: data.phone || undefined,
              organizationId: typeof data.organizationId === 'number' ? data.organizationId : undefined,
            },
          });
          toast.success('User updated successfully');
        } else {
          await createMutation.mutateAsync({
            firstName: data.firstName,
            lastName: data.lastName,
            email: data.email,
            password: data.password!,
            role: data.role as UserRole,
            phone: data.phone || undefined,
            organizationId: typeof data.organizationId === 'number' ? data.organizationId : undefined,
          });
          toast.success('User created successfully');
        }
        navigate(ROUTES.USERS);
      } catch {
        toast.error(isEdit ? 'Failed to update user' : 'Failed to create user');
      } finally {
        setIsSubmitting(false);
      }
    },
    [isEdit, id, createMutation, updateMutation, navigate],
  );

  return (
    <Box>
      <Breadcrumbs sx={{ mb: 1 }}>
        <Link component={RouterLink} to={ROUTES.USERS} underline="hover" color="text.secondary">Users</Link>
        <Typography color="text.primary">{isEdit ? 'Edit' : 'Create'} User</Typography>
      </Breadcrumbs>

      <PageHeader
        title={isEdit ? 'Edit User' : 'Create User'}
        description={isEdit ? 'Update user details and permissions' : 'Add a new user to the system'}
      />

      <Paper elevation={0} sx={{ p: 4, border: '1px solid', borderColor: 'divider', borderRadius: 2 }}>
        <Box component="form" onSubmit={handleSubmit(onSubmit)} noValidate>
          <Typography variant="subtitle1" fontWeight={600} sx={{ mb: 3 }}>Personal Information</Typography>
          <Grid container spacing={2.5}>
            <Grid item xs={12} sm={6}>
              <Controller name="firstName" control={control} render={({ field }) => (
                <TextField {...field} fullWidth label="First Name" required error={!!errors.firstName}
                  helperText={errors.firstName?.message} disabled={isSubmitting}
                  slotProps={{ input: { sx: { borderRadius: 2 } } }} />
              )} />
            </Grid>
            <Grid item xs={12} sm={6}>
              <Controller name="lastName" control={control} render={({ field }) => (
                <TextField {...field} fullWidth label="Last Name" required error={!!errors.lastName}
                  helperText={errors.lastName?.message} disabled={isSubmitting}
                  slotProps={{ input: { sx: { borderRadius: 2 } } }} />
              )} />
            </Grid>
            <Grid item xs={12} sm={6}>
              <Controller name="email" control={control} render={({ field }) => (
                <TextField {...field} fullWidth label="Email Address" type="email" required error={!!errors.email}
                  helperText={errors.email?.message} disabled={isSubmitting || isEdit}
                  slotProps={{ input: { sx: { borderRadius: 2 } } }} />
              )} />
            </Grid>
            {!isEdit && (
              <Grid item xs={12} sm={6}>
                <Controller name="password" control={control} render={({ field }) => (
                  <TextField {...field} fullWidth label="Password" type="password" required={!isEdit}
                    error={!!errors.password} helperText={errors.password?.message} disabled={isSubmitting}
                    slotProps={{ input: { sx: { borderRadius: 2 } } }} />
                )} />
              </Grid>
            )}
            <Grid item xs={12} sm={6}>
              <Controller name="role" control={control} render={({ field }) => (
                <TextField {...field} fullWidth label="Role" select required error={!!errors.role}
                  helperText={errors.role?.message} disabled={isSubmitting}
                  slotProps={{ input: { sx: { borderRadius: 2 } } }}>
                  {roles.map((role) => (
                    <MenuItem key={role} value={role}>{role}</MenuItem>
                  ))}
                </TextField>
              )} />
            </Grid>
            <Grid item xs={12} sm={6}>
              <Controller name="phone" control={control} render={({ field }) => (
                <TextField {...field} fullWidth label="Phone Number" error={!!errors.phone}
                  helperText={errors.phone?.message} disabled={isSubmitting}
                  slotProps={{ input: { sx: { borderRadius: 2 } } }} />
              )} />
            </Grid>
            <Grid item xs={12} sm={6}>
              <Controller name="organizationId" control={control} render={({ field }) => (
                <TextField {...field} fullWidth label="Organization" select
                  value={field.value ?? ''}
                  onChange={(e) => field.onChange(e.target.value ? Number(e.target.value) : '')}
                  disabled={isSubmitting}
                  slotProps={{ input: { sx: { borderRadius: 2 } } }}>
                  <MenuItem value="">None</MenuItem>
                  {(orgsData?.content ?? []).map((org) => (
                    <MenuItem key={org.id} value={org.id}>{org.name}</MenuItem>
                  ))}
                </TextField>
              )} />
            </Grid>
          </Grid>

          <Box sx={{ display: 'flex', gap: 2, mt: 4, justifyContent: 'flex-end' }}>
            <Button variant="outlined" onClick={() => navigate(ROUTES.USERS)} sx={{ borderRadius: 2 }}>Cancel</Button>
            <Button type="submit" variant="contained" disabled={isSubmitting} sx={{ borderRadius: 2, minWidth: 120 }}>
              {isSubmitting ? 'Saving...' : isEdit ? 'Update User' : 'Create User'}
            </Button>
          </Box>
        </Box>
      </Paper>
    </Box>
  );
}
