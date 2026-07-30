import { Box, Typography, Container } from '@mui/material';
import { config } from '@/config';

export function Footer() {
  return (
    <Box
      component="footer"
      sx={{
        py: 2,
        px: 3,
        borderTop: '1px solid',
        borderColor: 'divider',
        bgcolor: 'background.paper',
      }}
    >
      <Container maxWidth={false}>
        <Box
          sx={{
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            flexWrap: 'wrap',
            gap: 1,
          }}
        >
          <Typography variant="body2" color="text.secondary">
            &copy; {new Date().getFullYear()} {config.app.company}. All rights reserved.
          </Typography>
          <Typography variant="body2" color="text.secondary">
            v{config.app.version}
          </Typography>
        </Box>
      </Container>
    </Box>
  );
}
