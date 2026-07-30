import { useState, useCallback } from 'react';
import { Box, Button, FormControl, InputLabel, MenuItem, Select } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import { PageHeader, DataTable, SearchBar, FilterPanel, StatusChip, ConfirmationDialog } from '@/components/common';
import { useMeters, useDeleteMeter } from '@/services';
import { ROUTES } from '@/constants';
import type { SmartMeter, MeterFilters, MeterStatus, MeterType } from '@/types';
import type { GridColDef, GridPaginationModel, GridSortModel } from '@mui/x-data-grid';
import AddIcon from '@mui/icons-material/Add';

export function MeterListPage() {
  const navigate = useNavigate();
  const [filters, setFilters] = useState<MeterFilters>({ page: 0, size: 10, sortBy: 'createdAt', sortDirection: 'DESC' });
  const [searchQuery, setSearchQuery] = useState('');
  const [statusFilter, setStatusFilter] = useState<string>('');
  const [typeFilter, setTypeFilter] = useState<string>('');
  const [deleteOpen, setDeleteOpen] = useState(false);
  const [selectedMeter, setSelectedMeter] = useState<SmartMeter | null>(null);

  const effective: MeterFilters = { ...filters, search: searchQuery || undefined, status: statusFilter ? (statusFilter as MeterStatus) : undefined, meterType: typeFilter ? (typeFilter as MeterType) : undefined };
  const { data, isLoading, error, refetch } = useMeters(effective);
  const deleteMutation = useDeleteMeter();
  const activeFilterCount = [statusFilter, typeFilter].filter(Boolean).length;

  const handleSearch = useCallback((value: string) => { setSearchQuery(value); setFilters((prev) => ({ ...prev, page: 0 })); }, []);
  const handlePagination = useCallback((model: GridPaginationModel) => { setFilters((prev) => ({ ...prev, page: model.page, size: model.pageSize })); }, []);
  const handleSort = useCallback((model: GridSortModel) => {
    if (model.length > 0) setFilters((prev) => ({ ...prev, sortBy: model[0].field, sortDirection: model[0].sort === 'asc' ? 'ASC' : 'DESC' }));
  }, []);

  const clearFilters = useCallback(() => { setStatusFilter(''); setTypeFilter(''); }, []);

  const handleDelete = useCallback(async () => {
    if (!selectedMeter) return;
    try { await deleteMutation.mutateAsync(selectedMeter.id); toast.success('Meter deleted'); setDeleteOpen(false); setSelectedMeter(null); }
    catch { toast.error('Failed to delete meter'); }
  }, [selectedMeter, deleteMutation]);

  const columns: GridColDef[] = [
    { field: 'meterNumber', headerName: 'Meter #', width: 130 },
    { field: 'serialNumber', headerName: 'Serial #', width: 140 },
    { field: 'manufacturer', headerName: 'Manufacturer', flex: 1, minWidth: 130 },
    { field: 'model', headerName: 'Model', width: 120 },
    { field: 'meterType', headerName: 'Type', width: 110, renderCell: (params) => <StatusChip status={params.value} /> },
    { field: 'status', headerName: 'Status', width: 130, renderCell: (params) => <StatusChip status={params.value} /> },
    { field: 'location', headerName: 'Location', flex: 1, minWidth: 130 },
    { field: 'assignedUserName', headerName: 'Assigned To', width: 150 },
    {
      field: 'actions', headerName: 'Actions', width: 120, sortable: false,
      renderCell: (params) => (
        <Box sx={{ display: 'flex', gap: 0.5 }}>
          <Button size="small" variant="outlined" onClick={(e) => { e.stopPropagation(); navigate(`/meters/${params.row.id}`); }} sx={{ borderRadius: 1.5, minWidth: 0, px: 1 }}>View</Button>
          <Button size="small" color="error" variant="text" onClick={(e) => { e.stopPropagation(); setSelectedMeter(params.row as SmartMeter); setDeleteOpen(true); }} sx={{ borderRadius: 1.5, minWidth: 0, px: 1 }}>Delete</Button>
        </Box>
      ),
    },
  ];

  return (
    <Box>
      <PageHeader title="Smart Meters" description="Monitor and manage energy meters."
        action={<Button variant="contained" startIcon={<AddIcon />} onClick={() => navigate(ROUTES.METERS_CREATE)} sx={{ borderRadius: 2 }}>Register Meter</Button>} />
      <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, mb: 3 }}>
        <SearchBar value={searchQuery} onChange={handleSearch} placeholder="Search meters..." />
        <FilterPanel activeFilterCount={activeFilterCount} onClear={clearFilters}>
          <FormControl size="small" sx={{ minWidth: 150 }}>
            <InputLabel>Status</InputLabel>
            <Select value={statusFilter} label="Status" onChange={(e) => setStatusFilter(e.target.value)} sx={{ borderRadius: 2 }}>
              <MenuItem value="">All</MenuItem>
              {['ACTIVE', 'INACTIVE', 'MAINTENANCE', 'ERROR', 'DECOMMISSIONED'].map((s) => (
                <MenuItem key={s} value={s}>{s.charAt(0) + s.slice(1).toLowerCase()}</MenuItem>
              ))}
            </Select>
          </FormControl>
          <FormControl size="small" sx={{ minWidth: 150 }}>
            <InputLabel>Meter Type</InputLabel>
            <Select value={typeFilter} label="Meter Type" onChange={(e) => setTypeFilter(e.target.value)} sx={{ borderRadius: 2 }}>
              <MenuItem value="">All</MenuItem>
              {['ELECTRIC', 'GAS', 'WATER', 'SOLAR'].map((t) => (
                <MenuItem key={t} value={t}>{t.charAt(0) + t.slice(1).toLowerCase()}</MenuItem>
              ))}
            </Select>
          </FormControl>
        </FilterPanel>
      </Box>
      <DataTable rows={(data?.content ?? []) as unknown as Record<string, unknown>[]} columns={columns}
        loading={isLoading} error={error ? (error as Error).message : null} onRetry={refetch}
        totalRows={data?.totalElements ?? 0} page={data?.number ?? filters.page} pageSize={data?.size ?? filters.size}
        onPaginationModelChange={handlePagination} onSortModelChange={handleSort}
        onRowClick={(params) => navigate(`/meters/${params.row.id}`)} getRowId={(row) => (row as unknown as SmartMeter).id} title="All Meters" />
      <ConfirmationDialog open={deleteOpen} title="Delete Meter" message={`Delete meter "${selectedMeter?.meterNumber}"?`}
        confirmLabel="Delete" onConfirm={handleDelete} onCancel={() => { setDeleteOpen(false); setSelectedMeter(null); }}
        isLoading={deleteMutation.isPending} variant="delete" />
    </Box>
  );
}
