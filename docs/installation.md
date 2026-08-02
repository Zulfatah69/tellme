# Installation Guide

This guide covers setting up the TellMe application either from source for development, or using Docker for production-like deployments.

## Prerequisites
- Java 17+
- Maven 3.6+
- MySQL 8+ (if not using Docker)
- Git
- Docker and Docker Compose (Optional, for Option B)

## Configuration Setup
1. Copy the example configuration file:
   ```bash
   cp .env.example .env
   ```
2. Open `.env` and fill in the required values (Database credentials, Email SMTP settings). Refer to `configuration.md` for details.

## Database Setup (For Option A)
If you are running MySQL natively without Docker, create the database:
```sql
CREATE DATABASE tellme;
CREATE USER 'tellme_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON tellme.* TO 'tellme_user'@'localhost';
FLUSH PRIVILEGES;
```

## Option A: Running from Source (Development)
1. Start the local MySQL database. Alternatively, use the provided Docker Compose file just for the DB:
   ```bash
   cd docker
   docker-compose -f docker-compose.dev.yml up -d
   cd ..
   ```
2. Build the application:
   ```bash
   ./mvnw clean package -DskipTests
   ```
3. Run the Spring Boot app:
   ```bash
   ./mvnw spring-boot:run
   ```
The API and frontend will be available at `http://localhost:8081` (or your configured port).

## Option B: Running with Docker (Production)
1. Ensure your `.env` file is present in the `docker/` directory.
2. Navigate to the docker directory:
   ```bash
   cd docker
   ```
3. Build and start the services:
   ```bash
   docker-compose up -d --build
   ```
4. Check logs to ensure everything started correctly:
   ```bash
   docker-compose logs -f
   ```

## Verifying the Installation
Once running, visit `http://localhost:8081` in your browser. You should see the static frontend.
You can test the API health by sending a GET request to `http://localhost:8081/api/kategori`.

## Common Issues & Solutions
- **Port Conflicts**: If port 8081 is in use, modify `SERVER_PORT` in your `.env`.
- **Database Connection Refused**: Ensure MySQL is running, and the credentials in `.env` match your database setup. When using Docker, ensure the app container is waiting for the db container (`depends_on: db`).
- **File Upload Errors**: Ensure the `uploads/` directory exists and has correct write permissions. Docker handles this automatically via volume mounting.
- **Email Sending Fails**: Ensure `MAIL_PASSWORD` is an App Password if using Gmail, and that 2FA is enabled on the account.
