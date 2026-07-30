import { useState, useCallback, useRef, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Box,
  InputBase,
  Paper,
  List,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Typography,
  Chip,
  IconButton,
  CircularProgress,
  useMediaQuery,
  useTheme,
  Popper,
  ClickAwayListener,
} from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import ClearIcon from '@mui/icons-material/Clear';
import BusinessIcon from '@mui/icons-material/Business';
import PeopleIcon from '@mui/icons-material/People';
import SpeedIcon from '@mui/icons-material/Speed';
import BoltIcon from '@mui/icons-material/Bolt';
import ReceiptIcon from '@mui/icons-material/Receipt';
import { useDebounce } from '@/hooks/useDebounce';
import { ROUTES } from '@/constants';
import type { SvgIconComponent } from '@mui/icons-material';

interface SearchResult {
  id: string | number;
  label: string;
  subtitle?: string;
  type: 'organization' | 'user' | 'meter' | 'energy' | 'bill';
  path: string;
}

interface SearchCategory {
  key: string;
  label: string;
  icon: SvgIconComponent;
  color: string;
}

const SEARCH_CATEGORIES: SearchCategory[] = [
  { key: 'organizations', label: 'Organizations', icon: BusinessIcon, color: 'primary.main' },
  { key: 'users', label: 'Users', icon: PeopleIcon, color: 'secondary.main' },
  { key: 'meters', label: 'Meters', icon: SpeedIcon, color: 'success.main' },
  { key: 'energy', label: 'Energy', icon: BoltIcon, color: 'warning.main' },
  { key: 'bills', label: 'Bills', icon: ReceiptIcon, color: 'info.main' },
];

// Mock search function - replace with actual API calls when backend is ready
async function performSearch(query: string): Promise<SearchResult[]> {
  if (!query || query.length < 2) return [];

  // Simulate API delay
  await new Promise((resolve) => setTimeout(resolve, 150));

  const lowerQuery = query.toLowerCase();
  const results: SearchResult[] = [];

  // Mock organizations
  if ('organizations'.includes(lowerQuery) || lowerQuery.includes('org')) {
    results.push(
      { id: 1, label: 'Acme Power Corp', subtitle: 'Main utility provider', type: 'organization', path: ROUTES.ORGANIZATIONS },
      { id: 2, label: 'Green Energy Ltd', subtitle: 'Renewable energy company', type: 'organization', path: ROUTES.ORGANIZATIONS },
    );
  }

  // Mock users
  if ('users'.includes(lowerQuery) || lowerQuery.includes('user') || lowerQuery.includes('admin')) {
    results.push(
      { id: 1, label: 'John Admin', subtitle: 'john@powersphere.com · ADMIN', type: 'user', path: ROUTES.USERS },
      { id: 2, label: 'Jane Manager', subtitle: 'jane@powersphere.com · MANAGER', type: 'user', path: ROUTES.USERS },
    );
  }

  // Mock meters
  if ('meters'.includes(lowerQuery) || lowerQuery.includes('meter') || /\d{3,}/.test(query)) {
    results.push(
      { id: 'MTR-001', label: 'MTR-001', subtitle: 'Smart Meter · Building A', type: 'meter', path: ROUTES.METERS },
      { id: 'MTR-002', label: 'MTR-002', subtitle: 'Smart Meter · Building B', type: 'meter', path: ROUTES.METERS },
    );
  }

  return results;
}

interface GlobalSearchProps {
  fullWidth?: boolean;
  placeholder?: string;
}

