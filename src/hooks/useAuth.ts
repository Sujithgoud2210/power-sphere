import { useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from './useAppStore';
import { loginThunk, logoutThunk, clearAuthError } from '@/store/slices/authSlice';
import { clearUser } from '@/store/slices/userSlice';
import { ROUTES } from '@/constants';
import type { LoginRequest } from '@/types';

export function useAuth() {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { isAuthenticated, isLoading, error } = useAppSelector(
    (state) => state.auth,
  );

  const login = useCallback(
    async (credentials: LoginRequest) => {
      const result = await dispatch(loginThunk(credentials));
      if (loginThunk.fulfilled.match(result)) {
        navigate(ROUTES.DASHBOARD, { replace: true });
      }
      return result;
    },
    [dispatch, navigate],
  );

  const logout = useCallback(async () => {
    await dispatch(logoutThunk());
    dispatch(clearUser());
    navigate(ROUTES.LOGIN, { replace: true });
  }, [dispatch, navigate]);

  const clearError = useCallback(() => {
    dispatch(clearAuthError());
  }, [dispatch]);

  return {
    isAuthenticated,
    isLoading,
    error,
    login,
    logout,
    clearError,
  };
}
