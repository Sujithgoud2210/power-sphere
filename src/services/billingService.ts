import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { billingApi } from '@/api';
import type { GenerateBillRequest, BillFilters } from '@/types';
import { QUERY_KEYS } from '@/constants';

export function useBills(filters: BillFilters = {}) {
  return useQuery({
    queryKey: [QUERY_KEYS.BILLS, filters],
    queryFn: () => billingApi.getAll(filters),
  });
}

export function useBill(id: number | undefined) {
  return useQuery({
    queryKey: [QUERY_KEYS.BILLS, id],
    queryFn: () => billingApi.getById(id!),
    enabled: !!id,
  });
}

export function useGenerateBill() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (data: GenerateBillRequest) => billingApi.generate(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEYS.BILLS] });
    },
  });
}

export function useCancelBill() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ id, reason }: { id: number; reason?: string }) =>
      billingApi.cancel(id, reason),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEYS.BILLS] });
    },
  });
}

export function useSearchBills(filters: BillFilters) {
  return useQuery({
    queryKey: [QUERY_KEYS.BILLS, 'search', filters],
    queryFn: () => billingApi.search(filters),
  });
}
