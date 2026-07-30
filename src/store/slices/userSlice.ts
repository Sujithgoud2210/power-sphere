import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import type { UserState } from '@/types';
import { apiClient } from '@/api';

const initialState: UserState = {
  profile: null,
  isLoading: false,
  error: null,
};

export const fetchUserProfile = createAsyncThunk(
  'user/fetchProfile',
  async (_, { rejectWithValue }) => {
    try {
      const response = await apiClient.get('/users/profile');
      return response.data;
    } catch (error: unknown) {
      const message =
        error instanceof Error
          ? error.message
          : 'Failed to load user profile';
      return rejectWithValue(message);
    }
  },
);

const userSlice = createSlice({
  name: 'user',
  initialState,
  reducers: {
    clearUser(state) {
      state.profile = null;
      state.isLoading = false;
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchUserProfile.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(fetchUserProfile.fulfilled, (state, action) => {
        state.isLoading = false;
        state.profile = action.payload;
      })
      .addCase(fetchUserProfile.rejected, (state, action) => {
        state.isLoading = false;
        state.error = (action.payload as string) || 'Failed to load profile';
      });
  },
});

export const { clearUser } = userSlice.actions;
export default userSlice.reducer;
