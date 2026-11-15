# WealthDesk

A full-stack fintech trading platform for generating PnL, Holdings, and Insurance reports.

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Spring Boot 3.2 (Java 17) |
| Databases | PostgreSQL + MongoDB |
| Caching | Redis |
| Reports | Apache POI (Excel) + OpenPDF/iText (PDF) |
| Frontend | React + TypeScript + Vite |
| UI | Tailwind CSS + shadcn/ui |
| Auth | JWT (Spring Security) |
| API Docs | SpringDoc OpenAPI (Swagger) |

## Project Structure

```
WealthDesk/
├── backend/          # Spring Boot REST API
├── frontend/         # React SPA (Clean Architecture)
├── docker-compose.yml
└── README.md
```

## Getting Started

### Prerequisites
- Java 17+
- Node.js 18+
- Docker & Docker Compose

### Infrastructure
```bash
docker-compose up -d
```

### Backend
```bash
cd backend
./mvnw spring-boot:run
```

### Frontend
```bash
cd frontend
npm install
npm run dev
```

## API Documentation
Once the backend is running, visit: http://localhost:8080/swagger-ui.html

## License
MIT