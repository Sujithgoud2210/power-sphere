import { Box, Button, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import ErrorOutlineIcon from '@mui/icons-material/ErrorOutline';
import { ROUTES } from '@/constants';

export function NotFoundPage() {
  const navigate = useNavigate();

  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        minHeight: '100vh',
        gap: 2,
        p: 4,
        textAlign: 'center',
        bgcolor: 'background.default',
      }}
    >
      <ErrorOutlineIcon sx={{ fontSize: 80, color: 'text.disabled' }} />
      <Typography variant="h2" fontWeight={700} color="text.primary">
        404
      </Typography>
      <Typography variant="h5" color="text.secondary" gutterBottom>
        Page Not Found
      </Typography>
      <Typography variant="body1" color="text.secondary" sx={{ maxWidth: 450 }}>
        The page you are looking for does not exist or has been moved. Please
        check the URL or navigate back to the dashboard.
      </Typography>
      <Button
        variant="contained"
        onClick={() => navigate(ROUTES.DASHBOARD)}
        sx={{ mt: 2, borderRadius: 2, px: 4, py: 1.25 }}
      >
        Back to Dashboard
      </Button>
    </Box>
  );
}
