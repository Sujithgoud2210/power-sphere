import { useParams, useNavigate } from 'react-router-dom';
import { Box, Button, Typography, Breadcrumbs, Link, Grid, Card, CardContent } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';
import { PageHeader, LoadingSkeleton, EmptyState } from '@/components/common';
import { useConsumptionHistory, useMeter } from '@/services';
import { ROUTES } from '@/constants';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import ArrowUpwardIcon from '@mui/icons-material/ArrowUpward';
import dayjs from 'dayjs';

export function ConsumptionHistoryPage() {
  const { meterId } = useParams<{ meterId: string }>();
  const navigate = useNavigate();
  const { data: meter } = useMeter(meterId);
  const { data: history, isLoading, error } = useConsumptionHistory(meterId ? Number(meterId) : undefined);

  return (
    <Box>
      <Breadcrumbs sx={{ mb: 1 }}>
        <Link component={RouterLink} to={ROUTES.ENERGY} underline="hover" color="text.secondary">Energy Readings</Link>
        <Typography color="text.primary">Consumption - {meter?.meterNumber || `Meter #${meterId}`}</Typography>
      </Breadcrumbs>
      <PageHeader
        title="Consumption History"
        description={`Meter: ${meter?.meterNumber || `#${meterId}`}`}
        action={<Button variant="outlined" startIcon={<ArrowBackIcon />} onClick={() => navigate(ROUTES.ENERGY)} sx={{ borderRadius: 2 }}>Back</Button>}
      />
      {isLoading ? (
        <LoadingSkeleton variant="card" rows={3} />
      ) : error ? (
        <EmptyState title="Error loading data" description={(error as Error).message} variant="error" />
      ) : !history || history.length === 0 ? (
        <EmptyState title="No consumption data" description="No consumption records found for this meter." />
      ) : (
        <Grid container spacing={2}>
          {history.map((item, index) => (
            <Grid item xs={12} sm={6} md={4} key={index}>
              <Card elevation={0} sx={{ border: '1px solid', borderColor: 'divider', borderRadius: 2 }}>
                <CardContent>
                  <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 1 }}>
                    <ArrowUpwardIcon color="primary" />
                    <Typography variant="subtitle2" fontWeight={600}>{item.totalConsumption} {item.unit}</Typography>
                  </Box>
                  <Typography variant="caption" color="text.secondary">
                    {dayjs(item.startDate).format('MMM D, YYYY')} - {dayjs(item.endDate).format('MMM D, YYYY')}
                  </Typography>
                  <Box sx={{ mt: 1 }}>
                    <Typography variant="caption" color="text.secondary">Avg Daily: {item.averageDailyConsumption?.toFixed(2)} {item.unit}</Typography>
                  </Box>
                  {item.estimatedCost && (
                    <Typography variant="caption" color="text.secondary">Est. Cost: ${item.estimatedCost.toFixed(2)}</Typography>
                  )}
                  <Box sx={{ mt: 0.5 }}>
                    <Typography variant="caption" color="text.secondary">{item.readingCount} readings</Typography>
                  </Box>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      )}
    </Box>
  );
}
