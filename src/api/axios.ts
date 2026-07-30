import axios, {
  type AxiosError,
  type InternalAxiosRequestConfig,
} from 'axios';
import { config } from '@/config';
import { tokenUtils } from '@/utils';

let isRefreshing = false;
let failedQueue: Array<{
  resolve: (token: string) => void;
  reject: (error: unknown) => void;
}> = [];

const processQueue = (error: unknown, token: string | null = null): void => {
  failedQueue.forEach((prom) => {
    if (error) {
      prom.reject(error);
    } else {
      prom.resolve(token!);
    }
  });
  failedQueue = [];
};

export const apiClient = axios.create({
  baseURL: config.api.baseUrl,
  timeout: config.api.timeout,
  headers: {
    'Content-Type': 'application/json',
  },
});

apiClient.interceptors.request.use(
  (requestConfig: InternalAxiosRequestConfig) => {
    const accessToken = tokenUtils.getAccessToken();
    if (accessToken && requestConfig.headers) {
      requestConfig.headers.Authorization = `${config.auth.tokenType} ${accessToken}`;
    }
    return requestConfig;
  },
  (error: AxiosError) => Promise.reject(error),
);

apiClient.interceptors.response.use(
  (response) => response,
  async (error: AxiosError) => {
    const originalRequest = error.config as InternalAxiosRequestConfig & {
      _retry?: boolean;
    };

    if (!originalRequest) {
      return Promise.reject(error);
    }

    if (error.response?.status === 401 && !originalRequest._retry) {
      if (isRefreshing) {
        return new Promise<string>((resolve, reject) => {
          failedQueue.push({ resolve, reject });
        })
          .then((token) => {
            originalRequest.headers.Authorization = `${config.auth.tokenType} ${token}`;
            return apiClient(originalRequest);
          })
          .catch((err) => Promise.reject(err));
      }

      originalRequest._retry = true;
      isRefreshing = true;

      const refreshToken = tokenUtils.getRefreshToken();

      if (!refreshToken) {
        tokenUtils.clearAll();
        window.location.href = '/login';
        return Promise.reject(error);
      }

      try {
        const response = await axios.post(
          `${config.api.baseUrl}/auth/refresh`,
          { refreshToken },
        );

        const { accessToken: newAccessToken, refreshToken: newRefreshToken } =
          response.data;

        tokenUtils.setAccessToken(newAccessToken);
        tokenUtils.setRefreshToken(newRefreshToken);

        processQueue(null, newAccessToken);

        originalRequest.headers.Authorization = `${config.auth.tokenType} ${newAccessToken}`;
        return apiClient(originalRequest);
      } catch (refreshError) {
        processQueue(refreshError, null);
        tokenUtils.clearAll();
        window.location.href = '/login';
        return Promise.reject(refreshError);
      } finally {
        isRefreshing = false;
      }
    }

    return Promise.reject(error);
  },
);