export function GlobalSearch({ fullWidth = false, placeholder = 'Search organizations, users, meters...' }: GlobalSearchProps) {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('sm'));
  const navigate = useNavigate();
  const anchorRef = useRef<HTMLDivElement>(null);

  const [query, setQuery] = useState('');
  const [results, setResults] = useState<SearchResult[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [isOpen, setIsOpen] = useState(false);
  const [selectedIndex, setSelectedIndex] = useState(-1);

  const debouncedQuery = useDebounce(query, 300);

  useEffect(() => {
    if (!debouncedQuery || debouncedQuery.length < 2) {
      setResults([]);
      setIsOpen(false);
      return;
    }

    let cancelled = false;
    setIsLoading(true);

    performSearch(debouncedQuery).then((data) => {
      if (!cancelled) {
        setResults(data);
        setIsOpen(data.length > 0);
        setIsLoading(false);
        setSelectedIndex(-1);
      }
    });

    return () => {
      cancelled = true;
    };
  }, [debouncedQuery]);

  const handleSelect = useCallback(
    (result: SearchResult) => {
      setQuery('');
      setResults([]);
      setIsOpen(false);
      navigate(result.path);
    },
    [navigate],
  );

  const handleClear = useCallback(() => {
    setQuery('');
    setResults([]);
    setIsOpen(false);
  }, []);

  const handleKeyDown = useCallback(
    (event: React.KeyboardEvent) => {
      if (!isOpen || results.length === 0) return;

      switch (event.key) {
        case 'ArrowDown':
          event.preventDefault();
          setSelectedIndex((prev) => (prev < results.length - 1 ? prev + 1 : 0));
          break;
        case 'ArrowUp':
          event.preventDefault();
          setSelectedIndex((prev) => (prev > 0 ? prev - 1 : results.length - 1));
          break;
        case 'Enter':
          event.preventDefault();
          if (selectedIndex >= 0 && selectedIndex < results.length) {
            handleSelect(results[selectedIndex]);
          }
          break;
        case 'Escape':
          event.preventDefault();
          setIsOpen(false);
          break;
      }
    },
    [isOpen, results, selectedIndex, handleSelect],
  );

  const groupedResults = results.reduce<Record<string, SearchResult[]>>((acc, result) => {
    if (!acc[result.type]) acc[result.type] = [];
    acc[result.type].push(result);
    return acc;
  }, {});

  const resultTypeToCategory: Record<string, SearchCategory> = {
    organization: SEARCH_CATEGORIES[0],
    user: SEARCH_CATEGORIES[1],
    meter: SEARCH_CATEGORIES[2],
    energy: SEARCH_CATEGORIES[3],
    bill: SEARCH_CATEGORIES[4],
  };

  return (
    <ClickAwayListener onClickAway={() => setIsOpen(false)}>
      <Box
        ref={anchorRef}
        sx={{
          position: 'relative',
          width: fullWidth ? '100%' : { xs: '100%', sm: 320, md: 360 },
        }}
      >
        <Paper
          elevation={0}
          sx={{
            display: 'flex',
            alignItems: 'center',
            bgcolor: 'action.hover',
            borderRadius: 2,
            px: 1.5,
            py: 0.5,
            border: '1px solid',
            borderColor: isOpen ? 'primary.main' : 'transparent',
            transition: 'border-color 0.2s, box-shadow 0.2s',
            '&:focus-within': {
              borderColor: 'primary.main',
              boxShadow: (t) => `0 0 0 2px ${t.palette.primary.main}33`,
            },
          }}
        >
          {isLoading ? (
            <CircularProgress size={18} sx={{ color: 'text.disabled', mr: 1 }} />
          ) : (
            <SearchIcon sx={{ color: 'text.disabled', mr: 1, fontSize: 20 }} />
          )}
          <InputBase
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            onFocus={() => {
              if (results.length > 0) setIsOpen(true);
            }}
            onKeyDown={handleKeyDown}
            placeholder={isMobile ? 'Search...' : placeholder}
            inputProps={{
              'aria-label': 'Global search',
              'aria-expanded': isOpen,
              'aria-haspopup': 'listbox',
              'aria-controls': isOpen ? 'global-search-results' : undefined,
              'aria-activedescendant': selectedIndex >= 0 ? `search-result-${selectedIndex}` : undefined,
              role: 'combobox',
              autoComplete: 'off',
            }}
            sx={{
              fontSize: '0.875rem',
              width: '100%',
              '& input::placeholder': {
                opacity: 0.7,
              },
            }}
          />
          {query && (
            <IconButton size="small" onClick={handleClear} aria-label="Clear search" edge="end">
              <ClearIcon fontSize="small" />
            </IconButton>
          )}
        </Paper>

        <Popper
          open={isOpen}
          anchorEl={anchorRef.current}
          placement="bottom-start"
          style={{ zIndex: theme.zIndex.modal, width: anchorRef.current?.offsetWidth ?? 360 }}
        >
          <Paper
            id="global-search-results"
            role="listbox"
            elevation={8}
            sx={{
              mt: 0.5,
              borderRadius: 2,
              maxHeight: 400,
              overflow: 'auto',
              border: '1px solid',
              borderColor: 'divider',
            }}
          >
            {Object.entries(groupedResults).map(([type, typeResults]) => {
              const category = resultTypeToCategory[type];
              const CategoryIcon = category?.icon ?? SearchIcon;

              return (
                <Box key={type}>
                  <Box
                    sx={{
                      display: 'flex',
                      alignItems: 'center',
                      gap: 1,
                      px: 2,
                      py: 1,
                      bgcolor: 'action.hover',
                    }}
                  >
                    <CategoryIcon sx={{ fontSize: 16, color: category?.color }} />
                    <Typography variant="caption" fontWeight={600} color="text.secondary" sx={{ textTransform: 'uppercase', letterSpacing: 0.5 }}>
                      {category?.label ?? type}
                    </Typography>
                    <Chip label={typeResults.length} size="small" sx={{ height: 18, '& .MuiChip-label': { px: 0.5, fontSize: '0.7rem' } }} />
                  </Box>
                  <List disablePadding>
                    {typeResults.map((result) => {
                      const globalIndex = Object.values(groupedResults).flat().indexOf(result);
                      return (
                        <ListItemButton
                          key={`${result.type}-${result.id}`}
                          id={`search-result-${globalIndex}`}
                          role="option"
                          aria-selected={selectedIndex === globalIndex}
                          selected={selectedIndex === globalIndex}
                          onClick={() => handleSelect(result)}
                          sx={{
                            px: 2,
                            py: 1,
                            '&.Mui-selected': {
                              bgcolor: 'action.selected',
                            },
                          }}
                        >
                          <ListItemIcon sx={{ minWidth: 36 }}>
                            <CategoryIcon sx={{ fontSize: 18, color: category?.color }} />
                          </ListItemIcon>
                          <ListItemText
                            primary={result.label}
                            secondary={result.subtitle}
                            primaryTypographyProps={{ variant: 'body2', fontWeight: 500 }}
                            secondaryTypographyProps={{ variant: 'caption' }}
                          />
                        </ListItemButton>
                      );
                    })}
                  </List>
                </Box>
              );
            })}
          </Paper>
        </Popper>
      </Box>
    </ClickAwayListener>
  );
}
