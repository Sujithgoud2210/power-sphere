import { useState } from 'react';
import {
  AppBar,
  Avatar,
  Badge,
  Box,
  IconButton,
  InputBase,
  Menu,
  MenuItem,
  Toolbar,
  Tooltip,
  Typography,
  useMediaQuery,
  useTheme,
} from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import SearchIcon from '@mui/icons-material/Search';
import NotificationsIcon from '@mui/icons-material/Notifications';
import DarkModeIcon from '@mui/icons-material/DarkMode';
import LightModeIcon from '@mui/icons-material/LightMode';
import { useAuth, useThemeMode } from '@/hooks';

interface HeaderProps {
  onToggleSidebar: () => void;
}

export function Header({ onToggleSidebar }: HeaderProps) {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('md'));
  const { mode, toggle } = useThemeMode();
  const { logout } = useAuth();
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
  const [notifAnchorEl, setNotifAnchorEl] = useState<null | HTMLElement>(null);

  const handleProfileMenu = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleNotifMenu = (event: React.MouseEvent<HTMLElement>) => {
    setNotifAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
    setNotifAnchorEl(null);
  };

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
          <IconButton edge="start" onClick={onToggleSidebar} sx={{ mr: 1 }}>
            <MenuIcon />
          </IconButton>
        )}

        {/* Search Bar */}
        <Box
          sx={{
            display: { xs: 'none', sm: 'flex' },
            alignItems: 'center',
            bgcolor: 'action.hover',
            borderRadius: 2,
            px: 1.5,
            py: 0.5,
            flex: 1,
            maxWidth: 400,
          }}
        >
          <SearchIcon sx={{ color: 'text.disabled', mr: 1, fontSize: 20 }} />
          <InputBase
            placeholder="Search..."
            sx={{ fontSize: '0.875rem', width: '100%' }}
            inputProps={{ 'aria-label': 'search' }}
          />
        </Box>

        <Box sx={{ flexGrow: 1 }} />

        {/* Dark Mode Toggle */}
        <Tooltip title={mode === 'dark' ? 'Light Mode' : 'Dark Mode'}>
          <IconButton onClick={toggle} sx={{ mr: 1 }}>
            {mode === 'dark' ? (
              <LightModeIcon fontSize="small" />
            ) : (
              <DarkModeIcon fontSize="small" />
            )}
          </IconButton>
        </Tooltip>

        {/* Notifications */}
        <Tooltip title="Notifications">
          <IconButton onClick={handleNotifMenu} sx={{ mr: 1 }}>
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
            sx: { width: 320, mt: 1, borderRadius: 2 },
          }}
        >
          <MenuItem disabled>
            <Typography variant="subtitle2" fontWeight={600}>
              Notifications
            </Typography>
          </MenuItem>
          <MenuItem onClick={handleClose}>
            <Box sx={{ py: 1 }}>
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
        <Tooltip title="Profile">
          <IconButton onClick={handleProfileMenu} size="small">
            <Avatar
              sx={{
                width: 34,
                height: 34,
                bgcolor: 'primary.main',
                fontSize: '0.875rem',
                fontWeight: 600,
              }}
            >
              U
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
            sx: { mt: 1, minWidth: 180, borderRadius: 2 },
          }}
        >
          <MenuItem disabled>
            <Box>
              <Typography variant="body2" fontWeight={600}>
                User
              </Typography>
              <Typography variant="caption" color="text.secondary">
                user@powersphere.com
              </Typography>
            </Box>
          </MenuItem>
          <MenuItem onClick={() => { handleClose(); /* navigate to profile */ }}>
            Profile
          </MenuItem>
          <MenuItem onClick={() => { handleClose(); /* navigate to settings */ }}>
            Settings
          </MenuItem>
          <MenuItem
            onClick={() => {
              handleClose();
              logout();
            }}
            sx={{ color: 'error.main' }}
          >
            Logout
          </MenuItem>
        </Menu>
      </Toolbar>
    </AppBar>
  );
}
