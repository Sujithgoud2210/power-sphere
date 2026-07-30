import { config } from '@/config';

export const tokenUtils = {
  getAccessToken(): string | null {
    return localStorage.getItem(config.auth.tokenKey);
  },

  setAccessToken(token: string): void {
    localStorage.setItem(config.auth.tokenKey, token);
  },

  removeAccessToken(): void {
    localStorage.removeItem(config.auth.tokenKey);
  },

  getRefreshToken(): string | null {
    return localStorage.getItem(config.auth.refreshTokenKey);
  },

  setRefreshToken(token: string): void {
    localStorage.setItem(config.auth.refreshTokenKey, token);
  },

  removeRefreshToken(): void {
    localStorage.removeItem(config.auth.refreshTokenKey);
  },

  getRememberMe(): boolean {
    return localStorage.getItem(config.auth.rememberMeKey) === 'true';
  },

  setRememberMe(value: boolean): void {
    localStorage.setItem(config.auth.rememberMeKey, String(value));
  },

  clearAll(): void {
    this.removeAccessToken();
    this.removeRefreshToken();
    localStorage.removeItem(config.auth.rememberMeKey);
  },
};
