import { useState, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  AppBar,
  Avatar,
  Badge,
  Box,
  IconButton,
  Menu,
  MenuItem,
  Toolbar,
  Tooltip,
  Typography,
  useMediaQuery,
  useTheme,
  Divider,
} from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import NotificationsIcon from '@mui/icons-material/Notifications';
import DarkModeIcon from '@mui/icons-material/DarkMode';
import LightModeIcon from '@mui/icons-material/LightMode';
import SettingsIcon from '@mui/icons-material/Settings';
import PersonIcon from '@mui/icons-material/Person';
import LogoutIcon from '@mui/icons-material/Logout';
import { useAuth, useThemeMode, useAppSelector } from '@/hooks';
import { GlobalSearch } from '@/components/common';
import { ROUTES } from '@/constants';
import type { UserProfile } from '@/types';

interface HeaderProps {
  onToggleSidebar: () => void;
}

export function Header({ onToggleSidebar }: HeaderProps) {
  const theme = useTheme();
  const navigate = useNavigate();
  const isMobile = useMediaQuery(theme.breakpoints.down('md'));
  const { mode, toggle } = useThemeMode();
  const { logout } = useAuth();
  const profile = useAppSelector((state) => state.user.profile) as UserProfile | null;

  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
  const [notifAnchorEl, setNotifAnchorEl] = useState<null | HTMLElement>(null);

  const handleProfileMenu = useCallback((event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  }, []);

  const handleNotifMenu = useCallback((event: React.MouseEvent<HTMLElement>) => {
    setNotifAnchorEl(event.currentTarget);
  }, []);

  const handleClose = useCallback(() => {
    setAnchorEl(null);
    setNotifAnchorEl(null);
  }, []);

  const handleNavigate = useCallback(
    (path: string) => {
      handleClose();
      navigate(path);
    },
    [handleClose, navigate],
  );

  const handleLogout = useCallback(() => {
    handleClose();
    logout();
  }, [handleClose, logout]);

  const userInitial = profile?.firstName?.charAt(0)?.toUpperCase() ?? 'U';
  const userName = profile ? `${profile.firstName} ${profile.lastName}` : 'User';
  const userEmail = profile?.email ?? 'user@powersphere.com';

  return (
    <AppBar
      position="sticky"
      elevation={0}
      sx={{
        bgcolor: 'background.paper',
        borderBottom: '1px solid',
        borderColor: 'divider',
        color: 'text.primary',
      }}
    >
      <Toolbar sx={{ minHeight: '64px !important', px: { xs: 1, sm: 2 } }}>
        {/* Mobile Menu Toggle */}
        {isMobile && (
          <IconButton edge="start" onClick={onToggleSidebar} aria-label="Toggle navigation menu" sx={{ mr: 1 }}>
            <MenuIcon />
          </IconButton>
        )}

        {/* Global Search */}
        <Box sx={{ flex: 1, maxWidth: { xs: '100%', sm: 400 }, mr: 2 }}>
          <GlobalSearch fullWidth={isMobile} />
        </Box>

        <Box sx={{ flexGrow: 1 }} />

        {/* Dark Mode Toggle */}
        <Tooltip title={mode === 'dark' ? 'Switch to light mode' : 'Switch to dark mode'}>
          <IconButton onClick={toggle} aria-label={mode === 'dark' ? 'Switch to light mode' : 'Switch to dark mode'} sx={{ mr: 0.5 }}>
            {mode === 'dark' ? (
              <LightModeIcon fontSize="small" />
            ) : (
              <DarkModeIcon fontSize="small" />
            )}
          </IconButton>
        </Tooltip>

        {/* Notifications */}
        <Tooltip title="Notifications">
          <IconButton onClick={handleNotifMenu} aria-label={`Notifications${3 > 0 ? ` (${3} unread)` : ''}`} sx={{ mr: 0.5 }}>
            <Badge badgeContent={3} color="error" max={99}>
              <NotificationsIcon fontSize="small" />
            </Badge>
          </IconButton>
        </Tooltip>
        <Menu
          anchorEl={notifAnchorEl}
          open={Boolean(notifAnchorEl)}
          onClose={handleClose}
          transformOrigin={{ horizontal: 'right', vertical: 'top' }}
          anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
          PaperProps={{
            sx: { width: 320, mt: 1, borderRadius: 2, border: '1px solid', borderColor: 'divider' },
          }}
        >
          <MenuItem disabled sx={{ opacity: 1 }}>
            <Typography variant="subtitle2" fontWeight={600}>
              Notifications
            </Typography>
          </MenuItem>
          <Divider />
          <MenuItem onClick={handleClose} sx={{ py: 1.5 }}>
            <Box>
              <Typography variant="body2" fontWeight={500}>
                Welcome to PowerSphere
              </Typography>
              <Typography variant="caption" color="text.secondary">
                The platform is ready for development.
              </Typography>
            </Box>
          </MenuItem>
        </Menu>

        {/* Profile Menu */}
        <Tooltip title="Account settings">
          <IconButton onClick={handleProfileMenu} size="small" aria-label="Account settings" aria-haspopup="true" aria-expanded={Boolean(anchorEl)}>
            <Avatar
              sx={{
                width: 34,
                height: 34,
                bgcolor: 'primary.main',
                fontSize: '0.875rem',
                fontWeight: 600,
              }}
              aria-hidden="true"
            >
              {userInitial}
            </Avatar>
          </IconButton>
        </Tooltip>
        <Menu
          anchorEl={anchorEl}
          open={Boolean(anchorEl)}
          onClose={handleClose}
          transformOrigin={{ horizontal: 'right', vertical: 'top' }}
          anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
          PaperProps={{
            sx: { mt: 1, minWidth: 200, borderRadius: 2, border: '1px solid', borderColor: 'divider' },
          }}
        >
          <MenuItem disabled sx={{ opacity: 1, py: 1.5 }}>
            <Box>
              <Typography variant="body2" fontWeight={600}>
                {userName}
              </Typography>
              <Typography variant="caption" color="text.secondary">
                {userEmail}
              </Typography>
            </Box>
          </MenuItem>
          <Divider />
          <MenuItem onClick={() => handleNavigate(ROUTES.PROFILE)}>
            <PersonIcon fontSize="small" sx={{ mr: 1.5, color: 'text.secondary' }} />
            Profile
          </MenuItem>
          <MenuItem onClick={() => handleNavigate(ROUTES.SETTINGS)}>
            <SettingsIcon fontSize="small" sx={{ mr: 1.5, color: 'text.secondary' }} />
            Settings
          </MenuItem>
          <Divider />
          <MenuItem onClick={handleLogout} sx={{ color: 'error.main' }}>
            <LogoutIcon fontSize="small" sx={{ mr: 1.5 }} />
            Logout
          </MenuItem>
        </Menu>
      </Toolbar>
    </AppBar>
  );
}
