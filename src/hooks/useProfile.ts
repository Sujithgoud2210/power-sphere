import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { apiClient } from '../services/apiClient';
import type { Profile, ProfileUpdatePayload } from '../types/profile';

async function fetchProfile(): Promise<Profile> {
  return apiClient.get<Profile>('/profile');
}

async function updateProfile(payload: ProfileUpdatePayload): Promise<Profile> {
  return apiClient.put<Profile>('/profile', payload);
}

export function useProfile() {
  return useQuery<Profile>({
    queryKey: ['profile'],
    queryFn: fetchProfile,
    staleTime: 60_000,
  });
}

export function useUpdateProfile() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: updateProfile,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['profile'] });
    },
  });
}
