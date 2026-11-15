import { useQuery } from '@tanstack/react-query';
import { insuranceApi } from '@/data/api';
import { formatCurrency } from '@/lib/utils';
import { Card, CardHeader, CardTitle, CardContent, CardDescription } from '@/presentation/components/ui/card';
import { Shield } from 'lucide-react';

export default function InsurancePage() {
  const { data: policies, isLoading } = useQuery({
    queryKey: ['insurance'],
    queryFn: () => insuranceApi.getAll().then((r) => r.data),
  });

  if (isLoading) return <div className="flex justify-center p-12 text-muted-foreground">Loading policies...</div>;

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Insurance Policies</h1>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {policies?.map((policy) => (
          <Card key={policy.id}>
            <CardHeader>
              <div className="flex items-center gap-2">
                <Shield className="h-5 w-5 text-teal-600" />
                <CardTitle className="text-base">{policy.policyName}</CardTitle>
              </div>
              <CardDescription>{policy.provider} • {policy.policyNumber}</CardDescription>
            </CardHeader>
            <CardContent className="space-y-2 text-sm">
              <div className="flex justify-between">
                <span className="text-muted-foreground">Type</span>
                <span className="font-medium">{policy.type.replace('_', ' ')}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-muted-foreground">Premium</span>
                <span className="font-medium">{formatCurrency(policy.premium)}/yr</span>
              </div>
              <div className="flex justify-between">
                <span className="text-muted-foreground">Sum Assured</span>
                <span className="font-medium text-teal-600">{formatCurrency(policy.sumAssured)}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-muted-foreground">Period</span>
                <span className="text-xs">{policy.startDate} → {policy.endDate}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-muted-foreground">Nominee</span>
                <span>{policy.nominee}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-muted-foreground">Status</span>
                <span className={`px-2 py-0.5 rounded-full text-xs font-medium ${policy.status === 'ACTIVE' ? 'bg-green-100 text-green-700' : 'bg-gray-100 text-gray-600'}`}>
                  {policy.status}
                </span>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  );
}