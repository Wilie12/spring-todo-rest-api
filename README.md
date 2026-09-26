# Spring To-Do REST API

## Overview

Spring To-Do REST API is a secure, domain-structured backend service built with **Java 17** and **Spring Boot 3.5** that enables users to manage private task lists.

Beyond standard CRUD operations, the application enforces strict resource ownership (IDOR protection) through Spring Security method-level authorization, hashes credentials using BCrypt, and validates persistence and web contracts through an automated test suite powered by **JUnit 5**, **Mockito**, and **Testcontainers**.

## Architectural & Security Highlights

* **Feature-Based Modular Packaging:** Code is organized by domain (`task`, `user`, `shared`) rather than technical layers, encapsulating controllers, services, repositories, mappers, and exception advisors within their respective business boundaries.
* **Defense-in-Depth Authorization & IDOR Prevention:** Endpoints are protected via **HTTP Basic Authentication** (`SecurityConfig`) combined with method-level security (`@EnableMethodSecurity`). `TaskService` enforces `@PreAuthorize("hasAuthority('USER') && #username == authentication.name")` expressions alongside explicit resource ownership validation (`AccessDeniedException` mapped to `403 Forbidden`) to guarantee users can never read, modify, or delete tasks belonging to other accounts.
* **Cryptographic Credential Storage:** Raw passwords submitted during registration are never persisted in plain text; `UserMapper` hashes all secrets via Spring Security's `BCryptPasswordEncoder` prior to entity creation.
* **Immutable DTO Contracts:** All API request and response payloads (`TaskRequest`, `TaskUpdateRequest`, `TaskResponse`, `UserRequest`, `ErrorMessageResponse`) are implemented as immutable **Java 17 Records**, preventing entity leakage at the web layer for task operations.
* **Decentralized Exception Handling:** Domain exceptions (`TaskNotFoundException`, `UserAlreadyExistsException`) are supplied functionally via `Supplier<T>` utilities (`TaskExceptionSupplier`, `UserExceptionSupplier`) and intercepted by domain-specific `@ControllerAdvice` components (`TaskExceptionAdvisor`, `UserExceptionAdvisor`) returning standardized `ErrorMessageResponse` JSON payloads.
* **Multi-Layered Testing with Testcontainers:**
    * **Web Slice Tests (`@WebMvcTest`):** Isolated controller and security filter chain verification (`TaskControllerTest`, `UserControllerTest`).
    * **Method Security & Service Unit Tests:** Business logic and SpEL `@PreAuthorize` rule verification using `TestSecurityConfig` and `MockitoExtension` (`TaskServiceTest`, `UserServiceTest`).
    * **Persistence Integration Tests (`@DataJpaTest`):** Real PostgreSQL database query verification orchestrated via **Testcontainers** and Spring Boot's `@ServiceConnection` (`TaskRepositoryTestcontainersTest`, `UserRepositoryTestcontainersTest`).

## Project Structure

```text
src/main/java/com/nn/spring_todo_rest_api
├── SpringTodoRestApiApplication.java       # Application bootstrap
├── shared/
│   └── api/response/
│       └── ErrorMessageResponse.java       # Unified error response Record
├── task/
│   ├── api/
│   │   ├── request/                        # TaskRequest & TaskUpdateRequest Records
│   │   └── response/                       # TaskResponse Record
│   ├── controller/
│   │   └── TaskController.java             # REST endpoints (/api/v1/tasks)
│   ├── domain/
│   │   └── Task.java                       # JPA Entity (tasks table)
│   ├── repository/
│   │   └── TaskRepository.java             # Spring Data JPA repository
│   ├── service/
│   │   └── TaskService.java                # Business logic & @PreAuthorize ownership guards
│   └── support/                            # TaskMapper, TaskExceptionAdvisor, TaskNotFoundException
└── user/
    ├── api/request/
    │   └── UserRequest.java                # Registration payload Record
    ├── config/
    │   └── SecurityConfig.java             # SecurityFilterChain, HTTP Basic & BCrypt setup
    ├── controller/
    │   └── UserController.java             # Registration endpoint (/api/v1/users/register)
    ├── domain/                             # UserAccount Entity & UserAccountDetails adapter
    ├── repository/
    │   └── UserRepository.java             # Spring Data JPA repository
    ├── service/                            # UserService & UserAccountDetailsService
    └── support/                            # UserMapper, UserExceptionAdvisor, UserAlreadyExistsException
```

## Technology Stack

* **Language:** Java 17
* **Framework:** Spring Boot 3.5.6 (Spring Web, Spring Data JPA, Spring Security)
* **Database:** PostgreSQL
* **Testing:** JUnit 5, Mockito, AssertJ, Spring Security Test, Testcontainers (`postgresql`, `junit-jupiter`)
* **Build Tool:** Apache Maven (with Maven Wrapper 3.9.11)

---

## Getting Started

### Prerequisites

* **JDK 17** or higher installed
* **PostgreSQL** instance running locally on port `5432` (for running the application)
* **Docker** running (required to execute `@DataJpaTest` integration tests via Testcontainers)

### 1. Database & Environment Setup

1. Create a local PostgreSQL database named `todo_db`:
   ```sql
   CREATE DATABASE todo_db;
   ```

