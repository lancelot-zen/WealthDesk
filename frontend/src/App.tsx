import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useAuthStore } from '@/data/authStore';
import Layout from '@/presentation/components/Layout';
import LoginPage from '@/presentation/pages/LoginPage';
import RegisterPage from '@/presentation/pages/RegisterPage';
import DashboardPage from '@/presentation/pages/DashboardPage';
import TradesPage from '@/presentation/pages/TradesPage';
import HoldingsPage from '@/presentation/pages/HoldingsPage';
import InsurancePage from '@/presentation/pages/InsurancePage';
import ReportsPage from '@/presentation/pages/ReportsPage';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 5 * 60 * 1000,
      retry: 1,
    },
  },
});

function ProtectedRoute({ children }: { children: React.ReactNode }) {
  const isAuthenticated = useAuthStore((s) => s.isAuthenticated);
  return isAuthenticated ? <>{children}</> : <Navigate to="/login" replace />;
}

export default function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route
            path="/"
            element={
              <ProtectedRoute>
                <Layout />
              </ProtectedRoute>
            }
          >
            <Route index element={<DashboardPage />} />
            <Route path="trades" element={<TradesPage />} />
            <Route path="holdings" element={<HoldingsPage />} />
            <Route path="insurance" element={<InsurancePage />} />
            <Route path="reports" element={<ReportsPage />} />
          </Route>
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </BrowserRouter>
    </QueryClientProvider>
  );
}