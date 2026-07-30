import { Box, Skeleton, Paper } from '@mui/material';

interface SkeletonCardProps {
  variant?: 'card' | 'list' | 'chart' | 'detail';
  count?: number;
}

export function SkeletonCard({ variant = 'card', count = 1 }: SkeletonCardProps) {
  if (variant === 'chart') {
    return (
      <Paper elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider', borderRadius: 2 }}>
        <Skeleton variant="text" width="40%" height={24} sx={{ mb: 2 }} />
        <Skeleton variant="rounded" width="100%" height={200} />
      </Paper>
    );
  }

  if (variant === 'list') {
    return (
      <Box>
        {Array.from({ length: count }).map((_, i) => (
          <Box
            key={i}
            sx={{
              display: 'flex',
              alignItems: 'center',
              gap: 2,
              py: 1.5,
              px: 2,
              borderBottom: i < count - 1 ? '1px solid' : 'none',
              borderColor: 'divider',
            }}
          >
            <Skeleton variant="circular" width={36} height={36} />
            <Box sx={{ flex: 1 }}>
              <Skeleton variant="text" width="40%" height={18} />
              <Skeleton variant="text" width="25%" height={14} />
            </Box>
            <Skeleton variant="rounded" width={80} height={28} />
          </Box>
        ))}
      </Box>
    );
  }

  if (variant === 'detail') {
    return (
      <Paper elevation={0} sx={{ p: 4, border: '1px solid', borderColor: 'divider', borderRadius: 2 }}>
        <Skeleton variant="text" width="35%" height={32} sx={{ mb: 3 }} />
        <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', md: '1fr 1fr' }, gap: 3 }}>
          {Array.from({ length: 6 }).map((_, i) => (
            <Box key={i}>
              <Skeleton variant="text" width="30%" height={14} sx={{ mb: 0.5 }} />
              <Skeleton variant="text" width="70%" height={22} />
            </Box>
          ))}
        </Box>
      </Paper>
    );
  }

  // Card variant (default)
  return (
    <Box
      sx={{
        display: 'grid',
        gap: 2,
        gridTemplateColumns: { xs: '1fr', sm: 'repeat(2, 1fr)', md: 'repeat(3, 1fr)', lg: 'repeat(4, 1fr)' },
      }}
    >
      {Array.from({ length: count }).map((_, i) => (
        <Paper key={i} elevation={0} sx={{ p: 3, border: '1px solid', borderColor: 'divider', borderRadius: 2 }}>
          <Skeleton variant="circular" width={44} height={44} sx={{ mb: 2 }} />
          <Skeleton variant="text" width="60%" height={22} sx={{ mb: 0.5 }} />
          <Skeleton variant="text" width="40%" height={16} sx={{ mb: 2 }} />
          <Box sx={{ display: 'flex', gap: 1 }}>
            <Skeleton variant="rounded" width={80} height={28} />
            <Skeleton variant="rounded" width={60} height={28} />
          </Box>
        </Paper>
      ))}
    </Box>
  );
}
