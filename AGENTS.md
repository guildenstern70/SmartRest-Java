# AI Agents Instructions & Guidelines

This document provides architectural guidance, coding standards, and domain conventions for AI agents working in this repository.

---

## 1. Project Overview

**SmartREST (Java Edition)** is a production-grade microservice skeleton designed for building RESTful APIs with modern Java.

- **Language:** Java 26 (with modern language idioms: records, pattern matching, text blocks, etc.)
- **Framework:** Spring Boot 4.1.x
- **Build Tool:** Gradle (Kotlin DSL, `./gradlew`)
- **Persistence:** Spring Data JPA with Hibernate (code-first approach)
- **Database:** In-memory H2 database (`jdbc:h2:mem:smartrestdb`)
- **Documentation:** Springdoc OpenAPI 3 (Swagger UI with custom styling)
- **Templating:** Apache FreeMarker (web dashboard at `/`)
- **Boilerplate Reduction:** Project Lombok
- **Monitoring & Health:** Spring Boot Actuator (liveness and readiness probes)

> **Important:** Do **not** hardcode environment- or machine-specific paths (e.g., local `JAVA_HOME` paths, local user directories). Rely on standard tools, environment variables (`JAVA_HOME`), and the bundled Gradle wrapper (`./gradlew`).

---

## 2. Architecture & Package Structure

The codebase uses a clean, layered architecture under the root package `net.littlelite.smartrest`:

```
net.littlelite.smartrest
├── SmartRest.java             # Spring Boot Application entrypoint
├── config/                    # Spring configuration classes
│   ├── H2ConsoleConfig.java   # H2 console servlet registration
│   ├── OpenApiConfig.java     # OpenAPI 3 specification and Swagger metadata
│   └── SmartRestSwaggerIndexTransformer.java # Swagger UI HTML customization
├── controller/
│   ├── rest/                  # RESTful API controllers
│   │   ├── AliveController.java     # System alive probe (/api/alive)
│   │   ├── ReadinessController.java # Kubernetes readiness probe (/api/ready)
│   │   ├── BrandController.java     # Brand CRUD endpoints (/api/brands)
│   │   ├── DealerController.java    # Dealer CRUD endpoints (/api/dealers)
│   │   ├── CarController.java       # Car querying & creation (/api/cars)
│   │   └── RestExceptionHandler.java# Centralized @RestControllerAdvice error handler
│   └── web/                   # MVC web controllers
│       └── HomeController.java      # Dashboard UI controller (FreeMarker view)
├── service/                   # Business logic and transaction boundaries
│   ├── AliveService.java      # Application metadata and runtime info
│   ├── ReadinessService.java  # Health and DB ping checks
│   ├── BrandService.java      # Brand business logic and validations
│   ├── DealerService.java     # Dealer business logic and validations
│   ├── CarService.java        # Car filtering, search, and creation logic
│   └── InitializeDB.java      # Database seeder (CommandLineRunner)
├── dao/                       # Data access layer (Spring Data JPA Repositories)
│   ├── BrandRepository.java
│   ├── DealerRepository.java
│   └── CarRepository.java
├── model/                     # Domain JPA entities and enums
│   ├── Brand.java             # Brand entity
│   ├── Dealer.java            # Dealer entity
│   ├── Car.java               # Car entity
│   ├── FuelType.java          # Enum (PETROL, DIESEL, ELECTRIC, HYBRID, HYBRID_PLUG_IN)
│   └── TransmissionType.java  # Enum (MANUAL, AUTOMATIC)
└── dto/                       # Data Transfer Objects (Java records with Bean Validation)
    ├── BrandDto.java / CreateBrandDto.java
    ├── DealerDto.java / CreateDealerDto.java
    ├── CarDto.java / CreateCarDto.java
    ├── AliveDto.java / ReadinessDto.java
    └── ApiErrorDto.java
```

---

## 3. Domain Model & Business Invariants

AI agents modifying or extending domain features must adhere to the following business rules:

1. **Entity Relationships:**
   - **`Brand`**: Represents an automotive manufacturer (unique name, country). Has a one-to-many relationship with `Car` and a many-to-many relationship with `Dealer`.
   - **`Dealer`**: Represents a dealership located in a city. Has a many-to-many relationship with `Brand` and a one-to-many relationship with `Car`.
   - **`Car`**: Represents a vehicle model with specifications (power, acceleration, seating, price, fuel type, transmission). Must belong to a `Brand`, and can optionally be assigned to a `Dealer`.

