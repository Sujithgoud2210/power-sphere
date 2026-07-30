import { useState, useCallback } from 'react';
import { Box, Button } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import { PageHeader, DataTable, SearchBar, StatusChip, ConfirmationDialog } from '@/components/common';
import { useOrganizations, useDeleteOrganization } from '@/services';
import { ROUTES } from '@/constants';
import type { Organization, OrganizationFilters } from '@/types';
import type { GridColDef, GridPaginationModel, GridSortModel } from '@mui/x-data-grid';
import AddIcon from '@mui/icons-material/Add';

export function OrganizationListPage() {
  const navigate = useNavigate();
  const [filters, setFilters] = useState<OrganizationFilters>({ page: 0, size: 10, sortBy: 'createdAt', sortDirection: 'DESC' });
  const [searchQuery, setSearchQuery] = useState('');
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
  const [selectedOrg, setSelectedOrg] = useState<Organization | null>(null);

  const { data, isLoading, error, refetch } = useOrganizations({ ...filters, search: searchQuery || undefined });
  const deleteMutation = useDeleteOrganization();

  const handleSearch = useCallback((value: string) => {
    setSearchQuery(value);
    setFilters((prev) => ({ ...prev, page: 0 }));
  }, []);

  const handlePagination = useCallback((model: GridPaginationModel) => {
    setFilters((prev) => ({ ...prev, page: model.page, size: model.pageSize }));
  }, []);

  const handleSort = useCallback((model: GridSortModel) => {
    if (model.length > 0) {
      setFilters((prev) => ({
        ...prev,
        sortBy: model[0].field,
        sortDirection: model[0].sort === 'asc' ? 'ASC' : 'DESC',
      }));
    }
  }, []);

  const handleDelete = useCallback(async () => {
    if (!selectedOrg) return;
    try {
      await deleteMutation.mutateAsync(selectedOrg.id);
      toast.success('Organization deleted successfully');
      setDeleteDialogOpen(false);
      setSelectedOrg(null);
    } catch {
      toast.error('Failed to delete organization');
    }
  }, [selectedOrg, deleteMutation]);

  const columns: GridColDef[] = [
    { field: 'name', headerName: 'Name', flex: 1.5, minWidth: 180 },
    { field: 'code', headerName: 'Code', width: 120 },
    { field: 'email', headerName: 'Email', flex: 1, minWidth: 180 },
    { field: 'phone', headerName: 'Phone', width: 150 },
    { field: 'city', headerName: 'City', width: 130 },
    {
      field: 'isActive',
      headerName: 'Status',
      width: 110,
      renderCell: (params) => <StatusChip status={params.value ? 'ACTIVE' : 'INACTIVE'} />,
    },
    {
      field: 'actions',
      headerName: 'Actions',
      width: 120,
      sortable: false,
      renderCell: (params) => (
        <Box sx={{ display: 'flex', gap: 0.5 }}>
          <Button size="small" variant="outlined" onClick={(e) => { e.stopPropagation(); navigate(`/organizations/${params.row.id}`); }} sx={{ borderRadius: 1.5, minWidth: 0, px: 1 }}>View</Button>
          <Button size="small" color="error" variant="text" onClick={(e) => { e.stopPropagation(); setSelectedOrg(params.row as Organization); setDeleteDialogOpen(true); }} sx={{ borderRadius: 1.5, minWidth: 0, px: 1 }}>Delete</Button>
        </Box>
      ),
    },
  ];

  return (
    <Box>
      <PageHeader
        title="Organizations"
        description="Manage organizations, departments, and teams."
        action={
          <Button variant="contained" startIcon={<AddIcon />} onClick={() => navigate(ROUTES.ORGANIZATIONS_CREATE)} sx={{ borderRadius: 2 }}>Add Organization</Button>
        }
      />
      <Box sx={{ mb: 3 }}>
        <SearchBar value={searchQuery} onChange={handleSearch} placeholder="Search organizations..." />
      </Box>
      <DataTable
        rows={(data?.content ?? []) as unknown as Record<string, unknown>[]}
        columns={columns}
        loading={isLoading}
        error={error ? (error as Error).message : null}
        onRetry={refetch}
        totalRows={data?.totalElements ?? 0}
        page={data?.number ?? filters.page}
        pageSize={data?.size ?? filters.size}
        onPaginationModelChange={handlePagination}
        onSortModelChange={handleSort}
        onRowClick={(params) => navigate(`/organizations/${params.row.id}`)}
        getRowId={(row) => (row as unknown as Organization).id}
        title="All Organizations"
      />
      <ConfirmationDialog
        open={deleteDialogOpen}
        title="Delete Organization"
        message={`Are you sure you want to delete "${selectedOrg?.name}"? This action cannot be undone.`}
        confirmLabel="Delete"
        onConfirm={handleDelete}
        onCancel={() => { setDeleteDialogOpen(false); setSelectedOrg(null); }}
        isLoading={deleteMutation.isPending}
        variant="delete"
      />
    </Box>
  );
}
