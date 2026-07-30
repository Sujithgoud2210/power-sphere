import { useLocation, useNavigate } from 'react-router-dom';
import {
  Box,
  Drawer,
  List,
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Typography,
  Divider,
  Tooltip,
} from '@mui/material';
import DashboardIcon from '@mui/icons-material/Dashboard';
import BusinessIcon from '@mui/icons-material/Business';
import PeopleIcon from '@mui/icons-material/People';
import SpeedIcon from '@mui/icons-material/Speed';
import BoltIcon from '@mui/icons-material/Bolt';
import ReceiptIcon from '@mui/icons-material/Receipt';
import NotificationsIcon from '@mui/icons-material/Notifications';
import AssessmentIcon from '@mui/icons-material/Assessment';
import SettingsIcon from '@mui/icons-material/Settings';
import PersonIcon from '@mui/icons-material/Person';
import LogoutIcon from '@mui/icons-material/Logout';
import { DRAWER_WIDTH, SIDEBAR_MENU_ITEMS } from '@/constants';
import { useAuth } from '@/hooks';
import { ROUTES } from '@/constants';
import type { SvgIconComponent } from '@mui/icons-material';

const iconMap: Record<string, SvgIconComponent> = {
  Dashboard: DashboardIcon,
  Business: BusinessIcon,
  People: PeopleIcon,
  Speed: SpeedIcon,
  Bolt: BoltIcon,
  Receipt: ReceiptIcon,
  Notifications: NotificationsIcon,
  Assessment: AssessmentIcon,
  Settings: SettingsIcon,
  Person: PersonIcon,
  Logout: LogoutIcon,
};

const bottomMenuItems = [
  { label: 'Settings', path: ROUTES.SETTINGS, icon: 'Settings' },
  { label: 'Profile', path: ROUTES.PROFILE, icon: 'Person' },
];

interface SidebarProps {
  open: boolean;
  onClose: () => void;
  variant: 'permanent' | 'temporary';
}

export function Sidebar({ open, onClose, variant }: SidebarProps) {
  const location = useLocation();
  const navigate = useNavigate();
  const { logout } = useAuth();

  const handleNavigate = (path: string) => {
    navigate(path);
    if (variant === 'temporary') {
      onClose();
    }
  };

  const isActive = (path: string) => location.pathname === path;

  const sidebarContent = (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        height: '100%',
        bgcolor: 'background.paper',
        borderRight: '1px solid',
        borderColor: 'divider',
      }}
    >
      {/* Logo */}
      <Box
        sx={{
          display: 'flex',
          alignItems: 'center',
          gap: 1.5,
          px: 2.5,
          py: 2.5,
          minHeight: 64,
        }}
      >
        <BoltIcon color="primary" sx={{ fontSize: 32 }} />
        <Typography variant="h6" fontWeight={700} color="primary">
          PowerSphere
        </Typography>
      </Box>

      <Divider />

      {/* Main Navigation */}
      <List sx={{ flex: 1, px: 1.5, py: 1 }} disablePadding>
        {SIDEBAR_MENU_ITEMS.map((item) => {
          const Icon = iconMap[item.icon];
          const active = isActive(item.path);
          return (
            <ListItem key={item.path} disablePadding sx={{ mb: 0.5 }}>
              <Tooltip title={item.label} placement="right">
                <ListItemButton
                  onClick={() => handleNavigate(item.path)}
                  selected={active}
                  sx={{
                    borderRadius: 2,
                    '&.Mui-selected': {
                      bgcolor: 'primary.main',
                      color: 'primary.contrastText',
                      '&:hover': {
                        bgcolor: 'primary.dark',
                      },
                      '& .MuiListItemIcon-root': {
                        color: 'primary.contrastText',
                      },
                    },
                  }}
                >
                  <ListItemIcon
                    sx={{
                      minWidth: 40,
                      color: active ? 'inherit' : 'text.secondary',
                    }}
                  >
                    <Icon fontSize="small" />
                  </ListItemIcon>
                  <ListItemText
                    primary={item.label}
                    primaryTypographyProps={{
                      fontSize: '0.875rem',
                      fontWeight: active ? 600 : 500,
                    }}
                  />
                </ListItemButton>
              </Tooltip>
            </ListItem>
          );
        })}
      </List>

      <Divider />

      {/* Bottom Navigation */}
      <List sx={{ px: 1.5, py: 1 }} disablePadding>
        {bottomMenuItems.map((item) => {
          const Icon = iconMap[item.icon];
          const active = isActive(item.path);
          return (
            <ListItem key={item.path} disablePadding sx={{ mb: 0.5 }}>
              <Tooltip title={item.label} placement="right">
                <ListItemButton
                  onClick={() => handleNavigate(item.path)}
                  selected={active}
                  sx={{
                    borderRadius: 2,
                    '&.Mui-selected': {
                      bgcolor: 'primary.main',
                      color: 'primary.contrastText',
                      '&:hover': {
                        bgcolor: 'primary.dark',
                      },
                      '& .MuiListItemIcon-root': {
                        color: 'primary.contrastText',
                      },
                    },
                  }}
                >
                  <ListItemIcon
                    sx={{
                      minWidth: 40,
                      color: active ? 'inherit' : 'text.secondary',
                    }}
                  >
                    <Icon fontSize="small" />
                  </ListItemIcon>
                  <ListItemText
                    primary={item.label}
                    primaryTypographyProps={{
                      fontSize: '0.875rem',
                      fontWeight: active ? 600 : 500,
                    }}
                  />
                </ListItemButton>
              </Tooltip>
            </ListItem>
          );
        })}
        <ListItem disablePadding sx={{ mb: 0.5 }}>
          <Tooltip title="Logout" placement="right">
            <ListItemButton
              onClick={logout}
              sx={{
                borderRadius: 2,
                color: 'error.main',
                '&:hover': {
                  bgcolor: 'error.main',
                  color: 'error.contrastText',
                  '& .MuiListItemIcon-root': {
                    color: 'error.contrastText',
                  },
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 40, color: 'inherit' }}>
                <LogoutIcon fontSize="small" />
              </ListItemIcon>
              <ListItemText
                primary="Logout"
                primaryTypographyProps={{ fontSize: '0.875rem', fontWeight: 500 }}
              />
            </ListItemButton>
          </Tooltip>
        </ListItem>
      </List>
    </Box>
  );

  return (
    <Drawer
      variant={variant}
      open={open}
      onClose={onClose}
      sx={{
        width: DRAWER_WIDTH,
        flexShrink: 0,
        '& .MuiDrawer-paper': {
          width: DRAWER_WIDTH,
          boxSizing: 'border-box',
          border: 'none',
        },
      }}
    >
      {sidebarContent}
    </Drawer>
  );
}
