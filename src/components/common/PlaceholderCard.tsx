import { Paper, Typography } from '@mui/material';
import ConstructionIcon from '@mui/icons-material/Construction';

interface PlaceholderCardProps {
  title: string;
  description?: string;
}

export function PlaceholderCard({
  title,
  description = 'Coming in upcoming development days.',
}: PlaceholderCardProps) {
  return (
    <Paper
      elevation={0}
      sx={{
        p: 6,
        textAlign: 'center',
        border: '1px dashed',
        borderColor: 'divider',
        borderRadius: 3,
        bgcolor: 'background.default',
      }}
    >
      <ConstructionIcon
        sx={{ fontSize: 64, color: 'text.disabled', mb: 2 }}
      />
      <Typography variant="h5" fontWeight={600} gutterBottom>
        {title}
      </Typography>
      <Typography variant="body1" color="text.secondary" sx={{ maxWidth: 500, mx: 'auto' }}>
        {description}
      </Typography>
    </Paper>
  );
}
