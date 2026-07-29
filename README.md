# PowerSphere – Enterprise Smart Energy Management Platform

[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.9+-orange.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)

## Project Overview

PowerSphere is an **Enterprise Smart Energy Management Platform** designed to monitor, analyze, and optimize energy consumption across organizational infrastructures. Built on a **modular monolith** architecture, PowerSphere follows clean architecture and SOLID principles, with the flexibility to evolve into a microservices ecosystem as the platform grows.

### Key Capabilities (Future Modules)

| Module | Purpose |
|---|---|
| **Authentication** | Identity management and access control |
| **Users** | User administration and profiles |
| **Organization** | Tenant and organizational hierarchy |
| **Energy** | Energy consumption tracking and analytics |
| **Meter** | Smart meter data ingestion and management |
| **Billing** | Usage-based billing and invoicing |
| **Notification** | Multi-channel alerting (email, SMS, push) |
| **Reports** | Configurable reporting engine |
| **Dashboard** | Real-time visualization and monitoring |

---

## Architecture

### Modular Monolith Design

The platform is structured as a **modular monolith** — a single deployable unit with clear module boundaries. This approach provides:

- **Domain-Driven Design**: Each module represents a bounded context
- **Clean Separation**: Strict package-level isolation between modules
- **Future-Proof**: Modules can be extracted into microservices with minimal refactoring
- **Shared Foundation**: Common utilities, base entities, and cross-cutting concerns in shared modules

### Package Structure

```
com.powersphere
├── PowerSphereApplication.java          # Application entry point
├── authentication/                       # Authentication & authorization
├── users/                                # User management
├── organization/                         # Organization & tenant management
├── energy/                               # Energy data & analytics
├── meter/                                # Smart meter management
├── billing/                              # Billing & invoicing
├── notification/                         # Notification services
├── reports/                              # Report generation
├── dashboard/                            # Dashboard & visualization
├── common/                               # Shared base classes & utilities
│   ├── config/                           # Shared configuration
│   ├── constant/                         # Application constants
│   ├── dto/                              # Generic DTOs
│   ├── entity/                           # Base JPA entity
│   └── exception/                        # Global exception handling
├── shared/                               # Cross-module utilities
│   ├── annotation/                       # Custom annotations
│   ├── enums/                            # Shared enumerations
│   └── util/                             # Common utilities
└── config/                               # Infrastructure configuration
    ├── AsyncConfig.java                  # Async task execution
    ├── CacheConfig.java                  # Caching configuration
    ├── CorsConfig.java                   # CORS settings
    ├── JacksonConfig.java                # JSON serialization
    ├── JpaConfig.java                    # JPA configuration
    ├── OpenApiConfig.java                # API documentation
    ├── RedisConfig.java                  # Redis configuration
    └── SwaggerConfig.java                # Swagger annotations
```

---

## Technology Stack

### Core Framework
| Technology | Version | Purpose |
|---|---|---|
| Java | 21 LTS | Runtime platform |
| Spring Boot | 3.5.0 | Application framework |
| Maven | 3.9+ | Build & dependency management |

### Data Layer
| Technology | Version | Purpose |
|---|---|---|
| MySQL | 8.x | Primary database |
| Redis | 7.x | Caching & session management |
| Spring Data JPA | 3.5.x | ORM & data access |
| Hibernate | 6.x | JPA implementation |

### API & Documentation
| Technology | Version | Purpose |
|---|---|---|
| SpringDoc OpenAPI | 2.8.x | API documentation (Swagger) |
| Jackson | 2.18.x | JSON serialization |

### Development Tools
| Technology | Version | Purpose |
|---|---|---|
| Lombok | 1.18.36 | Boilerplate code reduction |
| MapStruct | 1.6.3 | Object mapping |
| Spring DevTools | 3.5.x | Development productivity |

### Testing
| Technology | Version | Purpose |
|---|---|---|
| JUnit | 5.x | Unit testing |
| Mockito | 5.x | Mocking framework |
| H2 Database | 2.x | In-memory test database |

---

## Folder Structure

```
power-sphere/
├── src/
│   ├── main/
│   │   ├── java/com/powersphere/        # Application source code
│   │   └── resources/                    # Configuration files
│   │       ├── application.yml           # Main configuration
│   │       ├── application-dev.yml       # Development profile
│   │       ├── application-test.yml      # Test profile
│   │       ├── application-prod.yml      # Production profile
│   │       ├── bootstrap.yml             # Bootstrap config
│   │       └── logback-spring.xml        # Logging configuration
│   └── test/
│       ├── java/                         # Test source code
│       └── resources/                    # Test resources
├── database/
│   ├── schema/                           # Database migration scripts
│   └── data/                             # Seed data scripts
├── docker/                               # Docker & Compose files
├── docs/                                 # Documentation
├── logs/                                 # Application logs
├── postman/                              # Postman collections
├── scripts/                              # Utility scripts
├── pom.xml                               # Maven build file
└── README.md                             # This file
```

---

## Getting Started

### Prerequisites

- **Java Development Kit (JDK)** 21 or later
- **Apache Maven** 3.9 or later
- **MySQL** 8.x (for local development with MySQL profile)
- **Redis** 7.x (optional, for caching)

