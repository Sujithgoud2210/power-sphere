import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
  Button,
  CircularProgress,
} from '@mui/material';
import WarningAmberIcon from '@mui/icons-material/WarningAmber';
import InfoOutlinedIcon from '@mui/icons-material/InfoOutlined';
import DeleteOutlineIcon from '@mui/icons-material/DeleteOutline';

interface ConfirmationDialogProps {
  open: boolean;
  title: string;
  message: string;
  confirmLabel?: string;
  cancelLabel?: string;
  onConfirm: () => void;
  onCancel: () => void;
  isLoading?: boolean;
  variant?: 'delete' | 'warning' | 'info';
}

export function ConfirmationDialog({
  open,
  title,
  message,
  confirmLabel = 'Confirm',
  cancelLabel = 'Cancel',
  onConfirm,
  onCancel,
  isLoading = false,
  variant = 'delete',
}: ConfirmationDialogProps) {
  const iconMap = {
    delete: <DeleteOutlineIcon sx={{ fontSize: 48, color: 'error.main' }} />,
    warning: <WarningAmberIcon sx={{ fontSize: 48, color: 'warning.main' }} />,
    info: <InfoOutlinedIcon sx={{ fontSize: 48, color: 'primary.main' }} />,
  };

  const colorMap = {
    delete: 'error' as const,
    warning: 'warning' as const,
    info: 'primary' as const,
  };

  return (
    <Dialog
      open={open}
      onClose={onCancel}
      maxWidth="xs"
      fullWidth
      PaperProps={{
        sx: { borderRadius: 3, p: 1 },
      }}
    >
      <DialogTitle sx={{ textAlign: 'center', pb: 1 }}>
        {iconMap[variant]}
      </DialogTitle>
      <DialogContent sx={{ textAlign: 'center', pb: 2 }}>
        <DialogContentText
          component="div"
          sx={{ '& strong': { color: 'text.primary' } }}
        >
          <strong style={{ fontSize: '1.125rem', display: 'block', marginBottom: 8 }}>
            {title}
          </strong>
          {message}
        </DialogContentText>
      </DialogContent>
      <DialogActions sx={{ justifyContent: 'center', gap: 1, px: 3, pb: 2 }}>
        <Button
          variant="outlined"
          onClick={onCancel}
          disabled={isLoading}
          sx={{ borderRadius: 2, minWidth: 100 }}
        >
          {cancelLabel}
        </Button>
        <Button
          variant="contained"
          color={colorMap[variant]}
          onClick={onConfirm}
          disabled={isLoading}
          sx={{ borderRadius: 2, minWidth: 100 }}
        >
          {isLoading ? <CircularProgress size={20} color="inherit" /> : confirmLabel}
        </Button>
      </DialogActions>
    </Dialog>
  );
}
