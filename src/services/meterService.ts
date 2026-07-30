import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { meterApi } from '@/api';
import type { CreateMeterRequest, UpdateMeterRequest, AssignMeterRequest, MeterFilters } from '@/types';
import { QUERY_KEYS } from '@/constants';

export function useMeters(filters: MeterFilters = {}) {
  return useQuery({
    queryKey: [QUERY_KEYS.METERS, filters],
    queryFn: () => meterApi.getAll(filters),
  });
}

export function useMeter(id: string | undefined) {
  return useQuery({
    queryKey: [QUERY_KEYS.METERS, id],
    queryFn: () => meterApi.getById(id!),
    enabled: !!id,
  });
}

export function useCreateMeter() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (data: CreateMeterRequest) => meterApi.create(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEYS.METERS] });
    },
  });
}

export function useUpdateMeter() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ id, data }: { id: string; data: UpdateMeterRequest }) =>
      meterApi.update(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEYS.METERS] });
    },
  });
}

export function useDeleteMeter() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id: string) => meterApi.delete(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEYS.METERS] });
    },
  });
}

export function useActivateMeter() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id: string) => meterApi.activate(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEYS.METERS] });
    },
  });
}

export function useDeactivateMeter() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id: string) => meterApi.deactivate(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEYS.METERS] });
    },
  });
}

export function useAssignMeter() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ id, data }: { id: string; data: AssignMeterRequest }) =>
      meterApi.assign(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEYS.METERS] });
    },
  });
}