2. Export the required database credentials in your terminal session (referenced in `src/main/resources/application.properties`):
   ```bash
   export DB_USERNAME=postgres
   export DB_PASSWORD=your_password
   ```

### 2. Running the Application

Clone the repository and launch the service using the repository-bound Maven Wrapper:

```bash
git clone https://github.com/Wilie12/spring-todo-rest-api.git
cd spring-todo-rest-api
./mvnw spring-boot:run
```

The server will start on `http://localhost:8080`. Hibernate (`spring.jpa.hibernate.ddl-auto=update`) will automatically initialize the `users` and `tasks` tables.

### 3. Running Automated Tests

Ensure Docker is running on your machine (Testcontainers will automatically spin up an isolated PostgreSQL container via `@ServiceConnection`), then execute:

```bash
./mvnw clean verify -B -ntp
```

---

## API Reference

### Authentication

All endpoints—except `POST /api/v1/users/register`—require **HTTP Basic Authentication**:
```http
Authorization: Basic <base64(username:password)>
```

### Endpoints Overview

| Method | Endpoint | Auth Required | Success Status | Description |
| :--- | :--- | :---: | :---: | :--- |
| `POST` | `/api/v1/users/register` | No | `201 Created` | Registers a new user account with a BCrypt-hashed password. |
| `GET` | `/api/v1/tasks` | Yes (Basic) | `200 OK` | Retrieves all tasks belonging to the authenticated user. |
| `GET` | `/api/v1/tasks/{taskId}` | Yes (Basic) | `200 OK` | Retrieves a specific task by ID (verifies ownership). |
| `POST` | `/api/v1/tasks` | Yes (Basic) | `201 Created` | Creates a new task assigned to the authenticated user. |
| `PUT` | `/api/v1/tasks/{taskId}` | Yes (Basic) | `200 OK` | Updates a task's title, description, or completion status. |
| `DELETE` | `/api/v1/tasks/{taskId}` | Yes (Basic) | `204 No Content` | Deletes a specific task by ID (verifies ownership). |

---

### Detailed Endpoint Specifications

#### 1. Register a New User
* **Endpoint:** `POST /api/v1/users/register`
* **Request Body:**
  ```json
  {
    "username": "johndoe",
    "password": "mySecurePassword123"
  }
  ```
* **Responses:**
    * `201 Created`: `"User registered successfully"`
    * `409 Conflict`: `{"message": "User with username: johndoe, already exists"}`

#### 2. Get All User Tasks
* **Endpoint:** `GET /api/v1/tasks`
* **Responses:**
    * `200 OK`: `List<TaskResponse>`
    * `401 Unauthorized`: Missing or invalid Basic Auth credentials.

#### 3. Get a Single Task
* **Endpoint:** `GET /api/v1/tasks/{taskId}`
* **Responses:**
    * `200 OK`: `TaskResponse`
    * `403 Forbidden`: Authenticated user is not the owner of the task.
    * `404 Not Found`: `{"message": "Task with id 1 not found"}`

#### 4. Create a New Task
* **Endpoint:** `POST /api/v1/tasks`
* **Request Body:**
  ```json
  {
    "title": "Buy groceries",
    "description": "Milk, Eggs, Bread, and Coffee"
  }
  ```
* **Responses:**
    * `201 Created`: `TaskResponse`

#### 5. Update a Task
* **Endpoint:** `PUT /api/v1/tasks/{taskId}`
* **Request Body:**
  ```json
  {
    "title": "Buy groceries",
    "description": "Milk, Eggs, Bread, and Decaf Coffee",
    "isCompleted": true
  }
  ```
* **Responses:**
    * `200 OK`: `TaskResponse`
    * `403 Forbidden`: Authenticated user is not the owner of the task.
    * `404 Not Found`: `{"message": "Task with id 1 not found"}`

#### 6. Delete a Task
* **Endpoint:** `DELETE /api/v1/tasks/{taskId}`
* **Responses:**
    * `204 No Content`
    * `403 Forbidden`: Authenticated user is not the owner of the task.
    * `404 Not Found`: `{"message": "Task with id 1 not found"}`

---

## Data Models

### `UserRequest`
| Field | Type | Description |
| :--- | :--- | :--- |
| `username` | `String` | Unique username for the account. |
| `password` | `String` | Raw password (hashed via BCrypt before persistence). |

### `TaskRequest`
| Field | Type | Description |
| :--- | :--- | :--- |
| `title` | `String` | Title of the task. |
| `description` | `String` | Detailed description of the task. |

### `TaskUpdateRequest`
| Field | Type | Description |
| :--- | :--- | :--- |
| `title` | `String` | Updated title of the task. |
| `description` | `String` | Updated description of the task. |
| `isCompleted` | `boolean` | Task completion status (`true` = completed, `false` = pending). |

### `TaskResponse`
| Field | Type | Description |
| :--- | :--- | :--- |
| `id` | `long` | Unique database identifier for the task. |
| `title` | `String` | Title of the task. |
| `description` | `String` | Detailed description of the task. |
| `isCompleted` | `boolean` | Current completion state of the task. |

### `ErrorMessageResponse`
| Field | Type | Description |
| :--- | :--- | :--- |
| `message` | `String` | Human-readable error message returned on `404` and `409` status codes. |