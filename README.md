# WealthDesk

A full-stack fintech trading platform for generating PnL, Holdings, and Insurance reports.

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Spring Boot 3.2 (Java 17) |
| Databases | PostgreSQL 16 + MongoDB 7 |
| Caching | Redis 7 |
| Reports | Apache POI (Excel) + OpenPDF (PDF) |
| Frontend | React 18 + TypeScript + Vite 5 |
| UI | Tailwind CSS 3 + shadcn/ui |
| State | Zustand + TanStack Query v5 |
| Auth | JWT (Spring Security + jjwt 0.12.5) |
| API Docs | SpringDoc OpenAPI (Swagger) |
| CI | GitHub Actions |

## Project Structure

```
WealthDesk/
├── backend/                    # Spring Boot REST API
│   └── src/main/java/com/wealthdesk/
│       ├── config/             # Security, Redis, OpenAPI configs
│       ├── controller/         # REST controllers (Auth, Trade, Holding, Insurance, Dashboard, Report)
│       ├── document/           # MongoDB documents (InsurancePolicy)
│       ├── dto/                # Request/Response DTOs
│       ├── entity/             # JPA entities (User, Trade, Holding)
│       ├── exception/          # Global exception handler
│       ├── report/             # Excel & PDF report generators
│       ├── repository/         # Spring Data repositories
│       ├── security/           # JWT provider, filter, UserDetailsService
│       ├── seed/               # DataSeeder (demo data)
│       └── service/            # Business logic services
├── frontend/                   # React SPA (Clean Architecture)
│   └── src/
│       ├── core/               # TypeScript interfaces & types
│       ├── data/               # API client (Axios), auth store (Zustand)
│       ├── lib/                # Utility functions (cn, formatCurrency)
│       └── presentation/       # UI layer
│           ├── components/     # Layout, shadcn/ui components
│           └── pages/          # Dashboard, Trades, Holdings, Insurance, Reports, Login, Register
├── .github/workflows/ci.yml   # GitHub Actions CI
├── docker-compose.yml          # PostgreSQL + MongoDB + Redis
└── README.md
```

## Getting Started

### Prerequisites
- Java 17+
- Node.js 18+
- Docker & Docker Compose

### 1. Start Infrastructure
```bash
docker-compose up -d
```
This starts PostgreSQL (port 5432), MongoDB (port 27017), and Redis (port 6379).

### 2. Run Backend
```bash
cd backend
./mvnw spring-boot:run
```
The backend starts on **http://localhost:8080**. On first run, it seeds the database with demo data.

### 3. Run Frontend
```bash
cd frontend
npm install
npm run dev
```
The frontend starts on **http://localhost:5173** with API proxy to the backend.

### Demo Credentials
| Username | Password | Roles |
|----------|----------|-------|
| `rahul` | `password123` | USER |
| `priya` | `password123` | USER, ADMIN |

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login (returns JWT) |
| GET | `/api/dashboard` | Portfolio summary |
| GET | `/api/trades` | All trades |
| POST | `/api/trades` | Create trade |
| GET | `/api/holdings` | All holdings |
| GET | `/api/insurance` | All insurance policies |
| POST | `/api/insurance` | Create policy |
| GET | `/api/reports/{type}?format=excel\|pdf` | Download report |

**Swagger UI:** http://localhost:8080/swagger-ui.html

## Reports

Download reports in **Excel (.xlsx)** or **PDF** format:
- **PnL Report** — Trade history with realized profit/loss
- **Holdings Report** — Portfolio with unrealized PnL and sector breakdown
- **Insurance Report** — Policy details with coverage and status

## Seed Data

The `DataSeeder` auto-populates on first run:
- 2 users, 25 trades (12 NSE symbols), 13 holdings, 7 insurance policies
- Covers: RELIANCE, TCS, INFY, HDFCBANK, WIPRO, ICICIBANK, SBIN, BAJFINANCE, MARUTI, TATAMOTORS, ASIANPAINT, TITAN, NESTLEIND, SUNPHARMA

## License
MIT