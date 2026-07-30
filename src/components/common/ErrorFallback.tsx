import { Box, Button, Typography, Divider } from '@mui/material';
import ErrorOutlineIcon from '@mui/icons-material/ErrorOutline';
import RefreshIcon from '@mui/icons-material/Refresh';
import HomeIcon from '@mui/icons-material/Home';

interface ErrorFallbackProps {
  title?: string;
  message?: string;
  error?: Error | null;
  onRetry?: () => void;
  onHome?: () => void;
  fullPage?: boolean;
}

export function ErrorFallback({
  title = 'Something went wrong',
  message = 'An unexpected error occurred. Please try again.',
  error,
  onRetry,
  onHome,
  fullPage = false,
}: ErrorFallbackProps) {
  return (
    <Box
      role="alert"
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        gap: 1.5,
        p: 4,
        textAlign: 'center',
        minHeight: fullPage ? '100vh' : 300,
        bgcolor: fullPage ? 'background.default' : 'transparent',
      }}
    >
      <Box
        sx={{
          width: 72,
          height: 72,
          borderRadius: '50%',
          bgcolor: 'error.light',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          mb: 1,
          opacity: 0.2,
        }}
      >
        <ErrorOutlineIcon sx={{ fontSize: 40, color: 'error.main' }} />
      </Box>
      <Typography variant="h5" fontWeight={700}>
        {title}
      </Typography>
      <Typography variant="body1" color="text.secondary" sx={{ maxWidth: 480 }}>
        {message}
      </Typography>
      {error && (
        <>
          <Divider sx={{ width: 80, my: 1 }} />
          <Typography
            variant="caption"
            color="text.disabled"
            sx={{
              maxWidth: 400,
              fontFamily: 'monospace',
              p: 1.5,
              bgcolor: 'action.hover',
              borderRadius: 1,
              wordBreak: 'break-word',
            }}
          >
            {error.message || error.name}
          </Typography>
        </>
      )}
      <Box sx={{ display: 'flex', gap: 1.5, mt: 1 }}>
        {onRetry && (
          <Button
            variant="contained"
            startIcon={<RefreshIcon />}
            onClick={onRetry}
            sx={{ borderRadius: 2 }}
          >
            Try Again
          </Button>
        )}
        {onHome && (
          <Button
            variant="outlined"
            startIcon={<HomeIcon />}
            onClick={onHome}
            sx={{ borderRadius: 2 }}
          >
            Go to Dashboard
          </Button>
        )}
      </Box>
    </Box>
  );
}
