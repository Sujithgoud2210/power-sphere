# PowerSphere – Architecture Documentation

## Overview

PowerSphere is designed as a **modular monolith** with clean architecture principles. The system is built for eventual migration to microservices with minimal refactoring effort. Each business domain is organized into independent packages with well-defined interfaces.

---

## High-Level Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                          Client Browser                              │
└───────────────────────────┬─────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────────────┐
│                        Nginx Reverse Proxy                           │
│                  (Load Balancing, SSL, Compression)                  │
├─────────────────────────────────────────────────────────────────────┤
│  ┌──────────────────────────┐  ┌─────────────────────────────────┐  │
│  │    Static Assets (SPA)   │  │   /api/* → Backend              │  │
│  │    Cache: 1 year         │  │   /actuator/* → Backend         │  │
│  └──────────────────────────┘  └─────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────────────┐
│                      Spring Boot Backend                             │
├─────────────────────────────────────────────────────────────────────┤
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐ │
│  │   Auth   │ │  Energy  │ │  Meter   │ │  Billing │ │Dashboard │ │
│  │ Module   │ │ Module   │ │ Module   │ │ Module   │ │ Module   │ │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘ └──────────┘ │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐              │
│  │  Notif.  │ │   Org    │ │  Users   │ │  Report  │              │
│  │ Module   │ │ Module   │ │ Module   │ │ Module   │              │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘              │
├─────────────────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────────────────────┐│
│  │                Shared Infrastructure Layer                       ││
│  │  (Security, Config, Cache, Async, Auditing, Persistence)       ││
│  └─────────────────────────────────────────────────────────────────┘│
└─────────────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────────────┐
│                          Data Layer                                  │
├────────────────────────────┬────────────────────────────────────────┤
│         MySQL 8.4          │            Redis 7.4                   │
│    (Primary Database)      │        (Cache Layer)                   │
│  - Relational data         │  - Session caching                    │
│  - Transactions            │  - Rate limiting                      │
│  - JPA entities            │  - Temporary data                     │
└────────────────────────────┴────────────────────────────────────────┘
```

---

## Deployment Architecture

```
┌────────────────────────────────────────────────────────────────────┐
│                        Docker Host                                  │
│                                                                     │
│  ┌──────────────────────────────┐                                   │
│  │   powersphere-network        │  Bridge Network (172.20.0.0/16)  │
│  │                              │                                   │
│  │  ┌──────────┐  ┌──────────┐  │  ┌──────────┐                    │
│  │  │ MySQL    │  │ Redis    │  │  │ MailHog  │                    │
│  │  │ :3306    │  │ :6379    │  │  │ :1025    │                    │
│  │  │ Volumes: │  │ Volumes: │  │  │ :8025    │                    │
│  │  │mysql-data│  │redis-data│  │  │mailhog-d │                    │
│  │  └──────┬───┘  └────┬─────┘  │  └────┬─────┘                    │
│  │         │            │        │       │                          │
│  │  ┌──────▼────────────▼──────┐ │  ┌────▼─────┐                   │
│  │  │     Backend (Spring)     │ │  │Frontend  │                   │
│  │  │     :8080                │ │  │Nginx :80 │                   │
│  │  │     Health: /actuator/*  │ │  │Health:/hz│                   │
│  │  └──────────┬───────────────┘ │  └──────────┘                   │
│  │             │                 │                                  │
│  │  ┌──────────▼───────────────┐ │  ┌──────────┐                   │
│  │  │     Prometheus :9090     │ │  │ Grafana  │                   │
│  │  │     Scrape: /actuator/p  │ │  │ :3000    │                   │
│  │  │     Retention: 30d       │ │  │ Dashbrds │                   │
│  │  └──────────────────────────┘ │  └──────────┘                   │
│  └──────────────────────────────┘                                   │
└────────────────────────────────────────────────────────────────────┘
```

---

## Component Architecture

### Backend Modules

```
com.powersphere/
├── authentication/        # Auth, JWT, RBAC
│   ├── controller/       # REST endpoints
│   ├── service/          # Business logic
│   ├── repository/       # Data access
│   ├── entity/           # JPA entities
│   ├── dto/              # Request/Response DTOs
│   ├── mapper/           # MapStruct mappers
│   ├── jwt/              # JWT token handling
│   ├── validation/       # Custom validators
│   └── event/            # Domain events
├── energy/               # Energy readings & consumption
├── meter/                # Smart meter management
├── billing/              # Billing & tariff plans
├── dashboard/            # Analytics & KPIs
├── notification/         # Alerts & notifications
├── organization/         # Org hierarchy management
├── users/                # User profile management
├── report/               # Reporting engine
└── common/               # Shared utilities
    └── config/           # Cross-cutting configs
```

### Frontend Layers

```
src/
├── api/                  # Axios HTTP client & API calls
├── components/           # Reusable UI components
│   ├── common/          # Shared components
│   ├── charts/          # Chart components
│   ├── dashboard/       # Dashboard widgets
│   └── ...              # Feature-specific components
├── hooks/               # Custom React hooks
├── layouts/             # App layout components
├── pages/               # Route-level page components
│   ├── billing/        # Billing pages
│   ├── energy/         # Energy pages
│   ├── meters/         # Meter pages
│   ├── ...             # Other feature pages
├── routes/              # React Router configuration
├── services/            # API service abstractions
├── store/               # Redux state management
├── styles/              # Global styles & MUI theme
├── types/               # TypeScript type definitions
└── utils/               # Utility functions
```

---

## Data Flow

### Request Flow (API Call)

```
Client → Nginx → Spring Security Filter Chain
                    ↓
              JWT Authentication Filter
                    ↓
              Controller (REST)
                    ↓
              Service (Business Logic)
                    ↓
              Repository (Data Access)
                    ↓
              MySQL / Redis
```

### Event Flow (Async)

```
Service → ApplicationEventPublisher
                    ↓
              @EventListener (Async)
                    ↓
         ┌───────────┼───────────┐
         ▼           ▼           ▼
    Email SVC   SMS SVC     In-App Notif
```

---

## Technology Decisions

| Decision | Choice | Rationale |
|----------|--------|-----------|
| **Monolith** | Modular monolith | Simpler deployment, eventual microservices migration |
| **Database** | MySQL 8.4 | Mature, ACID-compliant, widely supported |
| **Cache** | Redis 7.4 | High performance, pub/sub for notifications |
| **Auth** | JWT with refresh tokens | Stateless, scalable, token rotation |
| **ORM** | Spring Data JPA | Productivity, automatic query generation |
| **UI** | MUI (Material) | Consistent design, accessibility |
| **State** | Redux Toolkit | Predictable state, middleware support |
| **API** | TanStack React Query | Caching, background refetch, optimistic updates |
| **Container** | Docker Compose | Local dev/prod parity, simple orchestration |
| **Monitoring** | Prometheus + Grafana | Industry standard, powerful dashboards |

---

## Scalability Considerations

- **Horizontal scaling**: Backend services are stateless and can be scaled with `--scale backend=N`
- **Database connection pooling**: HikariCP with configurable pool sizes
- **Redis caching**: Reduces database load for frequently accessed data
- **Nginx load balancing**: Configured for round-robin across backend instances
- **Async processing**: Spring `@Async` for non-blocking operations
- **Connection pooling**: Optimized for concurrent access patterns

---

## Security Architecture

- **Authentication**: JWT-based with access/refresh token rotation
- **Authorization**: Role-based (RBAC) with method-level security (`@PreAuthorize`)
- **Password storage**: BCrypt hashing
- **API security**: CORS, CSRF protection, rate limiting (via Nginx)
- **Transport security**: HTTPS (via reverse proxy/load balancer)
- **Input validation**: Bean Validation (JSR-380) at controller level
- **Audit logging**: Spring Data Envers for entity change tracking

---

## Deployment Strategy

### Blue-Green Deployment (Recommended)

1. Build new Docker images with version tags
2. Deploy new stack alongside current stack
3. Run health checks against new stack
4. Switch Nginx upstream to new stack
5. Keep old stack for rollback if needed

### Rolling Update (Docker Compose)

```
docker compose up -d --no-deps --scale backend=3 backend
docker compose up -d --no-deps --scale backend=2 backend
```

---

## Monitoring & Observability

### Metrics (Prometheus)
- JVM: memory, threads, GC, classes
- HTTP: request rates, latencies, error rates
- DB: connection pool, active/idle connections
- Custom: business metrics

### Logging (ELK Stack compatible)
- Structured JSON logging in production
- Rolling file appenders with size/time policies
- Trace IDs for request correlation
- Profile-specific log levels

### Health Checks
- Liveness: Is the service running?
- Readiness: Can the service handle requests?
- Startup: Has the service finished initializing?

---

## Future Improvements

1. **Microservices Migration**: Split monolith into domain services
2. **Kubernetes**: Replace Docker Compose with K8s for production
3. **Message Queue**: Add Kafka/RabbitMQ for event-driven architecture
4. **API Gateway**: Spring Cloud Gateway for routing, rate limiting
5. **Service Mesh**: Istio/Linkerd for observability and security
6. **Database Sharding**: Horizontal sharding for large-scale deployments
7. **CDN**: CloudFront/Cloudflare for global content delivery
8. **Chaos Engineering**: Litmus/Gremlin for resilience testing
