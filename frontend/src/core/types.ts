export interface AuthResponse {
  token: string;
  type: string;
  id: number;
  username: string;
  email: string;
  fullName: string;
  roles: string[];
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  fullName: string;
}

export interface Trade {
  id: number;
  symbol: string;
  name: string;
  type: 'BUY' | 'SELL';
  quantity: number;
  buyPrice: number;
  sellPrice: number | null;
  tradeDate: string;
  exchange: string;
  realizedPnl: number;
}

export interface Holding {
  id: number;
  symbol: string;
  name: string;
  quantity: number;
  avgBuyPrice: number;
  currentPrice: number;
  exchange: string;
  sector: string;
  investedValue: number;
  currentValue: number;
  unrealizedPnl: number;
  pnlPercentage: number;
}

export interface InsurancePolicy {
  id: string;
  policyNumber: string;
  policyName: string;
  provider: string;
  type: string;
  premium: number;
  sumAssured: number;
  startDate: string;
  endDate: string;
  nominee: string;
  status: string;
}

export interface Dashboard {
  totalInvestedValue: number;
  totalCurrentValue: number;
  totalUnrealizedPnl: number;
  totalRealizedPnl: number;
  portfolioPnlPercentage: number;
  totalHoldings: number;
  totalTrades: number;
  activePolicies: number;
  totalInsuranceCoverage: number;
}