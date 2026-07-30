import { useCallback } from 'react';
import { useAppDispatch, useAppSelector } from './useAppStore';
import { toggleTheme, setTheme } from '@/store/slices/themeSlice';
import type { ThemeMode } from '@/store/slices/themeSlice';

export function useThemeMode() {
  const dispatch = useAppDispatch();
  const mode = useAppSelector((state) => state.theme.mode);

  const toggle = useCallback(() => {
    dispatch(toggleTheme());
  }, [dispatch]);

  const setMode = useCallback(
    (newMode: ThemeMode) => {
      dispatch(setTheme(newMode));
    },
    [dispatch],
  );

  return { mode, toggle, setMode, isDark: mode === 'dark' };
}
