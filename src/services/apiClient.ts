type HttpMethod = 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE';

interface ApiClientOptions {
  baseUrl?: string;
  headers?: Record<string, string>;
}

class ApiClient {
  private baseUrl: string;
  private defaultHeaders: Record<string, string>;

  constructor(options: ApiClientOptions = {}) {
    this.baseUrl = options.baseUrl || '/api/v1';
    this.defaultHeaders = {
      'Content-Type': 'application/json',
      ...options.headers,
    };
  }

  private getAuthToken(): string | null {
    return localStorage.getItem('access_token');
  }

  private getHeaders(): Record<string, string> {
    const token = this.getAuthToken();
    return {
      ...this.defaultHeaders,
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
    };
  }

  private async request<T>(
    method: HttpMethod,
    endpoint: string,
    body?: unknown,
    options?: { isBlob?: boolean }
  ): Promise<T> {
    const url = `${this.baseUrl}${endpoint}`;
    const headers = this.getHeaders();

    const response = await fetch(url, {
      method,
      headers: options?.isBlob ? {} : headers,
      ...(body ? { body: JSON.stringify(body) } : {}),
    });

    if (!response.ok) {
      const errorMessage = await response
        .text()
        .catch(() => `HTTP error ${response.status}`);
      throw new Error(errorMessage || `API Error: ${response.status} ${response.statusText}`);
    }

    if (options?.isBlob) {
      return response.blob() as Promise<T>;
    }

    return response.json();
  }

  async get<T>(
    endpoint: string,
    params?: Record<string, string>
  ): Promise<T> {
    const searchParams = new URLSearchParams(params);
    const queryString = searchParams.toString();
    const url = queryString ? `${endpoint}?${queryString}` : endpoint;
    return this.request<T>('GET', url);
  }

  async post<T>(endpoint: string, body?: unknown): Promise<T> {
    return this.request<T>('POST', endpoint, body);
  }

  async put<T>(endpoint: string, body?: unknown): Promise<T> {
    return this.request<T>('PUT', endpoint, body);
  }

  async patch<T>(endpoint: string, body?: unknown): Promise<T> {
    return this.request<T>('PATCH', endpoint, body);
  }

  async delete<T>(endpoint: string): Promise<T> {
    return this.request<T>('DELETE', endpoint);
  }

  async downloadBlob(endpoint: string): Promise<Blob> {
    return this.request<Blob>('GET', endpoint, undefined, { isBlob: true });
  }

  getDownloadUrl(endpoint: string): string {
    const token = this.getAuthToken();
    return `${this.baseUrl}${endpoint}${token ? `?token=${token}` : ''}`;
  }
}

export const apiClient = new ApiClient();

export default apiClient;
