import { useState, useCallback } from 'react';
import { Box, Button, FormControl, InputLabel, MenuItem, Select } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import { PageHeader, DataTable, SearchBar, FilterPanel, StatusChip, ConfirmationDialog } from '@/components/common';
import { useBills, useCancelBill } from '@/services';
import { ROUTES } from '@/constants';
import type { Bill, BillFilters, BillStatus } from '@/types';
import type { GridColDef, GridPaginationModel, GridSortModel } from '@mui/x-data-grid';
import AddIcon from '@mui/icons-material/Add';
import dayjs from 'dayjs';

export function BillListPage() {
  const navigate = useNavigate();
  const [filters, setFilters] = useState<BillFilters>({ page: 0, size: 20, sortBy: 'generatedDate', sortDirection: 'DESC' });
  const [searchQuery, setSearchQuery] = useState('');
  const [statusFilter, setStatusFilter] = useState<string>('');
  const [cancelOpen, setCancelOpen] = useState(false);
  const [selectedBill, setSelectedBill] = useState<Bill | null>(null);

  const effective: BillFilters = { ...filters, query: searchQuery || undefined, status: statusFilter ? (statusFilter as BillStatus) : undefined };
  const { data, isLoading, error, refetch } = useBills(effective);
  const cancelMutation = useCancelBill();

  const activeFilters = [statusFilter].filter(Boolean).length;

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

  const handleCancel = useCallback(async () => {
    if (!selectedBill) return;
    try {
      await cancelMutation.mutateAsync({ id: selectedBill.id });
      toast.success('Bill cancelled');
      setCancelOpen(false);
      setSelectedBill(null);
    } catch { toast.error('Failed to cancel bill'); }
  }, [selectedBill, cancelMutation]);

  const columns: GridColDef[] = [
    { field: 'billNumber', headerName: 'Bill #', width: 130 },
    { field: 'meterNumber', headerName: 'Meter', width: 110 },
    { field: 'customerName', headerName: 'Customer', flex: 1, minWidth: 150 },
    { field: 'organizationName', headerName: 'Organization', flex: 1, minWidth: 130 },
    {
      field: 'totalAmount', headerName: 'Amount', width: 120,
      valueFormatter: (value) => `$${Number(value).toFixed(2)}`,
    },
    { field: 'status', headerName: 'Status', width: 120, renderCell: (params) => <StatusChip status={params.value} /> },
    {
      field: 'generatedDate', headerName: 'Generated', width: 140,
      valueFormatter: (value) => value ? dayjs(value).format('MMM D, YYYY') : '-',
    },
    {
      field: 'actions', headerName: 'Actions', width: 150, sortable: false,
      renderCell: (params) => (
        <Box sx={{ display: 'flex', gap: 0.5 }}>
          <Button size="small" variant="outlined" onClick={(e) => { e.stopPropagation(); navigate(`/billing/${params.row.id}`); }} sx={{ borderRadius: 1.5, minWidth: 0, px: 1 }}>View</Button>
          {(params.row.status === 'GENERATED' || params.row.status === 'SENT') && (
            <Button size="small" color="error" variant="text" onClick={(e) => { e.stopPropagation(); setSelectedBill(params.row as Bill); setCancelOpen(true); }} sx={{ borderRadius: 1.5, minWidth: 0, px: 1 }}>Cancel</Button>
          )}
        </Box>
      ),
    },
  ];

  return (
    <Box>
      <PageHeader
        title="Billing"
        description="Manage invoices, tariffs, and billing history."
        action={<Button variant="contained" startIcon={<AddIcon />} onClick={() => navigate(ROUTES.BILLING_GENERATE)} sx={{ borderRadius: 2 }}>Generate Bill</Button>}
      />
      <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, mb: 3 }}>
        <SearchBar value={searchQuery} onChange={handleSearch} placeholder="Search by bill number, customer, organization..." />
        <FilterPanel activeFilterCount={activeFilters} onClear={() => setStatusFilter('')}>
          <FormControl size="small" sx={{ minWidth: 150 }}>
            <InputLabel>Status</InputLabel>
            <Select value={statusFilter} label="Status" onChange={(e) => setStatusFilter(e.target.value)} sx={{ borderRadius: 2 }}>
              <MenuItem value="">All</MenuItem>
              {['DRAFT', 'GENERATED', 'SENT', 'PAID', 'OVERDUE', 'CANCELLED', 'REFUNDED'].map((s) => (
                <MenuItem key={s} value={s}>{s.charAt(0) + s.slice(1).toLowerCase()}</MenuItem>
              ))}
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
        onRowClick={(params) => navigate(`/billing/${params.row.id}`)}
        getRowId={(row) => (row as unknown as Bill).id}
        title="All Bills"
      />
      <ConfirmationDialog
        open={cancelOpen}
        title="Cancel Bill"
        message={`Are you sure you want to cancel bill "${selectedBill?.billNumber}"?`}
        confirmLabel="Cancel Bill"
        onConfirm={handleCancel}
        onCancel={() => { setCancelOpen(false); setSelectedBill(null); }}
        isLoading={cancelMutation.isPending}
        variant="warning"
      />
    </Box>
  );
}
