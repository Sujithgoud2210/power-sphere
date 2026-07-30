import { useState, useEffect } from 'react';
import { Box, Typography, Slide, IconButton } from '@mui/material';
import WifiOffIcon from '@mui/icons-material/WifiOff';
import CloseIcon from '@mui/icons-material/Close';
import { useOnlineStatus } from '@/hooks/useOnlineStatus';

export function OfflineBanner() {
  const { isOnline, wasOffline } = useOnlineStatus();
  const [showBanner, setShowBanner] = useState(false);
  const [showReconnected, setShowReconnected] = useState(false);
  const [dismissed, setDismissed] = useState(false);

  useEffect(() => {
    if (!isOnline) {
      setShowBanner(true);
      setDismissed(false);
    } else if (wasOffline && isOnline) {
      setShowBanner(false);
      setShowReconnected(true);
      const timer = setTimeout(() => setShowReconnected(false), 4000);
      return () => clearTimeout(timer);
    }
  }, [isOnline, wasOffline]);

  const handleDismiss = () => {
    setDismissed(true);
  };

  if (dismissed) return null;

  return (
    <>
      {/* Offline Banner */}
      <Slide direction="down" in={showBanner && !isOnline} mountOnEnter unmountOnExit>
        <Box
          role="alert"
          aria-live="assertive"
          sx={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            gap: 1,
            px: 2,
            py: 1,
            bgcolor: 'warning.main',
            color: 'warning.contrastText',
            position: 'sticky',
            top: 0,
            zIndex: (t) => t.zIndex.appBar + 1,
          }}
        >
          <WifiOffIcon fontSize="small" />
          <Typography variant="body2" fontWeight={500}>
            You are offline. Some features may be unavailable.
          </Typography>
          <IconButton
            size="small"
            onClick={handleDismiss}
            aria-label="Dismiss offline notification"
            sx={{ color: 'inherit', ml: 1 }}
          >
            <CloseIcon fontSize="small" />
          </IconButton>
        </Box>
      </Slide>

      {/* Reconnected Banner */}
      <Slide direction="down" in={showReconnected} mountOnEnter unmountOnExit>
        <Box
          role="status"
          aria-live="polite"
          sx={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            gap: 1,
            px: 2,
            py: 1,
            bgcolor: 'success.main',
            color: 'success.contrastText',
            position: 'sticky',
            top: 0,
            zIndex: (t) => t.zIndex.appBar + 1,
          }}
        >
          <Typography variant="body2" fontWeight={500}>
            ✓ Back online
          </Typography>
        </Box>
      </Slide>
    </>
  );
}
