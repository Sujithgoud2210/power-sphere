import { useState, useCallback } from 'react';
import { Outlet } from 'react-router-dom';
import { Box, useMediaQuery, useTheme } from '@mui/material';
import { Sidebar } from './Sidebar';
import { Header } from './Header';
import { Footer } from './Footer';
import { OfflineBanner } from '@/components/common';
import { DRAWER_WIDTH } from '@/constants';

export function AppLayout() {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('md'));
  const isTablet = useMediaQuery(theme.breakpoints.down('lg'));
  const [mobileOpen, setMobileOpen] = useState(false);

  const handleToggleSidebar = useCallback(() => {
    setMobileOpen((prev) => !prev);
  }, []);

  const handleSidebarClose = useCallback(() => {
    setMobileOpen(false);
  }, []);

  const drawerWidth = isTablet && !isMobile ? 68 : DRAWER_WIDTH;

  return (
    <Box sx={{ display: 'flex', minHeight: '100vh' }}>
      <OfflineBanner />

      {/* Sidebar - permanent on desktop, temporary on mobile */}
      {!isMobile && (
        <Sidebar
          open={true}
          onClose={() => {}}
          variant="permanent"
          collapsed={isTablet}
        />
      )}
      {isMobile && (
        <Sidebar
          open={mobileOpen}
          onClose={handleSidebarClose}
          variant="temporary"
          collapsed={false}
        />
      )}

      {/* Main Content */}
      <Box
        sx={{
          flex: 1,
          display: 'flex',
          flexDirection: 'column',
          ml: { lg: `${drawerWidth}px` },
          width: { lg: `calc(100% - ${drawerWidth}px)` },
          minWidth: 0,
          transition: 'margin-left 0.2s ease, width 0.2s ease',
        }}
      >
        <Header onToggleSidebar={handleToggleSidebar} />

        <Box
          component="main"
          id="main-content"
          role="main"
          aria-label="Main content"
          tabIndex={-1}
          sx={{
            flex: 1,
            p: { xs: 1.5, sm: 2.5, md: 3, lg: 4 },
            bgcolor: 'background.default',
            overflow: 'auto',
          }}
        >
          <Outlet />
        </Box>

        <Footer />
      </Box>
    </Box>
  );
}
