import { create } from 'zustand';
import type { AuthResponse } from '@/core/types';

interface AuthState {
  token: string | null;
  user: Omit<AuthResponse, 'token' | 'type'> | null;
  isAuthenticated: boolean;
  login: (response: AuthResponse) => void;
  logout: () => void;
}

export const useAuthStore = create<AuthState>((set) => ({
  token: localStorage.getItem('token'),
  user: JSON.parse(localStorage.getItem('user') || 'null'),
  isAuthenticated: !!localStorage.getItem('token'),

  login: (response: AuthResponse) => {
    localStorage.setItem('token', response.token);
    const user = {
      id: response.id,
      username: response.username,
      email: response.email,
      fullName: response.fullName,
      roles: response.roles,
    };
    localStorage.setItem('user', JSON.stringify(user));
    set({ token: response.token, user, isAuthenticated: true });
  },

  logout: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    set({ token: null, user: null, isAuthenticated: false });
  },
}));