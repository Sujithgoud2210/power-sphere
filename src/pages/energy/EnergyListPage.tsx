import { useState, useCallback } from 'react';
import { Box, Button, FormControl, InputLabel, MenuItem, Select } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import { PageHeader, DataTable, SearchBar, FilterPanel, StatusChip, ConfirmationDialog } from '@/components/common';
import { useEnergyReadings, useDeleteEnergyReading } from '@/services';
import { ROUTES } from '@/constants';
import type { EnergyReading, EnergyReadingFilters, ReadingType, QualityStatus } from '@/types';
import type { GridColDef, GridPaginationModel, GridSortModel } from '@mui/x-data-grid';
import AddIcon from '@mui/icons-material/Add';
import dayjs from 'dayjs';

export function EnergyListPage() {
  const navigate = useNavigate();
  const [filters, setFilters] = useState<EnergyReadingFilters>({ page: 0, size: 10, sortBy: 'readingTimestamp', sortDirection: 'DESC' });
  const [searchQuery, setSearchQuery] = useState('');
  const [deleteOpen, setDeleteOpen] = useState(false);
  const [selectedReading, setSelectedReading] = useState<EnergyReading | null>(null);
  const [readingType, setReadingType] = useState<string>('');
  const [qualityStatus, setQualityStatus] = useState<string>('');

  const effective: EnergyReadingFilters = { ...filters, searchKeyword: searchQuery || undefined, readingType: readingType ? (readingType as ReadingType) : undefined, qualityStatus: qualityStatus ? (qualityStatus as QualityStatus) : undefined };
  const { data, isLoading, error, refetch } = useEnergyReadings(effective);
  const deleteMutation = useDeleteEnergyReading();
  const activeFilters = [readingType, qualityStatus].filter(Boolean).length;

  const handleSearch = useCallback((value: string) => {
    setSearchQuery(value);
    setFilters((prev) => ({ ...prev, page: 0 }));
  }, []);

  const handlePagination = useCallback((model: GridPaginationModel) => {
    setFilters((prev) => ({ ...prev, page: model.page, size: model.pageSize }));
  }, []);

  const handleSort = useCallback((model: GridSortModel) => {
    if (model.length > 0) {
      setFilters((prev) => ({ ...prev, sortBy: model[0].field, sortDirection: model[0].sort === 'asc' ? 'ASC' : 'DESC' }));
    }
  }, []);

  const handleDelete = useCallback(async () => {
    if (!selectedReading) return;
    try {
      await deleteMutation.mutateAsync(selectedReading.id);
      toast.success('Reading deleted');
      setDeleteOpen(false);
    } catch { toast.error('Failed to delete reading'); }
  }, [selectedReading, deleteMutation]);

  const columns: GridColDef[] = [
    { field: 'id', headerName: 'ID', width: 70 },
    { field: 'meterNumber', headerName: 'Meter', width: 120 },
    { field: 'readingValue', headerName: 'Reading', width: 110 },
    { field: 'readingUnit', headerName: 'Unit', width: 80 },
    { field: 'readingTimestamp', headerName: 'Timestamp', width: 170, valueFormatter: (value) => value ? dayjs(value).format('MMM D, YYYY h:mm A') : '-' },
    { field: 'readingType', headerName: 'Type', width: 110, renderCell: (params) => <StatusChip status={params.value} /> },
    { field: 'qualityStatus', headerName: 'Quality', width: 110, renderCell: (params) => <StatusChip status={params.value} /> },
    {
      field: 'actions', headerName: 'Actions', width: 120, sortable: false,
      renderCell: (params) => (
        <Box sx={{ display: 'flex', gap: 0.5 }}>
          <Button size="small" variant="outlined" onClick={(e) => { e.stopPropagation(); navigate(`/energy/${params.row.id}`); }} sx={{ borderRadius: 1.5, minWidth: 0, px: 1 }}>View</Button>
          <Button size="small" color="error" variant="text" onClick={(e) => { e.stopPropagation(); setSelectedReading(params.row as EnergyReading); setDeleteOpen(true); }} sx={{ borderRadius: 1.5, minWidth: 0, px: 1 }}>Delete</Button>
        </Box>
      ),
    },
  ];

  return (
    <Box>
      <PageHeader
        title="Energy Readings"
        description="Track and analyze energy consumption data."
        action={<Button variant="contained" startIcon={<AddIcon />} onClick={() => navigate(ROUTES.ENERGY_UPLOAD)} sx={{ borderRadius: 2 }}>Upload Reading</Button>}
      />
      <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, mb: 3 }}>
        <SearchBar value={searchQuery} onChange={handleSearch} placeholder="Search readings by meter, remarks..." />
        <FilterPanel activeFilterCount={activeFilters} onClear={() => { setReadingType(''); setQualityStatus(''); }}>
          <FormControl size="small" sx={{ minWidth: 150 }}>
            <InputLabel>Reading Type</InputLabel>
            <Select value={readingType} label="Reading Type" onChange={(e) => setReadingType(e.target.value)} sx={{ borderRadius: 2 }}>
              <MenuItem value="">All</MenuItem>
              {['MANUAL', 'AUTOMATIC', 'ESTIMATED', 'REMOTE'].map((t) => <MenuItem key={t} value={t}>{t}</MenuItem>)}
            </Select>
          </FormControl>
          <FormControl size="small" sx={{ minWidth: 150 }}>
            <InputLabel>Quality</InputLabel>
            <Select value={qualityStatus} label="Quality" onChange={(e) => setQualityStatus(e.target.value)} sx={{ borderRadius: 2 }}>
              <MenuItem value="">All</MenuItem>
              {['VALID', 'SUSPICIOUS', 'ESTIMATED', 'MISSING'].map((s) => <MenuItem key={s} value={s}>{s}</MenuItem>)}
            </Select>
          </FormControl>
        </FilterPanel>
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
        onRowClick={(params) => navigate(`/energy/${params.row.id}`)}
        getRowId={(row) => (row as unknown as EnergyReading).id}
        title="Energy Readings"
      />
      <ConfirmationDialog open={deleteOpen} title="Delete Reading" message={`Delete energy reading #${selectedReading?.id}?`}
        confirmLabel="Delete" onConfirm={handleDelete} onCancel={() => { setDeleteOpen(false); setSelectedReading(null); }}
        isLoading={deleteMutation.isPending} variant="delete" />
    </Box>
  );
}
