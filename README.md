
# ⚡ PowerSphere – Enterprise Smart Energy Management Platform

[![CI/CD Pipeline](https://github.com/powersphere/power-sphere/actions/workflows/ci.yml/badge.svg)](https://github.com/powersphere/power-sphere/actions/workflows/ci.yml)
[![Java Version](https://img.shields.io/badge/Java-21-blue.svg)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-19-61DAFB.svg)](https://react.dev/)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)

---

## 📋 Table of Contents

- [Project Overview](#-project-overview)
- [Architecture](#-architecture)
- [Technology Stack](#-technology-stack)
- [System Requirements](#-system-requirements)
- [Local Development Setup](#-local-development-setup)
- [Docker Deployment](#-docker-deployment)
- [Environment Variables](#-environment-variables)
- [API Documentation](#-api-documentation)
- [Monitoring](#-monitoring)
- [CI/CD Pipeline](#-cicd-pipeline)
- [Project Structure](#-project-structure)
- [Screenshots](#-screenshots)
- [Security](#-security)
- [Contributing](#-contributing)

---

## 📖 Project Overview

PowerSphere is an **Enterprise Smart Energy Management Platform** designed to monitor, analyze, and optimize energy consumption across organizations. It provides real-time tracking, billing management, meter management, and comprehensive analytics dashboards.

### Key Features

- **Real-time Energy Monitoring** – Track energy consumption across meters and organizations
- **Smart Meter Management** – Register, assign, and monitor smart meters
- **Automated Billing** – Generate and manage tariff plans, bills, and payments
- **Advanced Analytics** – Interactive dashboards with consumption trends, revenue analytics, and KPI tracking
- **Notification System** – Configurable alerts and notification channels (email, SMS, in-app)
- **Organization Management** – Multi-level hierarchy (Organization → Department → Team)
- **User Management** – Role-based access control with granular permissions
- **RESTful API** – Comprehensive API with OpenAPI/Swagger documentation

---

## 🏗️ Architecture

### High-Level Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                         Load Balancer / Nginx                         │
│                    (Reverse Proxy, SSL, Compression)                  │
└─────────────────────────────────────────────────────────────────────┘
                                    │
              ┌─────────────────────┼─────────────────────┐
              ▼                     ▼                      ▼
   ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐
   │   React SPA       │  │   Spring Boot    │  │   Prometheus     │
   │   (Nginx)         │  │   REST API       │  │   + Grafana      │
   │   Port 80         │  │   Port 8080      │  │   Port 9090/3000 │
   └──────────────────┘  └──────────────────┘  └──────────────────┘
                                  │                      │
                    ┌─────────────┼─────────────┐        │
                    ▼             ▼             ▼        │
           ┌────────────┐ ┌────────────┐ ┌─────────┐    │
           │  MySQL 8.4 │ │   Redis    │ │ MailHog │    │
           │ Database   │ │   Cache    │ │  Email  │    │
           └────────────┘ └────────────┘ └─────────┘    │
                    │             │                      │
                    └─────────────┘──────────────────────┘
```

### Deployment Diagram

```
┌──────────────────────────────────────────────────────────────┐
│                      Docker Host                              │
│  ┌───────────────────────────────┐                            │
│  │   powersphere-network         │                            │
│  │  (172.20.0.0/16)              │                            │
│  │                               │                            │
│  │  ┌──────┐ ┌──────┐ ┌──────┐  │                            │
│  │  │Backend│ │Front │ │Prom  │  │                            │
│  │  │:8080 │ │ :80  │ │:9090 │  │                            │
│  │  └──────┘ └──────┘ └──────┘  │                            │
│  │  ┌──────┐ ┌──────┐ ┌──────┐  │                            │
│  │  │MySQL │ │Redis │ │Graf  │  │                            │
│  │  │:3306 │ │:6379 │ │:3000 │  │                            │
│  │  └──────┘ └──────┘ └──────┘  │                            │
│  └───────────────────────────────┘                            │
└──────────────────────────────────────────────────────────────┘
```

### Component Diagram

- **Frontend**: React 19 SPA with MUI, Redux Toolkit, React Query, Recharts
- **Backend**: Spring Boot 3.5.0 modular monolith with clean architecture
- **Database**: MySQL 8.4 with HikariCP connection pooling
- **Cache**: Redis 7.4 for session caching and data optimization
- **Reverse Proxy**: Nginx 1.27 with compression, caching, and security headers
- **Monitoring**: Prometheus + Grafana with pre-configured dashboards
- **Email**: MailHog for email testing (development)

### ER Diagram

```
┌──────────┐     ┌──────────┐     ┌──────────┐
│   User   │────▶│  Role    │────▶│Permission│
└──────────┘     └──────────┘     └──────────┘
     │
     ▼
┌──────────┐     ┌──────────┐     ┌──────────┐
│  Org     │────▶│Dept      │────▶│  Team    │
└──────────┘     └──────────┘     └──────────┘
     │                                │
     ▼                                ▼
┌──────────┐     ┌──────────┐     ┌──────────┐
│  Meter   │────▶│  Energy  │     │   Bill   │
│          │     │ Reading  │     │          │
└──────────┘     └──────────┘     └──────────┘
     │                                │
     ▼                                ▼
┌──────────┐     ┌──────────┐     ┌──────────┐
│TariffPlan│     │Notificatn│     │BillHistory│
└──────────┘     └──────────┘     └──────────┘
```

---

## 🛠️ Technology Stack

### Backend

| Technology        | Version | Purpose                        |
|-------------------|---------|--------------------------------|
| Java              | 21      | Runtime platform               |
| Spring Boot       | 3.5.0   | Application framework          |
| Spring Security   | 6.5.0   | Authentication & authorization |
| Spring Data JPA   | 3.5.0   | Database ORM                   |
| Spring Data Redis | 3.5.0   | Redis cache integration        |
| MySQL             | 8.4     | Primary database               |
| Redis             | 7.4     | Caching layer                  |
| JWT (jjwt)        | 0.13    | Token-based authentication     |
| MapStruct         | 1.6.3   | Object mapping                 |
| Lombok            | 1.18.46 | Boilerplate reduction          |
| SpringDoc OpenAPI | 2.8.6   | API documentation              |
| HikariCP          | -       | Connection pooling             |
| JUnit 5           | -       | Testing framework              |
| Mockito           | -       | Mocking framework              |

### Frontend

| Technology        | Version | Purpose                        |
|-------------------|---------|--------------------------------|
| React             | 19      | UI library                     |
| TypeScript        | 5.6     | Type-safe JavaScript           |
| Vite              | 6.0     | Build tool                     |
| MUI (Material)    | 6.1     | Component library              |
| Redux Toolkit     | 2.3     | State management               |
| React Query       | 5.59    | Server state management        |
| React Router      | 6.27    | Routing                        |
| React Hook Form   | 7.53    | Form management                |
| Zod               | 3.23    | Schema validation              |
| Recharts          | 3.10    | Charts & visualization         |
| Axios             | 1.7     | HTTP client                    |
| Day.js            | 1.11    | Date handling                  |
| React Toastify    | 10.0    | Notifications                  |

### DevOps & Infrastructure

| Technology       | Version | Purpose                        |
|------------------|---------|--------------------------------|
| Docker           | 24+     | Containerization               |
| Docker Compose   | 2.20+   | Multi-container orchestration  |
| Nginx            | 1.27    | Reverse proxy & static serving |
| Prometheus       | Latest  | Metrics collection             |
| Grafana          | Latest  | Metrics visualization          |
| MailHog          | Latest  | Email testing                  |
| GitHub Actions   | -       | CI/CD pipeline                 |
| Trivy            | -       | Security vulnerability scanner |

---

## 💻 System Requirements

- **Java**: JDK 21 (Temurin recommended)
- **Node.js**: 22+
- **Maven**: 3.9+
- **Docker**: 24+ with Compose V2
- **MySQL**: 8.4+
- **Redis**: 7.4+
- **OS**: Linux, macOS, or Windows with WSL2

---

## 🚀 Local Development Setup

### 1. Clone the Repository

```bash
git clone https://github.com/powersphere/power-sphere.git
cd power-sphere
```

### 2. Backend Setup

```bash
# Build the backend
mvn clean install -DskipTests

# Run tests
mvn test

# Start backend (development)
mvn spring-boot:run -Pdev
```

The backend starts at `http://localhost:8080` with H2 in-memory database.

### 3. Frontend Setup

```bash
# Install dependencies
npm install

# Start development server
npm run dev
```

The frontend starts at `http://localhost:3000` with hot reload enabled.

### 4. Access the Application

| Service      | URL                          |
|-------------|------------------------------|
| Frontend    | http://localhost:3000         |
| Backend API | http://localhost:8080/api     |
| Swagger UI  | http://localhost:8080/swagger-ui.html |
| H2 Console  | http://localhost:8080/h2-console |
| Actuator    | http://localhost:8080/actuator/health |

---

## 🐳 Docker Deployment

### Prerequisites

- Docker 24+ with Compose V2
- At least 4GB RAM allocated to Docker

### Quick Start

```bash
# 1. Copy environment configuration
cp .env.example .env
# Edit .env with your secure values

# 2. Build and start all services
docker compose up -d --build

# 3. Check service health
docker compose ps

# 4. View logs
docker compose logs -f
```

### Services

| Service    | Port  | Credentials                      |
|------------|-------|----------------------------------|
| Frontend   | 80    | -                                |
| Backend    | 8080  | -                                |
| MySQL      | 3307  | `powersphere` / `(from .env)`    |
| Redis      | 6380  | -                                |
| MailHog    | 8025  | -                                |
| Prometheus | 9090  | -                                |
| Grafana    | 3000  | `admin` / `admin`                |

### Docker Commands

```bash
# Build images
docker compose build

# Start services
docker compose up -d

# View logs
docker compose logs -f [service_name]

# Stop services
docker compose down

# Stop and remove volumes
docker compose down -v

# Restart a specific service
docker compose restart [service_name]

# Scale a service
docker compose up -d --scale backend=3

# Check health
docker compose ps
```

---

## 🔐 Environment Variables

| Variable                   | Default                    | Description                          |
|---------------------------|----------------------------|--------------------------------------|
| `SPRING_PROFILES_ACTIVE`  | `prod`                     | Active Spring profile                |
| `SERVER_PORT`             | `8080`                     | Backend server port                  |
| `DB_URL`                  | (see .env.example)         | MySQL JDBC URL                       |
| `DB_USERNAME`             | -                          | Database username                    |
| `DB_PASSWORD`             | -                          | Database password                    |
| `DB_MAX_POOL_SIZE`        | `20`                       | HikariCP max pool size               |
| `REDIS_HOST`              | `redis`                    | Redis hostname                       |
| `REDIS_PORT`              | `6379`                     | Redis port                           |
| `REDIS_PASSWORD`          | -                          | Redis password                       |
| `JWT_SECRET`              | -                          | 256-bit JWT signing secret           |
| `JWT_ACCESS_EXPIRATION`   | `900000`                   | Access token TTL (ms)                |
| `JWT_REFRESH_EXPIRATION`  | `604800000`                | Refresh token TTL (ms)               |
| `GRAFANA_ADMIN_USER`      | `admin`                    | Grafana admin username               |
| `GRAFANA_ADMIN_PASSWORD`  | `admin`                    | Grafana admin password               |
| `LOG_LEVEL_ROOT`          | `WARN`                     | Root log level                       |
| `LOG_LEVEL_APP`           | `INFO`                     | Application log level                |
| `TOMCAT_MAX_THREADS`      | `200`                      | Tomcat max thread pool               |

---

## 📚 API Documentation

API documentation is automatically generated using SpringDoc OpenAPI.

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI Spec**: http://localhost:8080/api-docs

### Main API Endpoints

| Method | Endpoint                          | Description                |
|--------|----------------------------------|----------------------------|
| POST   | `/api/auth/login`                | User login                 |
| POST   | `/api/auth/register`             | User registration          |
| POST   | `/api/auth/refresh-token`        | Refresh JWT token          |
| GET    | `/api/energy/readings`           | List energy readings       |
| POST   | `/api/energy/readings`           | Create energy reading      |
| GET    | `/api/meters`                    | List smart meters          |
| POST   | `/api/meters`                    | Register smart meter       |
| GET    | `/api/bills`                     | List bills                 |
| POST   | `/api/bills/generate`            | Generate bill              |
| GET    | `/api/dashboard/summary`         | Dashboard KPIs             |
| GET    | `/api/dashboard/consumption`     | Consumption trends         |
| GET    | `/api/notifications`             | List notifications         |
| GET    | `/api/organizations`             | List organizations         |
| GET    | `/api/users`                     | List users                 |

---

## 📊 Monitoring

### Prometheus

Prometheus scrapes metrics from the Spring Boot Actuator at `/actuator/prometheus`.

**Available metrics:**
- JVM memory, threads, garbage collection
- HTTP request rates and latencies
- Database connection pool metrics
- Custom application metrics

**Access:** http://localhost:9090

### Grafana

Grafana is pre-configured with Prometheus as a datasource and includes a **PowerSphere System Overview** dashboard.

**Key panels:**
- JVM Memory Usage (with thresholds)
- HTTP Requests per Second
- Active Database Connections
- API Response Time (P95)
- CPU Usage (System vs Process)
- JVM Thread States
- Application Uptime

**Access:** http://localhost:3000 (admin / admin)

---

## 🔄 CI/CD Pipeline

The project uses **GitHub Actions** for continuous integration and deployment.

### Pipeline Stages

1. **Backend** – Compile, test, and build Spring Boot JAR
2. **Frontend** – TypeScript check, lint, test, and build React SPA
3. **Docker** – Build and verify Docker images
4. **Security** – Vulnerability scanning with Trivy
5. **Deploy** – (Manual trigger) Deploy to production server

### Workflow Triggers

- Push to `main` and `develop` branches
- Pull requests targeting `main` and `develop`

---

## 📁 Project Structure

```
power-sphere/
├── .github/
│   └── workflows/
│       └── ci.yml                    # CI/CD pipeline
├── docker/
│   ├── backend.Dockerfile            # Multi-stage backend build
│   ├── frontend.Dockerfile           # Multi-stage frontend build
│   ├── nginx.conf                    # Nginx production config
│   ├── prometheus.yml                # Prometheus scrape config
│   └── grafana/
│       ├── datasources/
│       │   └── datasource.yml        # Grafana datasource
│       └── dashboards/
│           ├── dashboard.yml         # Dashboard provider
│           └── powersphere-overview.json  # Overview dashboard
├── scripts/
│   ├── healthcheck.sh                # Health check script
│   └── deploy.sh                     # Deployment script
├── docs/
│   ├── architecture.md               # Architecture documentation
│   └── api.md                        # API reference
├── src/
│   ├── main/
│   │   ├── java/com/powersphere/     # Backend Java source
│   │   └── resources/                # Application configs
│   ├── api/                          # Frontend API layer
│   ├── components/                   # React components
│   ├── hooks/                        # Custom React hooks
│   ├── layouts/                      # Layout components
│   ├── pages/                        # Page components
│   ├── routes/                       # Route configuration
│   ├── services/                     # API service layer
│   ├── store/                        # Redux store
│   ├── styles/                       # Global styles & theme
│   ├── types/                        # TypeScript types
│   └── utils/                        # Utility functions
├── pom.xml                          # Maven build file
├── package.json                     # Node dependencies
├── vite.config.ts                   # Vite configuration
├── tsconfig.json                    # TypeScript config
├── docker-compose.yml               # Production Docker Compose
├── .env.example                     # Environment template
└── README.md                        # This file
```

---

## 📸 Screenshots

> *Screenshots coming soon*

| Dashboard | Energy Monitoring | Billing |
|-----------|-------------------|---------|
| ![Dashboard](docs/screenshots/dashboard.png) | ![Energy](docs/screenshots/energy.png) | ![Billing](docs/screenshots/billing.png) |

---

## 🔒 Security

- **JWT Authentication** with access and refresh tokens
- **Role-Based Access Control** (RBAC) with granular permissions
- **Password Validation** with custom validators
- **CORS Configuration** for controlled cross-origin access
- **Security Headers** via Nginx (CSP, HSTS, X-Frame-Options, etc.)
- **SQL Injection Protection** via JPA parameterized queries
- **Input Validation** on all API endpoints
- **Dependency Scanning** via Trivy in CI pipeline

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Code Style

- **Backend**: Follow Java conventions, use Lombok, test with JUnit 5
- **Frontend**: Use TypeScript strictly, follow React best practices
- **Commits**: Use conventional commits (`feat:`, `fix:`, `docs:`, etc.)

---

## 📄 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

---

<p align="center">Built with ❤️ by the PowerSphere Team</p>
