import { Chip, type ChipProps } from '@mui/material';
import AdminPanelSettingsIcon from '@mui/icons-material/AdminPanelSettings';
import ManageAccountsIcon from '@mui/icons-material/ManageAccounts';
import EngineeringIcon from '@mui/icons-material/Engineering';
import VisibilityIcon from '@mui/icons-material/Visibility';
import type { UserRole } from '@/types';

interface RoleBadgeProps {
  role: UserRole;
  size?: ChipProps['size'];
}

const ROLE_CONFIG: Record<UserRole, { label: string; color: ChipProps['color']; icon: React.ReactElement }> = {
  ADMIN: {
    label: 'Admin',
    color: 'error',
    icon: <AdminPanelSettingsIcon />,
  },
  MANAGER: {
    label: 'Manager',
    color: 'primary',
    icon: <ManageAccountsIcon />,
  },
  OPERATOR: {
    label: 'Operator',
    color: 'warning',
    icon: <EngineeringIcon />,
  },
  VIEWER: {
    label: 'Viewer',
    color: 'default',
    icon: <VisibilityIcon />,
  },
  USER: {
    label: 'User',
    color: 'default',
    icon: <VisibilityIcon />,
  },
};

export function RoleBadge({ role, size = 'small' }: RoleBadgeProps) {
  const config = ROLE_CONFIG[role] ?? {
    label: role,
    color: 'default' as ChipProps['color'],
    icon: <VisibilityIcon />,
  };

  return (
    <Chip
      label={config.label}
      color={config.color}
      size={size}
      icon={config.icon}
      variant="filled"
      sx={{
        fontWeight: 500,
        '& .MuiChip-icon': { fontSize: 16 },
      }}
    />
  );
}
