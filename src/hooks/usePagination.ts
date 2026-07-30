import { useState, useCallback, useMemo } from 'react';
import type { GridPaginationModel, GridSortModel } from '@mui/x-data-grid';

interface PaginationState {
  page: number;
  pageSize: number;
  sortModel: GridSortModel;
}

interface UsePaginationOptions {
  defaultPageSize?: number;
  storageKey?: string;
}

export function usePagination(options: UsePaginationOptions = {}) {
  const { defaultPageSize = 10, storageKey } = options;

  const [paginationModel, setPaginationModel] = useState<GridPaginationModel>(() => {
    if (storageKey) {
      try {
        const stored = localStorage.getItem(storageKey);
        if (stored) {
          const parsed = JSON.parse(stored) as PaginationState;
          return { page: parsed.page ?? 0, pageSize: parsed.pageSize ?? defaultPageSize };
        }
      } catch {
        // ignore parse errors
      }
    }
    return { page: 0, pageSize: defaultPageSize };
  });

  const [sortModel, setSortModel] = useState<GridSortModel>(() => {
    if (storageKey) {
      try {
        const stored = localStorage.getItem(storageKey);
        if ( stored) {
          const parsed = JSON.parse(stored) as PaginationState;
          return parsed.sortModel ?? [];
        }
      } catch {
        // ignore parse errors
      }
    }
    return [];
  });

  const persistState = useCallback(
    (model: GridPaginationModel, sort: GridSortModel) => {
      if (storageKey) {
        try {
          localStorage.setItem(
            storageKey,
            JSON.stringify({ page: model.page, pageSize: model.pageSize, sortModel: sort } satisfies PaginationState),
          );
        } catch {
          // ignore storage errors
        }
      }
    },
    [storageKey],
  );

  const handlePaginationModelChange = useCallback(
    (model: GridPaginationModel) => {
      setPaginationModel(model);
      persistState(model, sortModel);
    },
    [persistState, sortModel],
  );

  const handleSortModelChange = useCallback(
    (newSortModel: GridSortModel) => {
      setSortModel(newSortModel);
      persistState(paginationModel, newSortModel);
    },
    [persistState, paginationModel],
  );

  const resetPagination = useCallback(() => {
    const model = { page: 0, pageSize: defaultPageSize };
    setPaginationModel(model);
    setSortModel([]);
    if (storageKey) {
      try {
        localStorage.removeItem(storageKey);
      } catch {
        // ignore
      }
    }
  }, [defaultPageSize, storageKey]);

  return useMemo(
    () => ({
      paginationModel,
      sortModel,
      handlePaginationModelChange,
      handleSortModelChange,
      resetPagination,
      page: paginationModel.page,
      pageSize: paginationModel.pageSize,
    }),
    [paginationModel, sortModel, handlePaginationModelChange, handleSortModelChange, resetPagination],
  );
}
