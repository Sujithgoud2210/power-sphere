import { useState, useCallback } from 'react';
import { Box, Button } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import { PageHeader, DataTable, SearchBar, StatusChip, ConfirmationDialog } from '@/components/common';
import { useManagedUsers, useDeleteManagedUser } from '@/services';
import { ROUTES } from '@/constants';
import type { ManagedUser, UserFilters } from '@/types';
import type { GridColDef, GridPaginationModel, GridSortModel } from '@mui/x-data-grid';
import AddIcon from '@mui/icons-material/Add';
import dayjs from 'dayjs';

export function UserListPage() {
  const navigate = useNavigate();
  const [filters, setFilters] = useState<UserFilters>({ page: 0, size: 10, sortBy: 'createdAt', sortDirection: 'DESC' });
  const [searchQuery, setSearchQuery] = useState('');
  const [deleteOpen, setDeleteOpen] = useState(false);
  const [selectedUser, setSelectedUser] = useState<ManagedUser | null>(null);

  const { data, isLoading, error, refetch } = useManagedUsers({ ...filters, search: searchQuery || undefined });
  const deleteMutation = useDeleteManagedUser();

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
    if (!selectedUser) return;
    try {
      await deleteMutation.mutateAsync(selectedUser.id);
      toast.success('User deleted successfully');
      setDeleteOpen(false);
      setSelectedUser(null);
    } catch {
      toast.error('Failed to delete user');
    }
  }, [selectedUser, deleteMutation]);

  const columns: GridColDef[] = [
    { field: 'firstName', headerName: 'First Name', flex: 1, minWidth: 120 },
    { field: 'lastName', headerName: 'Last Name', flex: 1, minWidth: 120 },
    { field: 'email', headerName: 'Email', flex: 1.5, minWidth: 180 },
    {
      field: 'role',
      headerName: 'Role',
      width: 120,
      renderCell: (params) => <StatusChip status={params.value} />,
    },
    { field: 'organizationName', headerName: 'Organization', flex: 1, minWidth: 150 },
    {
      field: 'isActive',
      headerName: 'Status',
      width: 100,
      renderCell: (params) => <StatusChip status={params.value ? 'ACTIVE' : 'INACTIVE'} />,
    },
    {
      field: 'lastLoginAt',
      headerName: 'Last Login',
      width: 160,
      valueFormatter: (value) => value ? dayjs(value).format('MMM D, YYYY') : '-',
    },
    {
      field: 'actions',
      headerName: 'Actions',
      width: 120,
      sortable: false,
      renderCell: (params) => (
        <Box sx={{ display: 'flex', gap: 0.5 }}>
          <Button
            size="small"
            variant="outlined"
            onClick={(e) => { e.stopPropagation(); navigate(`/users/${params.row.id}`); }}
            sx={{ borderRadius: 1.5, minWidth: 0, px: 1 }}
          >
            View
          </Button>
          <Button
            size="small"
            color="error"
            variant="text"
            onClick={(e) => {
              e.stopPropagation();
              setSelectedUser(params.row as ManagedUser);
              setDeleteOpen(true);
            }}
            sx={{ borderRadius: 1.5, minWidth: 0, px: 1 }}
          >
            Delete
          </Button>
        </Box>
      ),
    },
  ];

  return (
    <Box>
      <PageHeader
        title="Users"
        description="View and manage system users."
        action={
          <Button
            variant="contained"
            startIcon={<AddIcon />}
            onClick={() => navigate(ROUTES.USERS_CREATE)}
            sx={{ borderRadius: 2 }}
          >
            Add User
          </Button>
        }
      />

      <Box sx={{ mb: 3 }}>
        <SearchBar value={searchQuery} onChange={handleSearch} placeholder="Search users..." />
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
        onRowClick={(params) => navigate(`/users/${params.row.id}`)}
        getRowId={(row) => (row as unknown as ManagedUser).id}
        title="All Users"
      />

      <ConfirmationDialog
        open={deleteOpen}
        title="Delete User"
        message={`Are you sure you want to delete "${selectedUser?.firstName} ${selectedUser?.lastName}"? This action cannot be undone.`}
        confirmLabel="Delete"
        onConfirm={handleDelete}
        onCancel={() => { setDeleteOpen(false); setSelectedUser(null); }}
        isLoading={deleteMutation.isPending}
        variant="delete"
      />
    </Box>
  );
}
