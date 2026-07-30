import { useMutation } from '@tanstack/react-query';
import { authApi } from '@/api';
import type { LoginRequest } from '@/types';

export function useLoginMutation() {
  return useMutation({
    mutationFn: (data: LoginRequest) => authApi.login(data),
  });
}

export function useLogoutMutation() {
  return useMutation({
    mutationFn: () => authApi.logout(),
  });
}