2. **Core Business Rules:**
   - **Dealer Brand Capacity:** A `Dealer` must carry **at least 1 and at most 2 brands** (`1 <= brands.size() <= 2`). Requests attempting to create a dealer with 0 or >2 brands must be rejected with an `IllegalArgumentException`.
   - **Car-Dealer Compatibility:** If a `Car` is assigned to a `Dealer`, that dealer **must carry the brand** of that car. If the dealer does not sell that brand, creation must be rejected with an `IllegalArgumentException`.
   - **Brand Integrity (Deletion):** A `Brand` cannot be deleted if any `Dealer` currently carries it. Attempting to delete an assigned brand must throw an `IllegalStateException` (mapped to HTTP 409 Conflict).
   - **Unique Brand Names:** Brand names must be unique (case-insensitive check prior to saving).
   - **Database Seeding:** `InitializeDB` seeds 5 brands, 5 dealers, and 20 cars on startup if the database is empty (`brandRepository.count() == 0`). Ensure any changes do not break DB seeding or existing tests.

---

## 4. Coding Standards & Recommendations for AI Agents

### 4.1 Java & Framework Conventions
- **Use Java 26 Features:** Leverage records for DTOs, pattern matching for `switch` and `instanceof`, text blocks for multiline strings, and `var` for local variables with obvious types.
- **DTO vs Entity Separation:** **Never** expose JPA entities directly in REST controllers. All REST endpoints must consume `Create*Dto` records and produce `*Dto` records. Entities remain confined to the service and repository layers.
- **DTO Mapping:** Provide static `fromEntity(Entity entity)` methods on DTO records for clean, deterministic conversions.
- **Dependency Injection:** Use constructor injection via Lombok's `@RequiredArgsConstructor` on classes with `private final` dependencies. Never use field injection (`@Autowired` on private fields).
- **Lombok Usage:**
  - Entities: Use `@Getter`, `@Setter`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`. Avoid `@Data` on JPA entities to prevent circular dependencies in `equals`, `hashCode`, and `toString`.
  - Services/Controllers: Use `@RequiredArgsConstructor` and `@Slf4j`.
- **Transactions:** Mark service classes with `@Transactional(readOnly = true)` and annotate mutating methods (`create*`, `update*`, `delete*`) with `@Transactional`.

### 4.2 Validation & Error Handling
- **Input Validation:** Use Jakarta Bean Validation annotations (`@NotBlank`, `@NotNull`, `@Size`, `@Positive`, `@PositiveOrZero`) inside DTO records. Controllers must annotate request bodies with `@Valid`.
- **Exception Handling:**
  - Business validation errors: Throw `IllegalArgumentException` (handled by `RestExceptionHandler` as HTTP 400 Bad Request).
  - Conflict / state errors: Throw `IllegalStateException` (handled by `RestExceptionHandler` as HTTP 409 Conflict).
  - All REST error responses must adhere to the `ApiErrorDto` JSON schema:
    ```json
    {
      "timestamp": "2026-09-16T13:14:15.123456Z",
      "status": 400,
      "error": "Bad Request",
      "message": "Description of the error",
      "validationErrors": { "fieldName": "error message" }
    }
    ```

### 4.3 REST API Design
- **HTTP Methods:** Use standard HTTP verbs (`GET` for retrieval, `POST` for creation, `PUT`/`PATCH` for updates, `DELETE` for removal).
- **Responses:**
  - `POST` endpoints should return HTTP 201 Created with a `Location` header pointing to the created resource (`ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(...)`).
  - `DELETE` endpoints should return HTTP 204 No Content upon success, or HTTP 404 Not Found if the resource does not exist.
  - Query endpoints should return HTTP 200 OK (empty list `[]` for collections, HTTP 404 for individual lookups).
- **OpenAPI Documentation:** Every REST controller must have `@Tag(name = "...", description = "...")` and each endpoint must be documented with `@Operation(summary = "...")`.

---

## 5. Testing Guidelines

- **Frameworks:** JUnit 5 (Jupiter), Spring Boot Test (`@SpringBootTest`), and `MockMvc`.
- **Test Isolation:** Annotate integration test classes modifying data with `@Transactional` so that database operations are rolled back after each test run.
- **Coverage Areas:**
  - Controllers: Validate status codes, JSON payload fields, headers, and validation errors.
  - Services: Validate domain rules (e.g., dealer brand constraints, duplicate brand handling, dealer brand match).
  - Web & Documentation: Ensure Home FreeMarker template, Swagger UI index, and OpenAPI docs endpoint (`/v3/api-docs`) return valid responses.

---

## 6. Build & Execution Cheatsheet

```bash
# Build project and assemble artifacts
./gradlew build

# Run all unit and integration tests
./gradlew test

# Run application locally on port 8080
./gradlew bootRun

# Build executable Spring Boot JAR (build/libs/SmartRest-*.jar)
./gradlew bootJar

# Container builds
docker build -t smartrest:latest .
podman build -t smartrest:latest .
```
