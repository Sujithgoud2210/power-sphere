import { createSlice, createAsyncThunk, type PayloadAction } from '@reduxjs/toolkit';
import type { AuthState, LoginRequest, RegisterRequest } from '@/types';
import { authApi } from '@/api';
import { tokenUtils } from '@/utils';

const initialState: AuthState = {
  isAuthenticated: !!tokenUtils.getAccessToken(),
  accessToken: tokenUtils.getAccessToken(),
  refreshToken: tokenUtils.getRefreshToken(),
  tokenType: 'Bearer',
  rememberMe: tokenUtils.getRememberMe(),
  isLoading: false,
  error: null,
};

export const loginThunk = createAsyncThunk(
  'auth/login',
  async (credentials: LoginRequest, { rejectWithValue }) => {
    try {
      const response = await authApi.login(credentials);
      tokenUtils.setAccessToken(response.accessToken);
      tokenUtils.setRefreshToken(response.refreshToken);
      if (credentials.rememberMe) {
        tokenUtils.setRememberMe(true);
      }
      return response;
    } catch (error: unknown) {
      const message =
        error instanceof Error ? error.message : 'Login failed. Please try again.';
      return rejectWithValue(message);
    }
  },
);

export const registerThunk = createAsyncThunk(
  'auth/register',
  async (data: RegisterRequest, { rejectWithValue }) => {
    try {
      const response = await authApi.register(data);
      return response;
    } catch (error: unknown) {
      const message =
        error instanceof Error
          ? error.message
          : 'Registration failed. Please try again.';
      return rejectWithValue(message);
    }
  },
);

export const logoutThunk = createAsyncThunk(
  'auth/logout',
  async () => {
    try {
      await authApi.logout();
    } catch {
      // Even if the API call fails, clear tokens locally
    } finally {
      tokenUtils.clearAll();
    }
  },
);

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    setCredentials(
      state,
      action: PayloadAction<{
        accessToken: string;
        refreshToken: string;
      }>,
    ) {
      state.accessToken = action.payload.accessToken;
      state.refreshToken = action.payload.refreshToken;
      state.isAuthenticated = true;
    },
    clearCredentials(state) {
      state.accessToken = null;
      state.refreshToken = null;
      state.isAuthenticated = false;
      state.error = null;
    },
    clearAuthError(state) {
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(loginThunk.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(loginThunk.fulfilled, (state, action) => {
        state.isLoading = false;
        state.isAuthenticated = true;
        state.accessToken = action.payload.accessToken;
        state.refreshToken = action.payload.refreshToken;
        state.tokenType = action.payload.tokenType;
      })
      .addCase(loginThunk.rejected, (state, action) => {
        state.isLoading = false;
        state.error = (action.payload as string) || 'Login failed';
      })
      .addCase(registerThunk.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(registerThunk.fulfilled, (state) => {
        state.isLoading = false;
        state.error = null;
      })
      .addCase(registerThunk.rejected, (state, action) => {
        state.isLoading = false;
        state.error = (action.payload as string) || 'Registration failed';
      })
      .addCase(logoutThunk.fulfilled, (state) => {
        state.isAuthenticated = false;
        state.accessToken = null;
        state.refreshToken = null;
        state.tokenType = null;
        state.isLoading = false;
        state.error = null;
      });
  },
});

export const { setCredentials, clearCredentials, clearAuthError } =
  authSlice.actions;
export default authSlice.reducer;
