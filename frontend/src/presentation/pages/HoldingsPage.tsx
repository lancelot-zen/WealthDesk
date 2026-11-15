import { useQuery } from '@tanstack/react-query';
import { holdingApi } from '@/data/api';
import { formatCurrency } from '@/lib/utils';
import { Card, CardHeader, CardTitle, CardContent } from '@/presentation/components/ui/card';

export default function HoldingsPage() {
  const { data: holdings, isLoading } = useQuery({
    queryKey: ['holdings'],
    queryFn: () => holdingApi.getAll().then((r) => r.data),
  });

  if (isLoading) return <div className="flex justify-center p-12 text-muted-foreground">Loading holdings...</div>;

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Holdings</h1>
      <Card>
        <CardHeader><CardTitle className="text-lg">Portfolio Holdings</CardTitle></CardHeader>
        <CardContent>
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b bg-muted/50">
                  <th className="text-left p-3 font-medium">Symbol</th>
                  <th className="text-left p-3 font-medium">Name</th>
                  <th className="text-right p-3 font-medium">Qty</th>
                  <th className="text-right p-3 font-medium">Avg Buy</th>
                  <th className="text-right p-3 font-medium">Current</th>
                  <th className="text-right p-3 font-medium">Invested</th>
                  <th className="text-right p-3 font-medium">Current Val</th>
                  <th className="text-right p-3 font-medium">PnL</th>
                  <th className="text-right p-3 font-medium">PnL %</th>
                  <th className="text-left p-3 font-medium">Sector</th>
                </tr>
              </thead>
              <tbody>
                {holdings?.map((h) => (
                  <tr key={h.id} className="border-b hover:bg-muted/30 transition-colors">
                    <td className="p-3 font-semibold">{h.symbol}</td>
                    <td className="p-3 text-muted-foreground">{h.name}</td>
                    <td className="p-3 text-right">{h.quantity}</td>
                    <td className="p-3 text-right">{formatCurrency(h.avgBuyPrice)}</td>
                    <td className="p-3 text-right">{formatCurrency(h.currentPrice)}</td>
                    <td className="p-3 text-right">{formatCurrency(h.investedValue)}</td>
                    <td className="p-3 text-right">{formatCurrency(h.currentValue)}</td>
                    <td className={`p-3 text-right font-medium ${h.unrealizedPnl >= 0 ? 'text-green-600' : 'text-red-600'}`}>
                      {formatCurrency(h.unrealizedPnl)}
                    </td>
                    <td className={`p-3 text-right font-medium ${h.pnlPercentage >= 0 ? 'text-green-600' : 'text-red-600'}`}>
                      {h.pnlPercentage >= 0 ? '+' : ''}{h.pnlPercentage}%
                    </td>
                    <td className="p-3">
                      <span className="px-2 py-1 rounded-full text-xs bg-muted">{h.sector}</span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}