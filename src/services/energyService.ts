import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { energyApi } from '@/api';
import type { CreateEnergyReadingRequest, UpdateEnergyReadingRequest, EnergyReadingFilters } from '@/types';
import { QUERY_KEYS } from '@/constants';

export function useEnergyReadings(filters: EnergyReadingFilters = {}) {
  return useQuery({
    queryKey: [QUERY_KEYS.ENERGY_READINGS, filters],
    queryFn: () => energyApi.getAll(filters),
  });
}

export function useEnergyReading(id: number | undefined) {
  return useQuery({
    queryKey: [QUERY_KEYS.ENERGY_READINGS, id],
    queryFn: () => energyApi.getById(id!),
    enabled: !!id,
  });
}

export function useCreateEnergyReading() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (data: CreateEnergyReadingRequest) => energyApi.create(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEYS.ENERGY_READINGS] });
    },
  });
}

export function useUpdateEnergyReading() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ id, data }: { id: number; data: UpdateEnergyReadingRequest }) =>
      energyApi.update(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEYS.ENERGY_READINGS] });
    },
  });
}

export function useDeleteEnergyReading() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id: number) => energyApi.delete(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEYS.ENERGY_READINGS] });
    },
  });
}

export function useEnergyReadingHistory(meterId: number | undefined) {
  return useQuery({
    queryKey: [QUERY_KEYS.ENERGY_READINGS, 'history', meterId],
    queryFn: () => energyApi.getHistoryByMeter(meterId!),
    enabled: !!meterId,
  });
}

export function useConsumption(meterId: number | undefined) {
  return useQuery({
    queryKey: [QUERY_KEYS.ENERGY_READINGS, 'consumption', meterId],
    queryFn: () => energyApi.getConsumption(meterId!),
    enabled: !!meterId,
  });
}

export function useConsumptionHistory(meterId: number | undefined) {
  return useQuery({
    queryKey: [QUERY_KEYS.ENERGY_READINGS, 'consumption-history', meterId],
    queryFn: () => energyApi.getConsumptionHistory(meterId!),
    enabled: !!meterId,
  });
}
