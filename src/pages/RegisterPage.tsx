import { useState } from 'react';
import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { useNavigate, Link as RouterLink } from 'react-router-dom';
import {
  Box,
  Button,
  Container,
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
import { registerSchema, type RegisterFormData } from '@/utils';
import { useAppDispatch, useAppSelector } from '@/hooks';
import { registerThunk, clearAuthError } from '@/store/slices/authSlice';
import { ROUTES } from '@/constants';

export function RegisterPage() {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { isLoading, error } = useAppSelector((state) => state.auth);
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);

  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm<RegisterFormData>({
    resolver: zodResolver(registerSchema),
    defaultValues: {
      firstName: '',
      lastName: '',
      email: '',
      password: '',
      confirmPassword: '',
    },
  });

  const onSubmit = async (data: RegisterFormData) => {
    const result = await dispatch(
      registerThunk({
        firstName: data.firstName,
        lastName: data.lastName,
        email: data.email,
        password: data.password,
      }),
    );

    if (registerThunk.fulfilled.match(result)) {
      setSuccessMessage(
        'Account created successfully! Redirecting to login...',
      );
      setTimeout(() => {
        navigate(ROUTES.LOGIN, { replace: true });
      }, 2000);
    }
  };

  return (
    <Box
      sx={{
        minHeight: '100vh',
        display: 'flex',
        alignItems: 'center',
        py: 4,
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
              Create your account to get started
            </Typography>
          </Box>

          {/* Register Card */}
          <Paper
            elevation={0}
            sx={{
              p: 4,
              border: '1px solid',
              borderColor: 'divider',
              borderRadius: 3,
              width: '100%',
            }}
          >
            {/* Success Alert */}
            {successMessage && (
              <Alert
                severity="success"
                sx={{ mb: 3, borderRadius: 2 }}
              >
                {successMessage}
              </Alert>
            )}

            {/* Error Alert */}
            {error && !successMessage && (
              <Alert
                severity="error"
                onClose={() => dispatch(clearAuthError())}
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
              {/* First Name & Last Name */}
              <Box
                sx={{
                  display: 'flex',
                  gap: 2,
                  mb: 2.5,
                  flexDirection: { xs: 'column', sm: 'row' },
                }}
              >
                <Box sx={{ flex: 1 }}>
                  <Controller
                    name="firstName"
                    control={control}
                    render={({ field }) => (
                      <TextField
                        {...field}
                        fullWidth
                        label="First Name"
                        autoComplete="given-name"
                        autoFocus
                        error={!!errors.firstName}
                        helperText={errors.firstName?.message}
                        disabled={isLoading || !!successMessage}
                        slotProps={{
                          input: { sx: { borderRadius: 2 } },
                        }}
                      />
                    )}
                  />
                </Box>
                <Box sx={{ flex: 1 }}>
                  <Controller
                    name="lastName"
                    control={control}
                    render={({ field }) => (
                      <TextField
                        {...field}
                        fullWidth
                        label="Last Name"
                        autoComplete="family-name"
                        error={!!errors.lastName}
                        helperText={errors.lastName?.message}
                        disabled={isLoading || !!successMessage}
                        slotProps={{
                          input: { sx: { borderRadius: 2 } },
                        }}
                      />
                    )}
                  />
                </Box>
              </Box>

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
                    error={!!errors.email}
                    helperText={errors.email?.message}
                    disabled={isLoading || !!successMessage}
                    sx={{ mb: 2.5 }}
                    slotProps={{
                      input: { sx: { borderRadius: 2 } },
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
                    autoComplete="new-password"
                    error={!!errors.password}
                    helperText={errors.password?.message}
                    disabled={isLoading || !!successMessage}
                    sx={{ mb: 2.5 }}
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

              {/* Confirm Password Field */}
              <Controller
                name="confirmPassword"
                control={control}
                render={({ field }) => (
                  <TextField
                    {...field}
                    fullWidth
                    label="Confirm Password"
                    type={showConfirmPassword ? 'text' : 'password'}
                    autoComplete="new-password"
                    error={!!errors.confirmPassword}
                    helperText={errors.confirmPassword?.message}
                    disabled={isLoading || !!successMessage}
                    sx={{ mb: 3 }}
                    slotProps={{
                      input: {
                        sx: { borderRadius: 2 },
                        endAdornment: (
                          <InputAdornment position="end">
                            <IconButton
                              onClick={() =>
                                setShowConfirmPassword(!showConfirmPassword)
                              }
                              edge="end"
                              size="small"
                              tabIndex={-1}
                            >
                              {showConfirmPassword ? (
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

              {/* Register Button */}
              <Button
                type="submit"
                fullWidth
                variant="contained"
                size="large"
                disabled={isLoading || !!successMessage}
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
                  'Create Account'
                )}
              </Button>
            </Box>
          </Paper>

          {/* Sign In Link */}
          <Typography
            variant="body2"
            color="text.secondary"
            sx={{ mt: 3 }}
          >
            Already have an account?{' '}
            <Link
              component={RouterLink}
              to={ROUTES.LOGIN}
              underline="hover"
              sx={{ fontWeight: 600, cursor: 'pointer' }}
            >
              Sign in
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
