import { Box, Typography } from '@mui/material';
import InboxIcon from '@mui/icons-material/Inbox';
import SearchOffIcon from '@mui/icons-material/SearchOff';
import ErrorOutlineIcon from '@mui/icons-material/ErrorOutline';

interface NoDataIllustrationProps {
  title?: string;
  description?: string;
  variant?: 'empty' | 'search' | 'error';
}

const ILLUSTRATIONS = {
  empty: {
    icon: InboxIcon,
    defaultTitle: 'No data available',
    defaultDescription: 'There are no records to display at the moment.',
  },
  search: {
    icon: SearchOffIcon,
    defaultTitle: 'No results found',
    defaultDescription: 'Try adjusting your search or filter criteria.',
  },
  error: {
    icon: ErrorOutlineIcon,
    defaultTitle: 'Something went wrong',
    defaultDescription: 'An error occurred while loading data. Please try again.',
  },
} as const;

export function NoDataIllustration({
  title,
  description,
  variant = 'empty',
}: NoDataIllustrationProps) {
  const config = ILLUSTRATIONS[variant];
  const Icon = config.icon;

  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        py: 8,
        px: 3,
        textAlign: 'center',
        minHeight: 300,
      }}
    >
      <Box
        sx={{
          width: 80,
          height: 80,
          borderRadius: '50%',
          bgcolor: 'action.hover',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          mb: 2,
        }}
      >
        <Icon
          sx={{
            fontSize: 40,
            color: variant === 'error' ? 'error.main' : 'text.disabled',
          }}
        />
      </Box>
      <Typography variant="h6" fontWeight={600} sx={{ mb: 0.5 }}>
        {title ?? config.defaultTitle}
      </Typography>
      <Typography variant="body2" color="text.secondary" sx={{ maxWidth: 400 }}>
        {description ?? config.defaultDescription}
      </Typography>
    </Box>
  );
}
