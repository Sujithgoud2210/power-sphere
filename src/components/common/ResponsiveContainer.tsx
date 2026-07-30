import { Box } from '@mui/material';
import type { ReactNode } from 'react';

interface ResponsiveContainerProps {
  children: ReactNode;
  maxWidth?: 'xs' | 'sm' | 'md' | 'lg' | 'xl';
  disableGutters?: boolean;
}

const MAX_WIDTH_VALUES: Record<string, number> = {
  xs: 444,
  sm: 600,
  md: 900,
  lg: 1200,
  xl: 1536,
};

export function ResponsiveContainer({
  children,
  maxWidth,
  disableGutters = false,
}: ResponsiveContainerProps) {
  return (
    <Box
      sx={{
        width: '100%',
        mx: 'auto',
        ...(maxWidth ? { maxWidth: MAX_WIDTH_VALUES[maxWidth] } : {}),
        ...(!disableGutters ? { px: { xs: 2, sm: 3, md: 4 } } : {}),
      }}
    >
      {children}
    </Box>
  );
}
