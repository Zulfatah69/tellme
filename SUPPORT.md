# Support

Thank you for using TellMe! This page outlines how to get help, report problems, and find answers to common questions.

---

## How to Get Help

| Channel | When to use |
|---|---|
| 💬 [GitHub Discussions](https://github.com/Zulfatah69/tellme/discussions) | General questions, ideas, deployment advice |
| 🐛 [GitHub Issues](https://github.com/Zulfatah69/tellme/issues) | Bug reports and feature requests (use the provided templates) |
| 🔒 [Security Policy](SECURITY.md) | Responsible disclosure of security vulnerabilities |

---

## Frequently Asked Questions

### 1. How do I set up the database?

Install MySQL 8.0+ and create a database:

```sql
CREATE DATABASE tellme_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Then configure the connection using environment variables or `application.properties`:

```properties
DB_URL=jdbc:mysql://localhost:3306/tellme_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
DB_USERNAME=root
DB_PASSWORD=your-password
```

By default, Hibernate's `ddl-auto=update` creates all tables on first startup.
For production, enable Flyway migrations — see [`docs/configuration.md`](docs/configuration.md).

---

### 2. My email notifications aren't working. What's wrong?

Check the following:

- **`MAIL_USERNAME`** and **`MAIL_PASSWORD`** are set correctly.
- For Gmail, you must use an **App Password** (not your regular password). Generate one at [myaccount.google.com/apppasswords](https://myaccount.google.com/apppasswords).
- The SMTP port is `587` (TLS) by default — ensure your network allows outbound connections on this port.
- Check the application logs; email failures are logged as `ERROR` with the full reason.

---

### 3. I forgot the admin password — how do I reset it?

TellMe does not yet have a built-in password-reset flow for administrators.
You can reset it manually using the MySQL CLI:

```sql
-- First, get a new BCrypt hash by running the app and registering a temporary account,
-- then copy that password hash. Alternatively, use an online BCrypt tool.
-- Example (replace the hash with a valid BCrypt hash):
UPDATE users
SET password = '$2a$12$<your-bcrypt-hash-here>'
WHERE email = 'admin@yourdomain.com';
```

> **Caution:** The hash in the database must be a valid BCrypt string starting with `$2a$` or `$2b$`.

---

### 4. Where are uploaded files stored?

By default, files are stored in the `uploads/` directory relative to the working directory where TellMe is launched.

Change this with the `UPLOAD_DIR` environment variable:

```properties
UPLOAD_DIR=/var/data/tellme/uploads
```

In Docker, the upload directory is mounted as a named volume (`tellme_uploads`) so it persists across container restarts.

---

### 5. Can I use PostgreSQL instead of MySQL?

TellMe is tested and officially supported with **MySQL 8.x**. Since it uses Spring Data JPA and Flyway, you can adapt it to PostgreSQL by:

1. Replacing the MySQL JDBC driver in `pom.xml` with the PostgreSQL driver.
2. Updating the JDBC URL in your configuration.
3. Adapting Flyway migrations (e.g., replacing `AUTO_INCREMENT` with `SERIAL`).

This is not an officially supported configuration but community contributions are welcome.

---

### 6. The app starts but the frontend shows a blank page or errors.

Most commonly this means:

- The backend is running on port `8081` but your browser is pointed at port `8080`. The default port is **`8081`**.
- CORS is blocking requests. If you're serving the frontend from a different origin, set `CORS_ALLOWED_ORIGINS` to match.
- Check the browser's DevTools console for specific JavaScript errors.

---

### 7. How do I enable Swagger UI in production?

Swagger UI is enabled by default. In production you should disable it to avoid exposing your API spec:

```properties
SWAGGER_ENABLED=false
```

Or in `application.properties`:
```properties
springdoc.api-docs.enabled=false
```

---

## Reporting Bugs

1. Search [existing issues](https://github.com/Zulfatah69/tellme/issues) to avoid duplicates.
2. Open a new issue using the **Bug Report** template.
3. Include logs, steps to reproduce, and your environment (Java version, MySQL version, OS).

**Do not include credentials, tokens, or passwords in your bug report.**

---

## Commercial Support

> **This is a community-driven open-source project with no paid or commercial support tier.**
>
> Support is provided by volunteers on a best-effort basis. We encourage everyone to participate in Discussions and help others where possible.

---

## Additional Resources

- [Installation Guide](docs/installation.md)
- [Configuration Guide](docs/configuration.md)
- [Architecture Overview](docs/architecture.md)
- [REST API Reference](docs/api.md)
- [Contributing Guide](CONTRIBUTING.md)
- [Code of Conduct](CODE_OF_CONDUCT.md)
