import { useState } from 'react';
import { reportApi } from '@/data/api';
import { Button } from '@/presentation/components/ui/button';
import { Card, CardHeader, CardTitle, CardContent, CardDescription } from '@/presentation/components/ui/card';
import { FileSpreadsheet, FileText, Download } from 'lucide-react';

const reportTypes = [
  { key: 'pnl', label: 'PnL Report', description: 'Profit & Loss from all trades', icon: FileSpreadsheet },
  { key: 'holdings', label: 'Holdings Report', description: 'Current portfolio holdings with unrealized PnL', icon: FileSpreadsheet },
  { key: 'insurance', label: 'Insurance Report', description: 'All insurance policies and coverage details', icon: FileText },
];

export default function ReportsPage() {
  const [downloading, setDownloading] = useState<string | null>(null);

  const handleDownload = async (type: string, format: string) => {
    setDownloading(`${type}-${format}`);
    try {
      await reportApi.download(type, format);
    } catch (err) {
      console.error('Download failed:', err);
    } finally {
      setDownloading(null);
    }
  };

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Reports</h1>
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {reportTypes.map((report) => (
          <Card key={report.key}>
            <CardHeader>
              <div className="flex items-center gap-2">
                <report.icon className="h-5 w-5 text-primary" />
                <CardTitle className="text-lg">{report.label}</CardTitle>
              </div>
              <CardDescription>{report.description}</CardDescription>
            </CardHeader>
            <CardContent className="flex gap-3">
              <Button
                variant="outline"
                size="sm"
                onClick={() => handleDownload(report.key, 'excel')}
                disabled={downloading === `${report.key}-excel`}
                className="flex-1"
              >
                <Download className="h-4 w-4 mr-1" />
                {downloading === `${report.key}-excel` ? 'Downloading...' : 'Excel'}
              </Button>
              <Button
                variant="outline"
                size="sm"
                onClick={() => handleDownload(report.key, 'pdf')}
                disabled={downloading === `${report.key}-pdf`}
                className="flex-1"
              >
                <Download className="h-4 w-4 mr-1" />
                {downloading === `${report.key}-pdf` ? 'Downloading...' : 'PDF'}
              </Button>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  );
}