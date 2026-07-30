import { Box, Button, Typography } from '@mui/material';
import InboxIcon from '@mui/icons-material/Inbox';
import ErrorOutlineIcon from '@mui/icons-material/ErrorOutline';
import SearchOffIcon from '@mui/icons-material/SearchOff';

interface EmptyStateProps {
  title: string;
  description?: string;
  actionLabel?: string;
  onAction?: () => void;
  variant?: 'empty' | 'error' | 'search';
}

export function EmptyState({
  title,
  description,
  actionLabel,
  onAction,
  variant = 'empty',
}: EmptyStateProps) {
  const iconMap = {
    empty: <InboxIcon sx={{ fontSize: 64, color: 'text.disabled' }} />,
    error: <ErrorOutlineIcon sx={{ fontSize: 64, color: 'error.main' }} />,
    search: <SearchOffIcon sx={{ fontSize: 64, color: 'text.disabled' }} />,
  };

  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        py: 8,
        px: 4,
        textAlign: 'center',
      }}
    >
      {iconMap[variant]}
      <Typography variant="h6" fontWeight={600} sx={{ mt: 2, mb: 0.5 }}>
        {title}
      </Typography>
      {description && (
        <Typography variant="body2" color="text.secondary" sx={{ maxWidth: 400 }}>
          {description}
        </Typography>
      )}
      {actionLabel && onAction && (
        <Button variant="contained" onClick={onAction} sx={{ mt: 2, borderRadius: 2 }}>
          {actionLabel}
        </Button>
      )}
    </Box>
  );
}
