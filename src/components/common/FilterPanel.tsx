import { Box, Button, Collapse, Chip } from '@mui/material';
import FilterListIcon from '@mui/icons-material/FilterList';
import CloseIcon from '@mui/icons-material/Close';
import { useState, type ReactNode } from 'react';

interface FilterPanelProps {
  children: ReactNode;
  activeFilterCount?: number;
  onClear?: () => void;
}

export function FilterPanel({ children, activeFilterCount = 0, onClear }: FilterPanelProps) {
  const [open, setOpen] = useState(activeFilterCount > 0);

  return (
    <Box>
      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 1 }}>
        <Button
          variant={open ? 'contained' : 'outlined'}
          size="small"
          startIcon={<FilterListIcon />}
          onClick={() => setOpen(!open)}
          sx={{ borderRadius: 2 }}
        >
          Filters
          {activeFilterCount > 0 && (
            <Chip
              label={activeFilterCount}
              size="small"
              color="primary"
              sx={{ ml: 1, height: 20, minWidth: 20, '& .MuiChip-label': { px: 0.5, fontSize: '0.75rem' } }}
            />
          )}
        </Button>
        {activeFilterCount > 0 && onClear && (
          <Button
            size="small"
            color="error"
            startIcon={<CloseIcon />}
            onClick={onClear}
            sx={{ borderRadius: 2 }}
          >
            Clear
          </Button>
        )}
      </Box>
      <Collapse in={open}>
        <Box
          sx={{
            display: 'flex',
            flexWrap: 'wrap',
            gap: 2,
            p: 2,
            mb: 2,
            border: '1px solid',
            borderColor: 'divider',
            borderRadius: 2,
            bgcolor: 'background.paper',
          }}
        >
          {children}
        </Box>
      </Collapse>
    </Box>
  );
}
