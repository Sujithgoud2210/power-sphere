import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { userManagementApi } from '@/api';
import type { CreateManagedUserRequest, UpdateManagedUserRequest, UserFilters } from '@/types';
import { QUERY_KEYS } from '@/constants';

export function useManagedUsers(filters: UserFilters = {}) {
  return useQuery({
    queryKey: [QUERY_KEYS.USERS, filters],
    queryFn: () => userManagementApi.getAll(filters),
  });
}

export function useManagedUser(id: number | undefined) {
  return useQuery({
    queryKey: [QUERY_KEYS.USERS, id],
    queryFn: () => userManagementApi.getById(id!),
    enabled: !!id,
  });
}

export function useCreateManagedUser() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (data: CreateManagedUserRequest) => userManagementApi.create(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEYS.USERS] });
    },
  });
}

export function useUpdateManagedUser() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ id, data }: { id: number; data: UpdateManagedUserRequest }) =>
      userManagementApi.update(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEYS.USERS] });
    },
  });
}

export function useDeleteManagedUser() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id: number) => userManagementApi.delete(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEYS.USERS] });
    },
  });
}

export function useAssignOrganization() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ userId, organizationId }: { userId: number; organizationId: number }) =>
      userManagementApi.assignOrganization(userId, organizationId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEYS.USERS] });
    },
  });
}

export function useAssignRole() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ userId, role }: { userId: number; role: string }) =>
      userManagementApi.assignRole(userId, role),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEYS.USERS] });
    },
  });
}
