import { Box, Skeleton, Paper } from '@mui/material';

interface LoadingSkeletonProps {
  rows?: number;
  columns?: number;
  variant?: 'table' | 'card' | 'detail';
}

export function LoadingSkeleton({ rows = 5, columns = 5, variant = 'table' }: LoadingSkeletonProps) {
  if (variant === 'card') {
    return (
      <Box sx={{ display: 'grid', gap: 2, gridTemplateColumns: { xs: '1fr', sm: '1fr 1fr', md: '1fr 1fr 1fr' } }}>
        {Array.from({ length: rows }).map((_, i) => (
          <Paper key={i} elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider', borderRadius: 2 }}>
            <Skeleton variant="circular" width={40} height={40} sx={{ mb: 2 }} />
            <Skeleton variant="text" width="60%" sx={{ mb: 1 }} />
            <Skeleton variant="text" width="40%" />
            <Box sx={{ mt: 2, display: 'flex', gap: 1 }}>
              <Skeleton variant="rounded" width={80} height={28} />
              <Skeleton variant="rounded" width={60} height={28} />
            </Box>
          </Paper>
        ))}
      </Box>
    );
  }

  if (variant === 'detail') {
    return (
      <Paper elevation={0} sx={{ p: 4, border: '1px solid', borderColor: 'divider', borderRadius: 2 }}>
        <Skeleton variant="text" width="40%" height={32} sx={{ mb: 3 }} />
        <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', md: '1fr 1fr' }, gap: 3 }}>
          {Array.from({ length: 6 }).map((_, i) => (
            <Box key={i}>
              <Skeleton variant="text" width="30%" height={16} sx={{ mb: 0.5 }} />
              <Skeleton variant="text" width="70%" height={24} />
            </Box>
          ))}
        </Box>
      </Paper>
    );
  }

  // Table variant
  return (
    <Box sx={{ p: 2 }}>
      {Array.from({ length: rows }).map((_, i) => (
        <Box key={i} sx={{ display: 'flex', gap: 2, py: 1.5, borderBottom: i < rows - 1 ? '1px solid' : 'none', borderColor: 'divider' }}>
          {Array.from({ length: Math.min(columns, 6) }).map((_, j) => (
            <Skeleton
              key={j}
              variant="text"
              width={j === 0 ? '25%' : j === 1 ? '20%' : '15%'}
              height={20}
            />
          ))}
        </Box>
      ))}
    </Box>
  );
}
