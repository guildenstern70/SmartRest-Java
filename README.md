# Smart REST - Java Edition

[![License: ISC](https://img.shields.io/badge/License-ISC-blue.svg)](https://opensource.org/licenses/ISC)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/a3472ccda37c4170adaff25373b7c81f)](https://app.codacy.com/gh/guildenstern70/SmartRest-Java/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)

<img src="./src/main/resources/screenshot.png" alt="Screenshot" width="700" />

A production-ready microservice template and starter skeleton for building RESTful APIs with **Spring Boot** and **Java 26**.

Looking for the Kotlin edition? Check out [SmartREST Kotlin Edition](https://github.com/guildenstern70/SmartREST).

---

## Table of Contents

- [Features & Tech Stack](#features--tech-stack)
- [Architecture & Project Structure](#architecture--project-structure)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Domain Model & Seed Data](#domain-model--seed-data)
- [REST API Reference](#rest-api-reference)
- [Web Dashboard & Developer Tools](#web-dashboard--developer-tools)
- [Container Instructions (Docker & Podman)](#container-instructions)
- [Configuration](#configuration)
- [AI Agents & Pair Programming](#ai-agents--pair-programming)
- [License](#license)

---

## Features & Tech Stack

- **Java 26**: Built using modern Java features including records for DTOs, pattern matching, switch expressions, and text blocks.
- **Spring Boot 4.1.x**: Leveraging Spring MVC, Spring Data JPA, and Spring Boot Actuator.
- **Code-First JPA / Hibernate**: Clean entity definitions mapped to an in-memory H2 database with automatic schema generation.
- **In-Memory H2 Database**: Pre-configured for fast development and testing (`jdbc:h2:mem:smartrestdb`), with the web console enabled.
- **Springdoc OpenAPI 3 / Swagger UI**: Interactive API documentation available out of the box with custom theme injection.
- **Apache FreeMarker**: Responsive server-rendered HTML dashboard at `/` displaying real-time application and environment metadata.
- **Jakarta Bean Validation**: Robust constraint validation on incoming request payloads with descriptive error responses.
- **Centralized Error Handling**: Standardized RFC-compliant error envelopes (`ApiErrorDto`) via `@RestControllerAdvice`.
- **Project Lombok**: Minimal boilerplate code for JPA entities and services.
- **Multi-Stage Containerization**: Optimized Dockerfile compatible with both Docker and Podman, running with an unprivileged non-root user (`spring`).

---

## Architecture & Project Structure

The project follows a clean layered architecture under `net.littlelite.smartrest`:

```
net.littlelite.smartrest
├── SmartRest.java             # Main Application Bootstrap
├── config/                    # Spring & OpenAPI configuration
├── controller/
│   ├── rest/                  # RESTful API controllers & RestExceptionHandler
│   └── web/                   # Web MVC controllers (FreeMarker view)
├── service/                   # Business logic and DB initialization (InitializeDB)
├── dao/                       # Spring Data JPA repositories (Brand, Car, Dealer)
├── model/                     # JPA domain entities and enums (FuelType, TransmissionType)
└── dto/                       # Immutable Java records for requests/responses with validation
```

---

## Prerequisites

- **Java Development Kit (JDK) 26** (e.g. [Eclipse Temurin 26](https://adoptium.net/))
- **Git**
- *(Optional)* **Docker** or **Podman** for containerized deployments

---

## Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/guildenstern70/SmartRest-Java.git
cd SmartRest-Java
```

### 2. Build the Application
```bash
./gradlew build
```

### 3. Run Automated Tests
```bash
./gradlew test
```

### 4. Run the Application Locally
```bash
./gradlew bootRun
```
The application will start on `http://localhost:8080`.

### 5. Package as Executable JAR
```bash
./gradlew bootJar
```
The fat JAR will be generated in `build/libs/SmartRest-<version>.jar`.

---

## Domain Model & Seed Data

The sample domain models an **automotive dealership network**:

- **Brands (`Brand`)**: Manufacturers such as BMW, Audi, Tesla, Toyota, Porsche.
- **Dealers (`Dealer`)**: Dealerships located in different cities. Each dealer represents **1 or 2 brands**.
- **Cars (`Car`)**: Vehicles with technical specs (power in HP, 0-100 km/h acceleration, seats, price, fuel type, transmission). Each car belongs to a brand and can optionally be assigned to an authorized dealer.

### Business Rules
1. **Dealer Capacity**: A dealer cannot represent fewer than 1 or more than 2 brands.
2. **Dealer Consistency**: A car may only be assigned to a dealer that carries that car's brand.
3. **Deletion Protection**: A brand cannot be deleted if any dealer is currently associated with it.

### Automatic Database Seeding
On initial launch, `InitializeDB` seeds the in-memory database with **5 brands**, **5 dealers**, and **20 cars** with diverse engine types (Petrol, Diesel, Electric, Hybrid, Plug-in Hybrid) and transmissions.

---

## REST API Reference

All REST endpoints accept and return JSON.

### System & Health Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/alive` | Application status, version, and sanitized DB URL |
| `GET` | `/api/ready` | Kubernetes readiness probe (checks DB connectivity) |
| `GET` | `/actuator/health` | Spring Boot Actuator health status |
| `GET` | `/actuator/info` | Application build info |

### Brand Management (`/api/brands`)

| Method | Endpoint | Description | Status Codes |
|---|---|---|---|
| `GET` | `/api/brands` | List all brands | `200 OK` |
| `GET` | `/api/brands/{id}` | Get brand details by ID | `200 OK`, `404 Not Found` |
| `POST` | `/api/brands` | Create a new brand | `201 Created`, `400 Bad Request` |
| `DELETE` | `/api/brands/{id}` | Delete a brand | `204 No Content`, `404 Not Found`, `409 Conflict` |

### Dealer Management (`/api/dealers`)

| Method | Endpoint | Description | Status Codes |
|---|---|---|---|
| `GET` | `/api/dealers` | List all dealers with their brands & inventory count | `200 OK` |
| `GET` | `/api/dealers/{id}` | Get dealer by ID | `200 OK`, `404 Not Found` |
| `GET` | `/api/dealers/{id}/cars` | Get all cars available at a specific dealership | `200 OK` |
| `POST` | `/api/dealers` | Create a new dealer (must specify 1 or 2 brand IDs) | `201 Created`, `400 Bad Request` |
| `DELETE` | `/api/dealers/{id}` | Delete a dealer | `204 No Content`, `404 Not Found` |

### Car Management (`/api/cars`)

| Method | Endpoint | Description | Status Codes |
|---|---|---|---|
| `GET` | `/api/cars` | List all cars (supports optional query filters) | `200 OK` |
| `GET` | `/api/cars?brandId={id}` | Filter cars by brand ID | `200 OK` |
| `GET` | `/api/cars?dealerId={id}` | Filter cars by dealer ID | `200 OK` |
| `GET` | `/api/cars?fuelType={type}` | Filter cars by fuel type (`PETROL`, `DIESEL`, `ELECTRIC`, `HYBRID`, `HYBRID_PLUG_IN`) | `200 OK` |
| `GET` | `/api/cars/{id}` | Get car details by ID | `200 OK`, `404 Not Found` |
| `POST` | `/api/cars` | Create a new car model | `201 Created`, `400 Bad Request` |
| `DELETE` | `/api/cars/{id}` | Delete a car by ID | `204 No Content`, `404 Not Found` |

---

## Web Dashboard & Developer Tools

When running locally on port `8080`:

* **Web Dashboard**: [http://localhost:8080/](http://localhost:8080/)
* **Swagger UI Documentation**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
* **OpenAPI 3 JSON Spec**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
* **H2 Database Console**: [http://localhost:8080/h2](http://localhost:8080/h2)
  - **JDBC URL:** `jdbc:h2:mem:smartrestdb`
  - **User Name:** `sa`
  - **Password:** `password`

---

## Container Instructions

You can build and run the application container using either **Docker** or **Podman**.

### 1. Using Docker

#### Build the image
```bash
docker build -t smartrest:latest .
```

#### Run the container
```bash
docker run -d --name smartrest-app -p 8080:8080 smartrest:latest
```

#### Stop and remove container
```bash
docker stop smartrest-app && docker rm smartrest-app
```

---

### 2. Using Podman

> **Note for macOS users**: Ensure your Podman VM is running (`podman machine start`) before executing commands.

#### Build the image
```bash
podman build -t smartrest:latest .
```

#### Run the container
```bash
podman run -d --name smartrest-app -p 8080:8080 smartrest:latest
```

#### Stop and remove container
```bash
podman stop smartrest-app && podman rm smartrest-app
```

---

## Configuration

Application configuration is located in `src/main/resources/application.yaml`. Key settings include:

```yaml
spring:
  application:
    name: SmartRest
    version: 0.7.0
  datasource:
    url: jdbc:h2:mem:smartrestdb
    driverClassName: org.h2.Driver
    username: sa
    password: password
  h2:
    console:
      enabled: true
      path: /h2
  jpa:
    hibernate:
      ddl-auto: create

springdoc:
  swagger-ui:
    document-title: "SmartREST API"

management:
  endpoints:
    web:
      exposure:
        include: health, info
  endpoint:
    health:
      probes:
        enabled: true
```

---

## AI Agents & Pair Programming

Guidelines, domain conventions, and instructions for autonomous AI agents and pair-programming assistants are documented in [AGENTS.md](AGENTS.md).

---

## License

This software is licensed under the [ISC License](LICENSE).

Copyright (c) 2026, Alessio Saltarin.
