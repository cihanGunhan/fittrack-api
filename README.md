# FitTrack API

Personal fitness tracking REST API with JWT authentication.

Built with Java 21, Spring Boot 3, Spring Security, and PostgreSQL.

## Requirements

- Java 21
- Docker Desktop

## Running the Application

Start the database:

```bash
docker run --name fittrack-db \
  -e POSTGRES_PASSWORD=fittrack123 \
  -e POSTGRES_DB=fittrackdb \
  -e POSTGRES_USER=fittrackuser \
  -p 5433:5432 -d postgres:15
```

Run the application:

```bash
./mvnw spring-boot:run
```

API runs on `http://localhost:8081`

## Authentication

Register or login to get a JWT token. Add it to requests as `Authorization: Bearer <token>`.

All endpoints except `/api/auth/register` and `/api/auth/login` require authentication.

## API Documentation

Swagger UI available at `http://localhost:8081/swagger-ui/index.html` when the application is running. Use the Authorize button to test protected endpoints.

## Tech Stack

- Java 21 (Temurin LTS)
- Spring Boot 3.5
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL 15
- Docker
- JUnit 5, Mockito