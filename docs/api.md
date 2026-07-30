# PowerSphere – API Documentation

## Overview

The PowerSphere API is a RESTful interface for the Enterprise Smart Energy Management Platform. All API endpoints are prefixed with `/api/` and return JSON responses.

**Base URL**: `http://localhost:8080/api` (development) or `https://api.powersphere.com/api` (production)

**Authentication**: JWT Bearer token required for protected endpoints.

---

## Authentication

### Register

```
POST /api/auth/register
```

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "password": "SecurePass123!",
  "phone": "+1234567890"
}
```

**Response (201):**
```json
{
  "status": "success",
  "message": "Registration successful. Please check your email for verification.",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "email": "john@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "roles": ["USER"]
  }
}
```

### Login

```
POST /api/auth/login
```

**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "SecurePass123!"
}
```

**Response (200):**
```json
{
  "status": "success",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "dGhpcyBpcyBhIHJlZnJlc2g...",
    "tokenType": "Bearer",
    "expiresIn": 900000,
    "user": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "email": "john@example.com",
      "firstName": "John",
      "lastName": "Doe",
      "roles": ["USER"]
    }
  }
}
```

### Refresh Token

```
POST /api/auth/refresh-token
```

**Request Body:**
```json
{
  "refreshToken": "dGhpcyBpcyBhIHJlZnJlc2g..."
}
```

---

## Energy Readings

### Create Energy Reading

```
POST /api/energy/readings
```

**Headers:** `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "meterId": "MTR-001",
  "readingValue": 1234.56,
  "readingDate": "2026-07-30T10:30:00Z",
  "source": "MANUAL",
  "quality": "VALIDATED"
}
```

### Get Energy Readings

```
GET /api/energy/readings?meterId=MTR-001&startDate=2026-01-01&endDate=2026-07-30&page=0&size=20
```

---

## Smart Meters

### Register Meter

```
POST /api/meters
```

**Request Body:**
```json
{
  "meterSerialNumber": "MTR-001",
  "meterType": "ELECTRIC",
  "phaseType": "THREE_PHASE",
  "connectionType": "DIRECT",
  "installationDate": "2026-01-15",
  "location": "Building A, Floor 3",
  "latitude": 40.7128,
  "longitude": -74.0060
}
```

### Get All Meters

```
GET /api/meters?status=ACTIVE&type=ELECTRIC&page=0&size=20&sort=installationDate,desc
```

---

## Billing

### Generate Bill

```
POST /api/bills/generate
```

**Request Body:**
```json
{
  "meterId": "MTR-001",
  "billingPeriodStart": "2026-06-01",
  "billingPeriodEnd": "2026-06-30",
  "tariffPlanId": "TPL-001",
  "dueDate": "2026-07-15"
}
```

### Get Bills

```
GET /api/bills?status=PENDING&organizationId=ORG-001&page=0&size=20
```

---

## Dashboard

### Get Summary

```
GET /api/dashboard/summary?dashboardType=EXECUTIVE&period=MONTHLY
```

### Get Consumption Trends

```
GET /api/dashboard/consumption?period=DAILY&startDate=2026-01-01&endDate=2026-07-30
```

### Get Revenue Trends

```
GET /api/dashboard/revenue?period=MONTHLY&year=2026
```

---

## Organizations

### Create Organization

```
POST /api/organizations
```

**Request Body:**
```json
{
  "name": "Acme Corporation",
  "code": "ACME",
  "address": "123 Main St, New York, NY",
  "contactEmail": "admin@acme.com",
  "contactPhone": "+1234567890"
}
```

---

## Notifications

### Get Notifications

```
GET /api/notifications?status=UNREAD&type=ALERT&page=0&size=20
```

### Mark Notification as Read

```
PATCH /api/notifications/{id}/read
```

### Create Alert Rule

```
POST /api/notifications/alert-rules
```

---

## Users

### Get Users

```
GET /api/users?role=ADMIN&organizationId=ORG-001&page=0&size=20
```

### Update User Profile

```
PUT /api/users/profile
```

---

## Common API Patterns

### Pagination

All list endpoints support pagination via query parameters:
- `page` - Page number (0-indexed, default: 0)
- `size` - Page size (default: 20, max: 100)
- `sort` - Sort field(s), e.g., `createdAt,desc`

### Error Response

```json
{
  "status": "error",
  "message": "Error description",
  "errors": [
    {
      "field": "email",
      "message": "Email is required"
    }
  ],
  "timestamp": "2026-07-30T10:30:00Z"
}
```

### HTTP Status Codes

| Code | Description |
|------|-------------|
| 200 | Success |
| 201 | Created |
| 204 | No Content |
| 400 | Bad Request |
| 401 | Unauthorized |
| 403 | Forbidden |
| 404 | Not Found |
| 409 | Conflict |
| 422 | Unprocessable Entity |
| 500 | Internal Server Error |
