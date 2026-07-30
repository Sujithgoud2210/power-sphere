import { Provider } from 'react-redux';
import { PersistGate } from 'redux-persist/integration/react';
import { BrowserRouter } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { store, persistor } from '@/store';
import { ThemeContextProvider } from '@/contexts/ThemeContext';
import { ErrorBoundary } from '@/components/common';
import { AppRouter } from '@/routes';
import { Loading } from '@/components/common';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: 2,
      refetchOnWindowFocus: false,
      staleTime: 5 * 60 * 1000,
    },
    mutations: {
      retry: 1,
    },
  },
});

export default function App() {
  return (
    <Provider store={store}>
      <PersistGate loading={<Loading fullPage message="Loading..." />} persistor={persistor}>
        <QueryClientProvider client={queryClient}>
          <BrowserRouter>
            <ThemeContextProvider>
              <ErrorBoundary>
                <AppRouter />
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
                />
              </ErrorBoundary>
            </ThemeContextProvider>
          </BrowserRouter>
        </QueryClientProvider>
      </PersistGate>
    </Provider>
  );
}
