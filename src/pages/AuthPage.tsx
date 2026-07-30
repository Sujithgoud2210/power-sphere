import { useState, useCallback } from 'react';
import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { useLocation } from 'react-router-dom';
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
  Tab,
  Tabs,
  Fade,
} from '@mui/material';
import VisibilityIcon from '@mui/icons-material/Visibility';
import VisibilityOffIcon from '@mui/icons-material/VisibilityOff';
import BoltIcon from '@mui/icons-material/Bolt';
import LoginIcon from '@mui/icons-material/Login';
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import {
  loginSchema,
  registerSchema,
  type LoginFormData,
  type RegisterFormData,
} from '@/utils';
import { useAuth, useAppDispatch } from '@/hooks';
import { registerThunk } from '@/store/slices/authSlice';
import { ROUTES } from '@/constants';

type TabValue = 0 | 1;

export function AuthPage() {
  const dispatch = useAppDispatch();
  const location = useLocation();

  // Determine initial tab from URL path
  const initialTab: TabValue = location.pathname === ROUTES.REGISTER ? 1 : 0;
  const [tab, setTab] = useState<TabValue>(initialTab);
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [regSuccess, setRegSuccess] = useState<string | null>(null);

  const { login, isLoading, error, clearError } = useAuth();

  // --- Login Form ---
  const loginForm = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
    defaultValues: { email: '', password: '', rememberMe: false },
  });

  const onLoginSubmit = (data: LoginFormData) => {
    login({
      email: data.email,
      password: data.password,
      rememberMe: data.rememberMe,
    });
  };

  // --- Register Form ---
  const registerForm = useForm<RegisterFormData>({
    resolver: zodResolver(registerSchema),
    defaultValues: {
      firstName: '',
      lastName: '',
      email: '',
      password: '',
      confirmPassword: '',
    },
  });

  const onRegisterSubmit = async (data: RegisterFormData) => {
    const result = await dispatch(
      registerThunk({
        firstName: data.firstName,
        lastName: data.lastName,
        email: data.email,
        password: data.password,
      }),
    );

    if (registerThunk.fulfilled.match(result)) {
      setRegSuccess('Account created successfully!');
      registerForm.reset();
      setTimeout(() => {
        setRegSuccess(null);
        setTab(0);
      }, 2000);
    }
  };

  // --- Tab switching ---
  const handleTabChange = useCallback(
    (_: React.SyntheticEvent, newValue: TabValue) => {
      setTab(newValue);
      setShowPassword(false);
      setShowConfirmPassword(false);
      setRegSuccess(null);
      clearError();
      loginForm.clearErrors();
      registerForm.clearErrors();
      // Update URL without navigation
      window.history.replaceState(
        null,
        '',
        newValue === 0 ? ROUTES.LOGIN : ROUTES.REGISTER,
      );
    },
    [clearError, dispatch, loginForm, registerForm],
  );

  const activeError = error;

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
              {tab === 0
                ? 'Sign in to your account to continue'
                : 'Create your account to get started'}
            </Typography>
          </Box>

          {/* Auth Card */}
          <Paper
            elevation={0}
            sx={{
              width: '100%',
              border: '1px solid',
              borderColor: 'divider',
              borderRadius: 3,
              overflow: 'hidden',
            }}
          >
            {/* Tabs */}
            <Tabs
              value={tab}
              onChange={handleTabChange}
              variant="fullWidth"
              sx={{
                borderBottom: '1px solid',
                borderColor: 'divider',
                '& .MuiTab-root': {
                  py: 2,
                  fontWeight: 600,
                  fontSize: '0.9375rem',
                  textTransform: 'none',
                },
              }}
            >
              <Tab
                icon={<LoginIcon sx={{ fontSize: 20 }} />}
                iconPosition="start"
                label="Sign In"
                disabled={isLoading}
              />
              <Tab
                icon={<PersonAddIcon sx={{ fontSize: 20 }} />}
                iconPosition="start"
                label="Sign Up"
                disabled={isLoading}
              />
            </Tabs>

            <Box sx={{ p: 4 }}>
              {/* Alerts */}
              {regSuccess && (
                <Alert severity="success" sx={{ mb: 3, borderRadius: 2 }}>
                  {regSuccess}
                </Alert>
              )}

              {activeError && !regSuccess && (
                <Alert
                  severity="error"
                  onClose={clearError}
                  sx={{ mb: 3, borderRadius: 2 }}
                >
                  {activeError}
                </Alert>
              )}

              {/* ============ LOGIN FORM ============ */}
              <Fade in={tab === 0} timeout={300} unmountOnExit>
                <Box>
                  {tab === 0 && (
                    <Box
                      component="form"
                      onSubmit={loginForm.handleSubmit(onLoginSubmit)}
                      noValidate
                    >
                      <Controller
                        name="email"
                        control={loginForm.control}
                        render={({ field }) => (
                          <TextField
                            {...field}
                            fullWidth
                            label="Email Address"
                            type="email"
                            autoComplete="email"
                            autoFocus
                            error={!!loginForm.formState.errors.email}
                            helperText={
                              loginForm.formState.errors.email?.message
                            }
                            disabled={isLoading}
                            sx={{ mb: 2.5 }}
                            slotProps={{
                              input: { sx: { borderRadius: 2 } },
                            }}
                          />
                        )}
                      />

                      <Controller
                        name="password"
                        control={loginForm.control}
                        render={({ field }) => (
                          <TextField
                            {...field}
                            fullWidth
                            label="Password"
                            type={showPassword ? 'text' : 'password'}
                            autoComplete="current-password"
                            error={!!loginForm.formState.errors.password}
                            helperText={
                              loginForm.formState.errors.password?.message
                            }
                            disabled={isLoading}
                            sx={{ mb: 1.5 }}
                            slotProps={{
                              input: {
                                sx: { borderRadius: 2 },
                                endAdornment: (
                                  <InputAdornment position="end">
                                    <IconButton
                                      onClick={() =>
                                        setShowPassword(!showPassword)
                                      }
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
                          control={loginForm.control}
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
                                <Typography variant="body2">
                                  Remember me
                                </Typography>
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
                  )}
                </Box>
              </Fade>

              {/* ============ REGISTER FORM ============ */}
              <Fade in={tab === 1} timeout={300} unmountOnExit>
                <Box>
                  {tab === 1 && (
                    <Box
                      component="form"
                      onSubmit={registerForm.handleSubmit(onRegisterSubmit)}
                      noValidate
                    >
                      {/* Name fields */}
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
                            control={registerForm.control}
                            render={({ field }) => (
                              <TextField
                                {...field}
                                fullWidth
                                label="First Name"
                                autoComplete="given-name"
                                autoFocus={tab === 1}
                                error={
                                  !!registerForm.formState.errors.firstName
                                }
                                helperText={
                                  registerForm.formState.errors.firstName
                                    ?.message
                                }
                                disabled={isLoading || !!regSuccess}
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
                            control={registerForm.control}
                            render={({ field }) => (
                              <TextField
                                {...field}
                                fullWidth
                                label="Last Name"
                                autoComplete="family-name"
                                error={
                                  !!registerForm.formState.errors.lastName
                                }
                                helperText={
                                  registerForm.formState.errors.lastName
                                    ?.message
                                }
                                disabled={isLoading || !!regSuccess}
                                slotProps={{
                                  input: { sx: { borderRadius: 2 } },
                                }}
                              />
                            )}
                          />
                        </Box>
                      </Box>

                      <Controller
                        name="email"
                        control={registerForm.control}
                        render={({ field }) => (
                          <TextField
                            {...field}
                            fullWidth
                            label="Email Address"
                            type="email"
                            autoComplete="email"
                            error={!!registerForm.formState.errors.email}
                            helperText={
                              registerForm.formState.errors.email?.message
                            }
                            disabled={isLoading || !!regSuccess}
                            sx={{ mb: 2.5 }}
                            slotProps={{
                              input: { sx: { borderRadius: 2 } },
                            }}
                          />
                        )}
                      />

                      <Controller
                        name="password"
                        control={registerForm.control}
                        render={({ field }) => (
                          <TextField
                            {...field}
                            fullWidth
                            label="Password"
                            type={showPassword ? 'text' : 'password'}
                            autoComplete="new-password"
                            error={!!registerForm.formState.errors.password}
                            helperText={
                              registerForm.formState.errors.password?.message
                            }
                            disabled={isLoading || !!regSuccess}
                            sx={{ mb: 2.5 }}
                            slotProps={{
                              input: {
                                sx: { borderRadius: 2 },
                                endAdornment: (
                                  <InputAdornment position="end">
                                    <IconButton
                                      onClick={() =>
                                        setShowPassword(!showPassword)
                                      }
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

                      <Controller
                        name="confirmPassword"
                        control={registerForm.control}
                        render={({ field }) => (
                          <TextField
                            {...field}
                            fullWidth
                            label="Confirm Password"
                            type={showConfirmPassword ? 'text' : 'password'}
                            autoComplete="new-password"
                            error={
                              !!registerForm.formState.errors.confirmPassword
                            }
                            helperText={
                              registerForm.formState.errors.confirmPassword
                                ?.message
                            }
                            disabled={isLoading || !!regSuccess}
                            sx={{ mb: 3 }}
                            slotProps={{
                              input: {
                                sx: { borderRadius: 2 },
                                endAdornment: (
                                  <InputAdornment position="end">
                                    <IconButton
                                      onClick={() =>
                                        setShowConfirmPassword(
                                          !showConfirmPassword,
                                        )
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

                      <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        size="large"
                        disabled={isLoading || !!regSuccess}
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
                  )}
                </Box>
              </Fade>
            </Box>
          </Paper>

          {/* Footer */}
          <Typography
            variant="body2"
            color="text.secondary"
            textAlign="center"
            sx={{ mt: 3 }}
          >
            &copy; {new Date().getFullYear()} PowerSphere Inc. All rights
            reserved.
          </Typography>
        </Box>
      </Container>
    </Box>
  );
}
