# Architecture Overview

TellMe is a modern web application built using a 3-tier architecture. It features a rich static frontend communicating via REST API with a robust Spring Boot backend, supported by a MySQL 8 relational database. The application employs standard layer separation for maintainability and scalability.

## High Level Architecture

```mermaid
C4Context
    title TellMe Application Architecture
    Person(user, "User/Admin", "Interacts with the application via browser")
    System(spa, "Static HTML/JS Frontend", "Provides the UI for the TellMe application")
    System(api, "Spring Boot Backend", "Handles business logic, auth, and data access (Port 8081)")
    SystemDb(db, "MySQL Database", "Stores application data (MySQL 8)")
    System_Ext(smtp, "SMTP Server", "Sends email notifications")

    Rel(user, spa, "Uses")
    Rel(spa, api, "Makes REST API calls (JSON/HTTPS)")
    Rel(api, db, "Reads/Writes data (JDBC/JPA)")
    Rel(api, smtp, "Sends emails")
```

## Layer Descriptions

- **Presentation Layer (Frontend)**: Composed of static HTML, CSS, and plain JavaScript. No server-side rendering (no Thymeleaf) is used. It handles user interactions and renders UI asynchronously.
- **API Layer (Controllers)**: Receives HTTP requests, validates input, calls the Service Layer, and formats HTTP responses (JSON).
- **Service Layer**: Contains core business logic, orchestrates interactions between entities, and triggers external services (e.g., Email Service, File Upload).
- **Persistence Layer (Repositories)**: Spring Data JPA interfaces that interact directly with the MySQL database.

## Entity Relationship Diagram

```mermaid
erDiagram
    users {
        int id PK
        string username
        string password
        string role
        string email
    }
    kategori {
        int id PK
        string name
    }
    status {
        int id PK
        string name
    }
    aspirasi {
        int id PK
        int user_id FK
        int kategori_id FK
        int status_id FK
        string judul
        text deskripsi
        datetime created_at
    }
    aspirasi_foto {
        int id PK
        int aspirasi_id FK
        string file_path
    }
    forum_post {
        int id PK
        int user_id FK
        string content
        datetime created_at
    }
    forum_comment {
        int id PK
        int post_id FK
        int user_id FK
        string content
        datetime created_at
    }

    users ||--o{ aspirasi : submits
    users ||--o{ forum_post : creates
    users ||--o{ forum_comment : writes
    kategori ||--o{ aspirasi : categorizes
    status ||--o{ aspirasi : tracks
    aspirasi ||--o{ aspirasi_foto : has
    forum_post ||--o{ forum_comment : contains
```

## Sequence Diagrams

### Submit a Complaint Flow

```mermaid
sequenceDiagram
    actor User
    participant Frontend
    participant AspirasiController
    participant AspirasiService
    participant UploadService
    participant Database

    User->>Frontend: Fills form & attaches photo
    Frontend->>AspirasiController: POST /api/aspirasi (FormData)
    AspirasiController->>UploadService: Save file
    UploadService-->>AspirasiController: File path
    AspirasiController->>AspirasiService: createAspirasi(dto)
    AspirasiService->>Database: Save Aspirasi & Photo
    Database-->>AspirasiService: Entities saved
    AspirasiService-->>AspirasiController: Aspirasi response
    AspirasiController-->>Frontend: 201 Created
    Frontend-->>User: Shows success message
```

### Admin Reviews Submission Flow

```mermaid
sequenceDiagram
    actor Admin
    participant Frontend
    participant AspirasiController
    participant AspirasiService
    participant EmailService
    participant Database

    Admin->>Frontend: Changes status to "In Progress"
    Frontend->>AspirasiController: PUT /api/aspirasi/{id}/proses
    AspirasiController->>AspirasiService: processAspirasi(id)
    AspirasiService->>Database: Update status
    Database-->>AspirasiService: Updated
    AspirasiService->>EmailService: sendNotification(user.email)
    EmailService-->>AspirasiService: Email sent
    AspirasiService-->>AspirasiController: Success response
    AspirasiController-->>Frontend: 200 OK
    Frontend-->>Admin: Updates UI
```

## Authentication Mechanism
TellMe uses a token-based authentication mechanism. Upon successful login (`/api/auth/login`), a secure token is generated, stored in the database, and returned to the client. The frontend must include this token in the `Authorization: Bearer <token>` header for all protected API requests. 

## Architectural Limitations & Planned Improvements
- **Limitation**: Token storage in the database can cause performance bottlenecks under high load.
- **Improvement**: Migrate to stateless JWT (JSON Web Tokens) to remove database lookups for authentication.
- **Limitation**: Uploaded files are stored on the local filesystem, hindering horizontal scalability.
- **Improvement**: Integrate with an S3-compatible object storage service.
