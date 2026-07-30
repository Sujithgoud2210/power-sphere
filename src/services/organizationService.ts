import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { organizationApi } from '@/api';
import type { CreateOrganizationRequest, UpdateOrganizationRequest, OrganizationFilters } from '@/types';
import { QUERY_KEYS } from '@/constants';

export function useOrganizations(filters: OrganizationFilters = {}) {
  return useQuery({
    queryKey: [QUERY_KEYS.ORGANIZATIONS, filters],
    queryFn: () => organizationApi.getAll(filters),
  });
}

export function useOrganization(id: number | undefined) {
  return useQuery({
    queryKey: [QUERY_KEYS.ORGANIZATIONS, id],
    queryFn: () => organizationApi.getById(id!),
    enabled: !!id,
  });
}

export function useCreateOrganization() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (data: CreateOrganizationRequest) => organizationApi.create(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEYS.ORGANIZATIONS] });
    },
  });
}

export function useUpdateOrganization() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ id, data }: { id: number; data: UpdateOrganizationRequest }) =>
      organizationApi.update(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEYS.ORGANIZATIONS] });
    },
  });
}

export function useDeleteOrganization() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id: number) => organizationApi.delete(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEYS.ORGANIZATIONS] });
    },
  });
}
