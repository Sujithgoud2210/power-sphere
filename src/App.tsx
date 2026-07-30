import { Suspense, lazy } from 'react';
import { Provider } from 'react-redux';
import { PersistGate } from 'redux-persist/integration/react';
import { BrowserRouter } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { store, persistor } from '@/store';
import { ThemeContextProvider } from '@/contexts/ThemeContext';
import { ErrorBoundary, Loading } from '@/components/common';

const AppRouter = lazy(() =>
  import('@/routes').then((module) => ({ default: module.AppRouter })),
);

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: 2,
      retryDelay: (attemptIndex) => Math.min(1000 * 2 ** attemptIndex, 10000),
      refetchOnWindowFocus: false,
      staleTime: 5 * 60 * 1000,
      gcTime: 10 * 60 * 1000,
      refetchOnReconnect: true,
    },
    mutations: {
      retry: 1,
    },
  },
});

export default function App() {
  return (
    <Provider store={store}>
      <PersistGate loading={<Loading fullPage message="Loading PowerSphere..." />} persistor={persistor}>
        <QueryClientProvider client={queryClient}>
          <BrowserRouter>
            <ThemeContextProvider>
              <ErrorBoundary fullPage>
                <Suspense fallback={<Loading fullPage message="Loading application..." />}>
                  <AppRouter />
                </Suspense>
                <ToastContainer
                  position="top-right"
                  autoClose={4000}
                  hideProgressBar={false}
                  newestOnTop
                  closeOnClick
                  pauseOnFocusLoss
                  draggable
                  pauseOnHover
                  theme="colored"
                  limit={3}
                  stacked
                  style={{ borderRadius: 12 }}
                />
              </ErrorBoundary>
            </ThemeContextProvider>
          </BrowserRouter>
        </QueryClientProvider>
      </PersistGate>
    </Provider>
  );
}
