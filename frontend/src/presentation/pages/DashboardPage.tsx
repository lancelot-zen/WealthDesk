import { useQuery } from '@tanstack/react-query';
import { dashboardApi } from '@/data/api';
import { formatCurrency } from '@/lib/utils';
import { Card, CardHeader, CardTitle, CardContent } from '@/presentation/components/ui/card';
import { TrendingUp, TrendingDown, Briefcase, BarChart3, Shield, IndianRupee } from 'lucide-react';

export default function DashboardPage() {
  const { data: dashboard, isLoading } = useQuery({
    queryKey: ['dashboard'],
    queryFn: () => dashboardApi.get().then((r) => r.data),
  });

  if (isLoading) return <div className="flex justify-center p-12 text-muted-foreground">Loading dashboard...</div>;
  if (!dashboard) return null;

  const cards = [
    { title: 'Portfolio Value', value: formatCurrency(dashboard.totalCurrentValue), icon: IndianRupee, color: 'text-blue-600' },
    { title: 'Invested Value', value: formatCurrency(dashboard.totalInvestedValue), icon: Briefcase, color: 'text-slate-600' },
    { title: 'Unrealized PnL', value: formatCurrency(dashboard.totalUnrealizedPnl), icon: dashboard.totalUnrealizedPnl >= 0 ? TrendingUp : TrendingDown, color: dashboard.totalUnrealizedPnl >= 0 ? 'text-green-600' : 'text-red-600' },
    { title: 'Realized PnL', value: formatCurrency(dashboard.totalRealizedPnl), icon: BarChart3, color: dashboard.totalRealizedPnl >= 0 ? 'text-green-600' : 'text-red-600' },
    { title: 'Holdings', value: String(dashboard.totalHoldings), icon: Briefcase, color: 'text-purple-600' },
    { title: 'Total Trades', value: String(dashboard.totalTrades), icon: BarChart3, color: 'text-orange-600' },
    { title: 'Active Policies', value: String(dashboard.activePolicies), icon: Shield, color: 'text-teal-600' },
    { title: 'Insurance Coverage', value: formatCurrency(dashboard.totalInsuranceCoverage), icon: Shield, color: 'text-indigo-600' },
  ];

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Dashboard</h1>
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {cards.map((card) => (
          <Card key={card.title}>
            <CardHeader className="flex flex-row items-center justify-between pb-2">
              <CardTitle className="text-sm font-medium text-muted-foreground">{card.title}</CardTitle>
              <card.icon className={`h-5 w-5 ${card.color}`} />
            </CardHeader>
            <CardContent>
              <div className={`text-xl font-bold ${card.color}`}>{card.value}</div>
            </CardContent>
          </Card>
        ))}
      </div>
      {dashboard.portfolioPnlPercentage !== 0 && (
        <div className="mt-6 p-4 rounded-lg bg-muted/50">
          <span className="text-sm text-muted-foreground">Portfolio Return: </span>
          <span className={`text-lg font-bold ${dashboard.portfolioPnlPercentage >= 0 ? 'text-green-600' : 'text-red-600'}`}>
            {dashboard.portfolioPnlPercentage >= 0 ? '+' : ''}{dashboard.portfolioPnlPercentage}%
          </span>
        </div>
      )}
    </div>
  );
}