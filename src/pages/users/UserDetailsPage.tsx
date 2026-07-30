import { useParams, useNavigate } from 'react-router-dom';
import { Box, Button, Typography, Breadcrumbs, Link, Grid, Paper, Stack, Avatar, Divider, MenuItem, Select, FormControl, InputLabel } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';
import { useState, useCallback } from 'react';
import { toast } from 'react-toastify';
import { PageHeader, LoadingSkeleton, StatusChip, ConfirmationDialog } from '@/components/common';
import { useManagedUser, useDeleteManagedUser, useAssignOrganization, useAssignRole, useOrganizations } from '@/services';
import { ROUTES } from '@/constants';
import type { UserRole } from '@/types';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import dayjs from 'dayjs';

const roles: UserRole[] = ['ADMIN', 'MANAGER', 'VIEWER', 'USER'];

export function UserDetailsPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [deleteOpen, setDeleteOpen] = useState(false);
  const [selectedRole, setSelectedRole] = useState('');
  const [selectedOrg, setSelectedOrg] = useState<number | ''>('');
  const [savingRole, setSavingRole] = useState(false);
  const [savingOrg, setSavingOrg] = useState(false);

  const { data: user, isLoading, error } = useManagedUser(id ? Number(id) : undefined);
  const { data: orgsData } = useOrganizations({ size: 100 });
  const deleteMutation = useDeleteManagedUser();
  const assignOrgMutation = useAssignOrganization();
  const assignRoleMutation = useAssignRole();

  const handleDelete = useCallback(async () => {
    if (!user) return;
    try {
      await deleteMutation.mutateAsync(user.id);
      toast.success('User deleted successfully');
      navigate(ROUTES.USERS);
    } catch {
      toast.error('Failed to delete user');
    }
  }, [user, deleteMutation, navigate]);

  const handleAssignRole = useCallback(async () => {
    if (!user || !selectedRole) return;
    setSavingRole(true);
    try {
      await assignRoleMutation.mutateAsync({ userId: user.id, role: selectedRole });
      toast.success('Role updated successfully');
    } catch {
      toast.error('Failed to update role');
    } finally {
      setSavingRole(false);
    }
  }, [user, selectedRole, assignRoleMutation]);

  const handleAssignOrg = useCallback(async () => {
    if (!user || selectedOrg === '') return;
    setSavingOrg(true);
    try {
      await assignOrgMutation.mutateAsync({ userId: user.id, organizationId: selectedOrg as number });
      toast.success('Organization assigned successfully');
    } catch {
      toast.error('Failed to assign organization');
    } finally {
      setSavingOrg(false);
    }
  }, [user, selectedOrg, assignOrgMutation]);

  if (isLoading) return <LoadingSkeleton variant="detail" />;
  if (error || !user) return <Typography color="error">Failed to load user details.</Typography>;

  const initials = `${user.firstName[0]}${user.lastName[0]}`.toUpperCase();

  return (
    <Box>
      <Breadcrumbs sx={{ mb: 1 }}>
        <Link component={RouterLink} to={ROUTES.USERS} underline="hover" color="text.secondary">Users</Link>
        <Typography color="text.primary">{user.fullName}</Typography>
      </Breadcrumbs>

      <PageHeader
        title={user.fullName}
        description={user.email}
        action={
          <Stack direction="row" spacing={1}>
            <Button variant="outlined" startIcon={<ArrowBackIcon />} onClick={() => navigate(ROUTES.USERS)} sx={{ borderRadius: 2 }}>Back</Button>
            <Button variant="contained" startIcon={<EditIcon />} onClick={() => navigate(`/users/${user.id}/edit`)} sx={{ borderRadius: 2 }}>Edit</Button>
            <Button variant="outlined" color="error" startIcon={<DeleteIcon />} onClick={() => setDeleteOpen(true)} sx={{ borderRadius: 2 }}>Delete</Button>
          </Stack>
        }
      />

      <Grid container spacing={3}>
        {/* Profile Info */}
        <Grid item xs={12} md={8}>
          <Paper elevation={0} sx={{ p: 4, border: '1px solid', borderColor: 'divider', borderRadius: 2 }}>
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, mb: 3 }}>
              <Avatar sx={{ width: 72, height: 72, bgcolor: 'primary.main', fontSize: '1.5rem', fontWeight: 700 }}>
                {initials}
              </Avatar>
              <Box>
                <Typography variant="h6" fontWeight={600}>{user.fullName}</Typography>
                <Typography variant="body2" color="text.secondary">{user.email}</Typography>
                <Box sx={{ mt: 0.5 }}><StatusChip status={user.role} /></Box>
              </Box>
            </Box>
            <Divider sx={{ mb: 3 }} />
            <Grid container spacing={2}>
              <Grid item xs={6}>
                <Typography variant="caption" color="text.secondary">Phone</Typography>
                <Typography variant="body2">{user.phone || '-'}</Typography>
              </Grid>
              <Grid item xs={6}>
                <Typography variant="caption" color="text.secondary">Organization</Typography>
                <Typography variant="body2">{user.organizationName || '-'}</Typography>
              </Grid>
              <Grid item xs={6}>
                <Typography variant="caption" color="text.secondary">Status</Typography>
                <Box sx={{ mt: 0.5 }}><StatusChip status={user.isActive ? 'ACTIVE' : 'INACTIVE'} /></Box>
              </Grid>
              <Grid item xs={6}>
                <Typography variant="caption" color="text.secondary">Email Verified</Typography>
                <Typography variant="body2">{user.emailVerified ? 'Yes' : 'No'}</Typography>
              </Grid>
              <Grid item xs={6}>
                <Typography variant="caption" color="text.secondary">Last Login</Typography>
                <Typography variant="body2">{user.lastLoginAt ? dayjs(user.lastLoginAt).format('MMM D, YYYY h:mm A') : 'Never'}</Typography>
              </Grid>
              <Grid item xs={6}>
                <Typography variant="caption" color="text.secondary">Created</Typography>
                <Typography variant="body2">{dayjs(user.createdAt).format('MMM D, YYYY')}</Typography>
              </Grid>
            </Grid>
          </Paper>
        </Grid>

        {/* Actions Panel */}
        <Grid item xs={12} md={4}>
          <Paper elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider', borderRadius: 2, mb: 3 }}>
            <Typography variant="subtitle2" fontWeight={600} sx={{ mb: 2 }}>Assign Role</Typography>
            <FormControl fullWidth size="small" sx={{ mb: 1.5 }}>
              <InputLabel>Select role</InputLabel>
              <Select value={selectedRole} label="Select role" onChange={(e) => setSelectedRole(e.target.value)} sx={{ borderRadius: 2 }}>
                <MenuItem value="" disabled>Select role...</MenuItem>
                {roles.map((r) => <MenuItem key={r} value={r}>{r}</MenuItem>)}
              </Select>
            </FormControl>
            <Button fullWidth variant="contained" size="small" disabled={!selectedRole || savingRole} onClick={handleAssignRole} sx={{ borderRadius: 2 }}>
              {savingRole ? 'Saving...' : 'Assign Role'}
            </Button>
          </Paper>

          <Paper elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider', borderRadius: 2 }}>
            <Typography variant="subtitle2" fontWeight={600} sx={{ mb: 2 }}>Assign Organization</Typography>
            <FormControl fullWidth size="small" sx={{ mb: 1.5 }}>
              <InputLabel>Select organization</InputLabel>
              <Select value={selectedOrg} label="Select organization" onChange={(e) => setSelectedOrg(e.target.value as number)} sx={{ borderRadius: 2 }}>
                <MenuItem value="" disabled>Select organization...</MenuItem>
                {(orgsData?.content ?? []).map((org) => <MenuItem key={org.id} value={org.id}>{org.name}</MenuItem>)}
              </Select>
            </FormControl>
            <Button fullWidth variant="contained" size="small" disabled={selectedOrg === '' || savingOrg} onClick={handleAssignOrg} sx={{ borderRadius: 2 }}>
              {savingOrg ? 'Saving...' : 'Assign Organization'}
            </Button>
          </Paper>
        </Grid>
      </Grid>

      <ConfirmationDialog
        open={deleteOpen}
        title="Delete User"
        message={`Are you sure you want to delete "${user.fullName}"?`}
        confirmLabel="Delete"
        onConfirm={handleDelete}
        onCancel={() => setDeleteOpen(false)}
        isLoading={deleteMutation.isPending}
        variant="delete"
      />
    </Box>
  );
}
