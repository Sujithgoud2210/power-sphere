import { useState } from 'react';
import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import {
  Box,
  Button,
  Checkbox,
  Container,
  FormControlLabel,
  IconButton,
  InputAdornment,
  Link,
  Paper,
  TextField,
  Typography,
  Alert,
  CircularProgress,
} from '@mui/material';
import VisibilityIcon from '@mui/icons-material/Visibility';
import VisibilityOffIcon from '@mui/icons-material/VisibilityOff';
import BoltIcon from '@mui/icons-material/Bolt';
import { loginSchema, type LoginFormData } from '@/utils';
import { Link as RouterLink } from 'react-router-dom';
import { useAuth } from '@/hooks';
import { ROUTES } from '@/constants';

export function LoginPage() {
  const { login, isLoading, error, clearError } = useAuth();
  const [showPassword, setShowPassword] = useState(false);

  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
    defaultValues: {
      email: '',
      password: '',
      rememberMe: false,
    },
  });

  const onSubmit = (data: LoginFormData) => {
    login({
      email: data.email,
      password: data.password,
      rememberMe: data.rememberMe,
    });
  };

  return (
    <Box
      sx={{
        minHeight: '100vh',
        display: 'flex',
        alignItems: 'center',
        bgcolor: 'background.default',
      }}
    >
      <Container maxWidth="sm">
        <Box
          sx={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
            {/* Logo */}
            <Box sx={{ textAlign: 'center', mb: 4 }}>
              <Box
                sx={{
                  display: 'inline-flex',
                  alignItems: 'center',
                  gap: 1.5,
                  mb: 1,
                }}
              >
                <BoltIcon color="primary" sx={{ fontSize: 40 }} />
                <Typography
                  variant="h3"
                  fontWeight={700}
                  color="primary"
                  sx={{ letterSpacing: '-0.5px' }}
                >
                  PowerSphere
                </Typography>
              </Box>
              <Typography variant="body1" color="text.secondary">
                Sign in to your account to continue
              </Typography>
            </Box>

            {/* Login Card */}
            <Paper
              elevation={0}
              sx={{
                p: 4,
                border: '1px solid',
                borderColor: 'divider',
                borderRadius: 3,
              }}
            >
              {/* Error Alert */}
              {error && (
                <Alert
                  severity="error"
                  onClose={clearError}
                  sx={{ mb: 3, borderRadius: 2 }}
                >
                  {error}
                </Alert>
              )}

              <Box
                component="form"
                onSubmit={handleSubmit(onSubmit)}
                noValidate
              >
                {/* Email Field */}
                <Controller
                  name="email"
                  control={control}
                  render={({ field }) => (
                    <TextField
                      {...field}
                      fullWidth
                      label="Email Address"
                      type="email"
                      autoComplete="email"
                      autoFocus
                      error={!!errors.email}
                      helperText={errors.email?.message}
                      disabled={isLoading}
                      sx={{ mb: 2.5 }}
                      slotProps={{
                        input: {
                          sx: { borderRadius: 2 },
                        },
                      }}
                    />
                  )}
                />

                {/* Password Field */}
                <Controller
                  name="password"
                  control={control}
                  render={({ field }) => (
                    <TextField
                      {...field}
                      fullWidth
                      label="Password"
                      type={showPassword ? 'text' : 'password'}
                      autoComplete="current-password"
                      error={!!errors.password}
                      helperText={errors.password?.message}
                      disabled={isLoading}
                      sx={{ mb: 1.5 }}
                      slotProps={{
                        input: {
                          sx: { borderRadius: 2 },
                          endAdornment: (
                            <InputAdornment position="end">
                              <IconButton
                                onClick={() => setShowPassword(!showPassword)}
                                edge="end"
                                size="small"
                                tabIndex={-1}
                              >
                                {showPassword ? (
                                  <VisibilityOffIcon fontSize="small" />
                                ) : (
                                  <VisibilityIcon fontSize="small" />
                                )}
                              </IconButton>
                            </InputAdornment>
                          ),
                        },
                      }}
                    />
                  )}
                />

                {/* Remember Me & Forgot Password */}
                <Box
                  sx={{
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                    mb: 3,
                    flexWrap: 'wrap',
                  }}
                >
                  <Controller
                    name="rememberMe"
                    control={control}
                    render={({ field }) => (
                      <FormControlLabel
                        control={
                          <Checkbox
                            checked={field.value}
                            onChange={field.onChange}
                            disabled={isLoading}
                            size="small"
                          />
                        }
                        label={
                          <Typography variant="body2">Remember me</Typography>
                        }
                      />
                    )}
                  />
                  <Link
                    href="#"
                    variant="body2"
                    underline="hover"
                    sx={{ cursor: 'pointer', fontWeight: 500 }}
                    onClick={(e) => e.preventDefault()}
                  >
                    Forgot password?
                  </Link>
                </Box>

                {/* Login Button */}
                <Button
                  type="submit"
                  fullWidth
                  variant="contained"
                  size="large"
                  disabled={isLoading}
                  sx={{
                    py: 1.25,
                    borderRadius: 2,
                    fontWeight: 600,
                    fontSize: '0.9375rem',
                  }}
                >
                  {isLoading ? (
                    <CircularProgress size={22} color="inherit" />
                  ) : (
                    'Sign In'
                  )}
                </Button>
              </Box>
            </Paper>

            {/* Sign Up Link */}
            <Typography
              variant="body2"
              color="text.secondary"
              textAlign="center"
              sx={{ mt: 3 }}
            >
              Don&apos;t have an account?{' '}
              <Link
                component={RouterLink}
                to={ROUTES.REGISTER}
                underline="hover"
                sx={{ fontWeight: 600, cursor: 'pointer' }}
              >
                Sign up
              </Link>
            </Typography>

            {/* Footer */}
            <Typography
              variant="body2"
              color="text.secondary"
              textAlign="center"
              sx={{ mt: 1 }}
            >
              &copy; {new Date().getFullYear()} PowerSphere Inc. All rights
              reserved.
            </Typography>
        </Box>
      </Container>
    </Box>
  );
}
