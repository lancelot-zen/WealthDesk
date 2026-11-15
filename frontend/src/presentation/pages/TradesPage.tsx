import { useQuery } from '@tanstack/react-query';
import { tradeApi } from '@/data/api';
import { formatCurrency } from '@/lib/utils';
import { Card, CardHeader, CardTitle, CardContent } from '@/presentation/components/ui/card';

export default function TradesPage() {
  const { data: trades, isLoading } = useQuery({
    queryKey: ['trades'],
    queryFn: () => tradeApi.getAll().then((r) => r.data),
  });

  if (isLoading) return <div className="flex justify-center p-12 text-muted-foreground">Loading trades...</div>;

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Trades</h1>
      <Card>
        <CardHeader><CardTitle className="text-lg">Trade History</CardTitle></CardHeader>
        <CardContent>
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b bg-muted/50">
                  <th className="text-left p-3 font-medium">Date</th>
                  <th className="text-left p-3 font-medium">Symbol</th>
                  <th className="text-left p-3 font-medium">Name</th>
                  <th className="text-center p-3 font-medium">Type</th>
                  <th className="text-right p-3 font-medium">Qty</th>
                  <th className="text-right p-3 font-medium">Buy Price</th>
                  <th className="text-right p-3 font-medium">Sell Price</th>
                  <th className="text-right p-3 font-medium">PnL</th>
                </tr>
              </thead>
              <tbody>
                {trades?.map((trade) => (
                  <tr key={trade.id} className="border-b hover:bg-muted/30 transition-colors">
                    <td className="p-3">{trade.tradeDate}</td>
                    <td className="p-3 font-semibold">{trade.symbol}</td>
                    <td className="p-3 text-muted-foreground">{trade.name}</td>
                    <td className="p-3 text-center">
                      <span className={`px-2 py-1 rounded-full text-xs font-medium ${trade.type === 'BUY' ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}`}>
                        {trade.type}
                      </span>
                    </td>
                    <td className="p-3 text-right">{trade.quantity}</td>
                    <td className="p-3 text-right">{formatCurrency(trade.buyPrice)}</td>
                    <td className="p-3 text-right">{trade.sellPrice ? formatCurrency(trade.sellPrice) : '—'}</td>
                    <td className={`p-3 text-right font-medium ${trade.realizedPnl > 0 ? 'text-green-600' : trade.realizedPnl < 0 ? 'text-red-600' : 'text-muted-foreground'}`}>
                      {trade.realizedPnl !== 0 ? formatCurrency(trade.realizedPnl) : '—'}
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