# Academic Advisor Backend (Academic-Path)

A comprehensive academic support and student discussion forum system tailored for students at the University of Science (HCMUS). This project is built using modern software development standards and is ready for production scaling.

---

## 🏗️ System Architecture

The project follows the standard Spring Boot **Layered Architecture**:

1.  **Controller Layer**: Handles incoming HTTP requests, defines API endpoints, and provides documentation via Swagger (OpenAPI 3).
2.  **Service Layer**: Encapsulates all Business Logic (e.g., Trending algorithms, Recursive Nested Comment deletion, Academic Progress calculations).
3.  **Repository Layer**: Manages database persistence using Spring Data JPA and PostgreSQL.
4.  **DTO (Data Transfer Object)**: Ensures secure and optimized communication between client and server.
5.  **Exception Handling**: Centralized error processing via `GlobalExceptionHandler` with a standardized `ApiResponse` format.

---

## 🛠️ Tech Stack

| Component | Technology |
| :--- | :--- |
| **Framework** | Spring Boot 4.0.1 (Java 21) |
| **Database** | PostgreSQL (Hosted on Supabase / Render) |
| **Migration** | Liquibase / Hibernate DDL Auto |
| **API Documentation** | SpringDoc OpenAPI 3 (Swagger UI) |
| **Build Tool** | Maven |
| **Utility** | Lombok |

---

## 🌐 Production & API Links

### 🔗 Live API Docs (Swagger UI)
Access the interactive documentation to test the APIs directly:
*   **Production:** [https://academic-advisor-backend-j2w8.onrender.com/swagger-ui/index.html](https://academic-advisor-backend-j2w8.onrender.com/swagger-ui/index.html)
*   **Local Host:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### 📂 Major API Groups:

1.  **1. Categories**: Forum category management.
2.  **2. Posts**: CRUD operations, Search, Tagging, and Trending logic.
3.  **3. Likes & Interactions**: Toggle-based Like/Unlike mechanisms.
4.  **4. Comments**: Multi-level Nested Comments support.
5.  **6. Academic**: Credit tracking and semester-wise progress.
6.  **9. Students**: Personal profile and academic information.

---

## 🚀 Setup & Running Locally

### 1. Prerequisites
*   Java JDK 21 or higher.
*   Maven 3.8+.
*   Docker (Optional, for `docker-compose.yml`).

### 2. Configuration
The project is currently connected to a **Supabase (Live)** database. If you wish to use a local instance:
1.  Navigate to `src/main/resources/application.yaml`.
2.  Update the `datasource` properties: `url`, `username`, and `password`.

### 3. Run the App
```bash
./mvnw clean install
./mvnw spring-boot:run
```

---

## 🧪 Interactive UI Tester

An integrated web-based tool for visually testing the Forum business logic:
*   **URL:** [http://localhost:8080/forum-test.html](http://localhost:8080/forum-test.html)
*   **Features:** Create posts, post nested replies, toggle likes, and switch "Mock Student IDs" to simulate multiple user interactions.

---

## 📂 Project Structure

```text
src/main/java/vn/edu/hcmus/fit/learningpath/
├── config/             # Swagger & App Configuration
├── controller/         # API Endpoints mapped to functional groups
├── dto/                # Request & Response data structures
├── entity/             # JPA Entities for DB mapping
├── exception/          # Custom Exceptions & Global Handlers
├── repository/         # Data Access Objects (DAOs)
└── service/            # Core Business Logic implementation
```

---

## 🛡️ Best Practices
*   **Unified Response Wrapper**: Consistent format for all API responses `{ code, message, data }`.
*   **Pagination Support**: Large datasets (Posts, Comments) are served with server-side pagination.
*   **Optimized Counts**: View, Like, and Comment counts are synchronized at the service level for high-performance reading.

---
*Developed by Academic Advisor Team - Academic Advisor Backend*
