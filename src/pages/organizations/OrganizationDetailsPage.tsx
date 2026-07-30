import { useParams, useNavigate } from 'react-router-dom';
import { Box, Button, Typography, Breadcrumbs, Link, Grid, Paper, Stack } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';
import { PageHeader, LoadingSkeleton, StatusChip, ConfirmationDialog } from '@/components/common';
import { useOrganization, useDeleteOrganization } from '@/services';
import { ROUTES } from '@/constants';
import BusinessIcon from '@mui/icons-material/Business';
import EmailIcon from '@mui/icons-material/Email';
import PhoneIcon from '@mui/icons-material/Phone';
import LanguageIcon from '@mui/icons-material/Language';
import LocationOnIcon from '@mui/icons-material/LocationOn';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import { useState, useCallback } from 'react';
import { toast } from 'react-toastify';
import dayjs from 'dayjs';

export function OrganizationDetailsPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [deleteOpen, setDeleteOpen] = useState(false);
  const { data: org, isLoading, error } = useOrganization(id ? Number(id) : undefined);
  const deleteMutation = useDeleteOrganization();

  const handleDelete = useCallback(async () => {
    if (!org) return;
    try {
      await deleteMutation.mutateAsync(org.id);
      toast.success('Organization deleted');
      navigate(ROUTES.ORGANIZATIONS);
    } catch { toast.error('Failed to delete organization'); }
  }, [org, deleteMutation, navigate]);

  if (isLoading) return <LoadingSkeleton variant="detail" />;
  if (error || !org) return <Typography color="error">Failed to load organization details.</Typography>;

  const infoItems = [
    { label: 'Organization Name', value: org.name, icon: <BusinessIcon /> },
    { label: 'Code', value: org.code, icon: null },
    { label: 'Email', value: org.email || '-', icon: <EmailIcon /> },
    { label: 'Phone', value: org.phone || '-', icon: <PhoneIcon /> },
    { label: 'Website', value: org.website || '-', icon: <LanguageIcon /> },
    { label: 'City', value: org.city || '-', icon: <LocationOnIcon /> },
    { label: 'State', value: org.state || '-', icon: null },
    { label: 'Country', value: org.country || '-', icon: null },
    { label: 'Postal Code', value: org.postalCode || '-', icon: null },
    { label: 'Status', value: <StatusChip status={org.isActive ? 'ACTIVE' : 'INACTIVE'} />, icon: null },
    { label: 'Created', value: dayjs(org.createdAt).format('MMM D, YYYY h:mm A'), icon: null },
    { label: 'Last Updated', value: dayjs(org.updatedAt).format('MMM D, YYYY h:mm A'), icon: null },
  ];

  return (
    <Box>
      <Breadcrumbs sx={{ mb: 1 }}>
        <Link component={RouterLink} to={ROUTES.ORGANIZATIONS} underline="hover" color="text.secondary">Organizations</Link>
        <Typography color="text.primary">{org.name}</Typography>
      </Breadcrumbs>
      <PageHeader
        title={org.name} description={`Organization code: ${org.code}`}
        action={
          <Stack direction="row" spacing={1}>
            <Button variant="outlined" startIcon={<ArrowBackIcon />} onClick={() => navigate(ROUTES.ORGANIZATIONS)} sx={{ borderRadius: 2 }}>Back</Button>
            <Button variant="contained" startIcon={<EditIcon />} onClick={() => navigate(`/organizations/${org.id}/edit`)} sx={{ borderRadius: 2 }}>Edit</Button>
            <Button variant="outlined" color="error" startIcon={<DeleteIcon />} onClick={() => setDeleteOpen(true)} sx={{ borderRadius: 2 }}>Delete</Button>
          </Stack>
        }
      />
      <Paper elevation={0} sx={{ p: 4, border: '1px solid', borderColor: 'divider', borderRadius: 2 }}>
        <Typography variant="subtitle1" fontWeight={600} sx={{ mb: 3, display: 'flex', alignItems: 'center', gap: 1 }}>
          <BusinessIcon color="primary" /> Organization Details
        </Typography>
        <Grid container spacing={3}>
          {infoItems.map((item) => (
            <Grid item xs={12} sm={6} md={4} key={item.label}>
              <Box>
                <Typography variant="caption" color="text.secondary" sx={{ display: 'flex', alignItems: 'center', gap: 0.5, mb: 0.5 }}>
                  {item.icon && <Box sx={{ fontSize: 14, display: 'flex', color: 'text.secondary' }}>{item.icon}</Box>}
                  {item.label}
                </Typography>
                <Typography variant="body1" fontWeight={500}>{item.value}</Typography>
              </Box>
            </Grid>
          ))}
        </Grid>
      </Paper>
      <ConfirmationDialog open={deleteOpen} title="Delete Organization"
        message={`Delete "${org.name}"? This will affect all associated data.`}
        confirmLabel="Delete" onConfirm={handleDelete} onCancel={() => setDeleteOpen(false)}
        isLoading={deleteMutation.isPending} variant="delete" />
    </Box>
  );
}
