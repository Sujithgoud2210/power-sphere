import { Chip, type ChipProps } from '@mui/material';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import CancelIcon from '@mui/icons-material/Cancel';
import HourglassEmptyIcon from '@mui/icons-material/HourglassEmpty';
import WarningIcon from '@mui/icons-material/Warning';
import ErrorIcon from '@mui/icons-material/Error';
import BuildCircleIcon from '@mui/icons-material/BuildCircle';
import BlockIcon from '@mui/icons-material/Block';

interface StatusChipProps {
  status: string;
  size?: ChipProps['size'];
}

const statusConfig: Record<string, { label: string; color: ChipProps['color']; icon: React.ReactElement }> = {
  // Generic
  ACTIVE: { label: 'Active', color: 'success', icon: <CheckCircleIcon /> },
  INACTIVE: { label: 'Inactive', color: 'default', icon: <CancelIcon /> },
  PENDING: { label: 'Pending', color: 'warning', icon: <HourglassEmptyIcon /> },
  
  // Meter statuses
  MAINTENANCE: { label: 'Maintenance', color: 'info', icon: <BuildCircleIcon /> },
  ERROR: { label: 'Error', color: 'error', icon: <ErrorIcon /> },
  DECOMMISSIONED: { label: 'Decommissioned', color: 'default', icon: <BlockIcon /> },
  
  // Bill statuses
  DRAFT: { label: 'Draft', color: 'default', icon: <HourglassEmptyIcon /> },
  GENERATED: { label: 'Generated', color: 'info', icon: <HourglassEmptyIcon /> },
  SENT: { label: 'Sent', color: 'primary', icon: <CheckCircleIcon /> },
  PAID: { label: 'Paid', color: 'success', icon: <CheckCircleIcon /> },
  OVERDUE: { label: 'Overdue', color: 'error', icon: <WarningIcon /> },
  CANCELLED: { label: 'Cancelled', color: 'default', icon: <CancelIcon /> },
  REFUNDED: { label: 'Refunded', color: 'warning', icon: <WarningIcon /> },
  
  // Energy quality
  VALID: { label: 'Valid', color: 'success', icon: <CheckCircleIcon /> },
  SUSPICIOUS: { label: 'Suspicious', color: 'warning', icon: <WarningIcon /> },
  ESTIMATED: { label: 'Estimated', color: 'info', icon: <HourglassEmptyIcon /> },
  MISSING: { label: 'Missing', color: 'error', icon: <ErrorIcon /> },
};

export function StatusChip({ status, size = 'small' }: StatusChipProps) {
  const config = statusConfig[status] ?? {
    label: status,
    color: 'default' as ChipProps['color'],
    icon: <HourglassEmptyIcon />,
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
