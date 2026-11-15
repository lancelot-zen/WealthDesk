import axios from 'axios';
import type { AuthResponse, LoginRequest, RegisterRequest, Trade, Holding, InsurancePolicy, Dashboard } from '@/core/types';

const api = axios.create({
  baseURL: '/api',
  headers: { 'Content-Type': 'application/json' },
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Auth
export const authApi = {
  login: (data: LoginRequest) => api.post<AuthResponse>('/auth/login', data),
  register: (data: RegisterRequest) => api.post<AuthResponse>('/auth/register', data),
};

// Trades
export const tradeApi = {
  getAll: () => api.get<Trade[]>('/trades'),
  getById: (id: number) => api.get<Trade>(`/trades/${id}`),
  create: (data: Partial<Trade>) => api.post<Trade>('/trades', data),
};

// Holdings
export const holdingApi = {
  getAll: () => api.get<Holding[]>('/holdings'),
};

// Insurance
export const insuranceApi = {
  getAll: () => api.get<InsurancePolicy[]>('/insurance'),
  create: (data: Partial<InsurancePolicy>) => api.post<InsurancePolicy>('/insurance', data),
};

// Dashboard
export const dashboardApi = {
  get: () => api.get<Dashboard>('/dashboard'),
};

// Reports
export const reportApi = {
  download: async (type: string, format: string) => {
    const response = await api.get(`/reports/${type}`, {
      params: { format },
      responseType: 'blob',
    });
    const url = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement('a');
    link.href = url;
    const ext = format === 'pdf' ? '.pdf' : '.xlsx';
    link.setAttribute('download', `${type}_report${ext}`);
    document.body.appendChild(link);
    link.click();
    link.remove();
    window.URL.revokeObjectURL(url);
  },
};

export default api;