import { useCallback } from 'react';
import { Box, Paper, Typography } from '@mui/material';
import { DataGrid, type GridColDef, type GridSortModel, type GridPaginationModel, type GridRowParams, type GridCallbackDetails } from '@mui/x-data-grid';
import { LoadingSkeleton } from './LoadingSkeleton';
import { EmptyState } from './EmptyState';

interface DataTableProps<T> {
  rows: T[];
  columns: GridColDef[];
  loading?: boolean;
  error?: string | null;
  onRetry?: () => void;
  totalRows?: number;
  page?: number;
  pageSize?: number;
  onPaginationModelChange?: (model: GridPaginationModel) => void;
  onSortModelChange?: (model: GridSortModel) => void;
  onRowClick?: (params: GridRowParams) => void;
  sortModel?: GridSortModel;
  title?: string;
  getRowId?: (row: T) => string | number;
  checkboxSelection?: boolean;
  density?: 'compact' | 'standard' | 'comfortable';
}

export function DataTable<T extends Record<string, unknown>>({
  rows,
  columns,
  loading = false,
  error = null,
  onRetry,
  totalRows = 0,
  page = 0,
  pageSize = 10,
  onPaginationModelChange,
  onSortModelChange,
  onRowClick,
  sortModel,
  title,
  getRowId,
  checkboxSelection = false,
  density = 'standard',
}: DataTableProps<T>) {
  const handlePaginationChange = useCallback(
    (model: GridPaginationModel, _details: GridCallbackDetails) => {
      onPaginationModelChange?.(model);
    },
    [onPaginationModelChange],
  );

  if (error) {
    return (
      <EmptyState
        title="Error loading data"
        description={error}
        actionLabel="Try Again"
        onAction={onRetry}
      />
    );
  }

  if (!loading && rows.length === 0) {
    return (
      <EmptyState
        title={title ? `No ${title.toLowerCase()} found` : 'No data found'}
        description="No records to display at the moment."
      />
    );
  }

  return (
    <Paper
      elevation={0}
      sx={{
        border: '1px solid',
        borderColor: 'divider',
        borderRadius: 2,
        overflow: 'hidden',
      }}
    >
      {title && (
        <Box sx={{ px: 2, py: 1.5, borderBottom: '1px solid', borderColor: 'divider' }}>
          <Typography variant="subtitle1" fontWeight={600}>
            {title}
          </Typography>
        </Box>
      )}
      {loading ? (
        <LoadingSkeleton rows={8} columns={columns.length} />
      ) : (
        <DataGrid
          rows={rows}
          columns={columns}
          rowCount={totalRows}
          paginationMode="server"
          sortingMode="server"
          paginationModel={{ page, pageSize }}
          onPaginationModelChange={handlePaginationChange}
          onSortModelChange={onSortModelChange}
          sortModel={sortModel}
          onRowClick={onRowClick}
          getRowId={getRowId as (row: T) => string | number}
          checkboxSelection={checkboxSelection}
          disableRowSelectionOnClick
          density={density}
          pageSizeOptions={[5, 10, 25, 50]}
          sx={{
            border: 'none',
            '& .MuiDataGrid-cell:focus': {
              outline: 'none',
            },
            '& .MuiDataGrid-row': {
              cursor: onRowClick ? 'pointer' : 'default',
            },
            '& .MuiDataGrid-columnHeaders': {
              bgcolor: 'action.hover',
              borderRadius: 0,
            },
          }}
        />
      )}
    </Paper>
  );
}
