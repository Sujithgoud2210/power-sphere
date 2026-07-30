import { apiClient } from './axios';
import type { LoginRequest, LoginResponse, RegisterRequest, RegisterResponse } from '@/types';

export const authApi = {
  login(data: LoginRequest): Promise<LoginResponse> {
    return apiClient.post('/auth/login', data).then((res) => res.data);
  },

  register(data: RegisterRequest): Promise<RegisterResponse> {
    return apiClient.post('/auth/register', data).then((res) => res.data);
  },

  refreshToken(refreshToken: string): Promise<LoginResponse> {
    return apiClient
      .post('/auth/refresh', { refreshToken })
      .then((res) => res.data);
  },

  logout(): Promise<void> {
    return apiClient.post('/auth/logout').then(() => undefined);
  },

  getProfile(): Promise<unknown> {
    return apiClient.get('/users/profile').then((res) => res.data);
  },
};