### Environment Variables

The application uses environment variables for all sensitive credentials:

| Variable | Description | Default |
|---|---|---|
| `SERVER_PORT` | Application server port | `8080` |
| `SPRING_PROFILES_ACTIVE` | Active profile | `dev` |
| `DB_URL` | Database JDBC URL | `jdbc:mysql://localhost:3306/powersphere` |
| `DB_USERNAME` | Database username | `root` |
| `DB_PASSWORD` | Database password | `root` |
| `DB_MAX_POOL_SIZE` | HikariCP max pool size | `10` |
| `DB_MIN_IDLE` | HikariCP min idle connections | `5` |
| `JPA_DDL_AUTO` | Hibernate DDL mode | `validate` |
| `REDIS_HOST` | Redis host | `localhost` |
| `REDIS_PORT` | Redis port | `6379` |
| `REDIS_PASSWORD` | Redis password | (empty) |
| `LOG_LEVEL_ROOT` | Root log level | `INFO` |
| `LOG_LEVEL_APP` | Application log level | `DEBUG` |

### Quick Start (Development)

```bash
# Clone the repository
git clone https://github.com/powersphere/power-sphere.git
cd power-sphere

# Build the project
./mvnw clean install

# Run with development profile (H2 in-memory database)
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Access Swagger UI
# http://localhost:8080/swagger-ui.html

# Access H2 Console (dev mode only)
# http://localhost:8080/h2-console
```

---

## Development Workflow

### Maven Commands

```bash
# Build the project (skip tests)
./mvnw clean install -DskipTests

# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=PowerSphereApplicationTests

# Run application
./mvnw spring-boot:run

# Package as JAR
./mvnw clean package
```

### Profiles

| Profile | Database | Caching | Logging | Purpose |
|---|---|---|---|---|
| `dev` | H2 (in-memory) | In-memory | Verbose | Local development |
| `test` | H2 (in-memory) | In-memory | Minimal | Automated testing |
| `prod` | MySQL | Redis | WARN | Production deployment |

### Code Quality

- **Lombok**: Reduces boilerplate (getters, setters, builders, etc.)
- **MapStruct**: Type-safe object mapping between entities and DTOs
- **Spring Validation**: Bean validation with `jakarta.validation` annotations
- **Checkstyle**: (Planned) Code style enforcement

---

## API Documentation

Once the application is running, API documentation is available at:

- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI Spec**: [http://localhost:8080/api-docs](http://localhost:8080/api-docs)
- **Actuator Health**: [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)
- **Actuator Info**: [http://localhost:8080/actuator/info](http://localhost:8080/actuator/info)

---

## Monitoring & Operations

Spring Boot Actuator is configured with the following endpoints:

- `/actuator/health` — Health probes (liveness, readiness)
- `/actuator/info` — Application information
- `/actuator/metrics` — Application metrics
- `/actuator/env` — Environment properties
- `/actuator/loggers` — Logger configuration
- `/actuator/threaddump` — Thread dump
- `/actuator/heapdump` — Heap dump (production)

---

## Database

### Schema Management

The platform uses **Hibernate DDL auto** for schema management during development (`ddl-auto: update`) and **Flyway** or **Liquibase** (planned) for production migrations.

### Initial Setup

```sql
-- Create the PowerSphere database
CREATE DATABASE IF NOT EXISTS powersphere
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- Create application user
CREATE USER IF NOT EXISTS 'powersphere'@'%' IDENTIFIED BY 'your-strong-password';
GRANT ALL PRIVILEGES ON powersphere.* TO 'powersphere'@'%';
FLUSH PRIVILEGES;
```

---

## Future Roadmap

### Phase 1 — Foundation (Current)
- [x] Project structure & configuration
- [x] Modular monolith architecture
- [x] Shared utilities & base classes
- [x] API documentation setup
- [x] Monitoring & health checks

### Phase 2 — Core Infrastructure
- [ ] Authentication & authorization (JWT)
- [ ] User management
- [ ] Organization & tenant setup
- [ ] Database migrations (Flyway/Liquibase)
- [ ] CI/CD pipeline

### Phase 3 — Business Modules
- [ ] Smart meter data ingestion
- [ ] Energy consumption analytics
- [ ] Billing engine
- [ ] Notification system
- [ ] Reporting engine
- [ ] Real-time dashboards

### Phase 4 — Enterprise Features
- [ ] Microservices extraction
- [ ] Event-driven architecture
- [ ] API Gateway
- [ ] Service discovery
- [ ] Distributed tracing
- [ ] Multi-region deployment

---

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Branch Naming Convention

- `feature/*` — New features
- `fix/*` — Bug fixes
- `refactor/*` — Code refactoring
- `docs/*` — Documentation updates

---

## License

This project is licensed under the Apache License 2.0 — see the [LICENSE](LICENSE) file for details.

## Contact

- **Project Team**: dev@powersphere.com
- **Project Home**: [https://powersphere.com](https://powersphere.com)
- **Issue Tracker**: [GitHub Issues](https://github.com/powersphere/power-sphere/issues)
