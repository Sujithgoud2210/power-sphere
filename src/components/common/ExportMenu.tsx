import { useState, useCallback } from 'react';
import {
  Button,
  Menu,
  MenuItem,
  ListItemIcon,
  ListItemText,
  Divider,
  CircularProgress,
  Typography,
  Tooltip,
} from '@mui/material';
import FileDownloadIcon from '@mui/icons-material/FileDownload';
import DescriptionIcon from '@mui/icons-material/Description';
import TableChartIcon from '@mui/icons-material/TableChart';
import PictureAsPdfIcon from '@mui/icons-material/PictureAsPdf';
import { toast } from 'react-toastify';

export type ExportFormat = 'xlsx' | 'csv' | 'pdf';

interface ExportAction {
  format: ExportFormat;
  label: string;
  description: string;
  icon: React.ReactElement;
}

const EXPORT_ACTIONS: ExportAction[] = [
  { format: 'xlsx', label: 'Export to Excel', description: 'Download as .xlsx file', icon: <TableChartIcon /> },
  { format: 'csv', label: 'Export to CSV', description: 'Download as .csv file', icon: <DescriptionIcon /> },
  { format: 'pdf', label: 'Export to PDF', description: 'Download as .pdf file', icon: <PictureAsPdfIcon /> },
];

interface ExportMenuProps {
  onExport?: (format: ExportFormat) => Promise<void> | void;
  disabled?: boolean;
  fileName?: string;
  variant?: 'button' | 'icon';
}

async function placeholderExport(format: ExportFormat, fileName?: string): Promise<void> {
  // Simulate export delay
  await new Promise((resolve) => setTimeout(resolve, 1500));

  const message = `📄 ${fileName || 'data'}.${format} — Export feature ready. Connect backend API endpoint to enable actual downloads.`;
  toast.info(message, {
    autoClose: 5000,
    style: { borderRadius: 12 },
  });
}

export function ExportMenu({ onExport, disabled = false, fileName, variant = 'button' }: ExportMenuProps) {
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
  const [exportingFormat, setExportingFormat] = useState<ExportFormat | null>(null);
  const open = Boolean(anchorEl);

  const handleClick = useCallback((event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  }, []);

  const handleClose = useCallback(() => {
    setAnchorEl(null);
  }, []);

  const handleExport = useCallback(
    async (format: ExportFormat) => {
      setExportingFormat(format);
      handleClose();

      try {
        if (onExport) {
          await onExport(format);
        } else {
          await placeholderExport(format, fileName);
        }
      } catch (error) {
        const message = error instanceof Error ? error.message : 'Export failed. Please try again.';
        toast.error(message, { style: { borderRadius: 12 } });
      } finally {
        setExportingFormat(null);
      }
    },
    [onExport, handleClose, fileName],
  );

  return (
    <>
      <Tooltip title={disabled ? 'Export unavailable' : 'Export data'}>
        <span>
          <Button
            variant="outlined"
            size="small"
            startIcon={
              exportingFormat ? (
                <CircularProgress size={16} color="inherit" />
              ) : (
                <FileDownloadIcon />
              )
            }
            onClick={handleClick}
            disabled={disabled || exportingFormat !== null}
            aria-label="Export data"
            aria-haspopup="true"
            aria-expanded={open}
            sx={{
              borderRadius: 2,
              minWidth: variant === 'icon' ? 36 : 'auto',
              px: variant === 'icon' ? 1 : 2,
            }}
          >
            {variant === 'button' && (exportingFormat ? 'Exporting...' : 'Export')}
          </Button>
        </span>
      </Tooltip>

      <Menu
        anchorEl={anchorEl}
        open={open}
        onClose={handleClose}
        transformOrigin={{ horizontal: 'right', vertical: 'top' }}
        anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
        PaperProps={{
          sx: {
            mt: 0.5,
            minWidth: 220,
            borderRadius: 2,
            border: '1px solid',
            borderColor: 'divider',
          },
        }}
      >
        <Typography variant="caption" color="text.secondary" sx={{ px: 2, py: 1, display: 'block', fontWeight: 600, textTransform: 'uppercase', letterSpacing: 0.5 }}>
          Export Format
        </Typography>
        <Divider />
        {EXPORT_ACTIONS.map((action) => (
          <MenuItem
            key={action.format}
            onClick={() => handleExport(action.format)}
            disabled={exportingFormat === action.format}
            sx={{ py: 1.5 }}
          >
            <ListItemIcon sx={{ color: action.format === 'pdf' ? 'error.main' : action.format === 'csv' ? 'success.main' : 'primary.main' }}>
              {exportingFormat === action.format ? <CircularProgress size={20} /> : action.icon}
            </ListItemIcon>
            <ListItemText
              primary={action.label}
              secondary={action.description}
              primaryTypographyProps={{ variant: 'body2', fontWeight: 500 }}
              secondaryTypographyProps={{ variant: 'caption' }}
            />
          </MenuItem>
        ))}
      </Menu>
    </>
  );
}
