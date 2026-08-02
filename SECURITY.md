# Security Policy

## Supported Versions

| Version | Supported          |
| ------- | ------------------ |
| 1.0.x   | ✅ Active support  |
| < 1.0   | ❌ Not supported   |

## Reporting a Vulnerability

We take the security of TellMe seriously. If you discover a security vulnerability, **please do not create a public GitHub Issue**.

Instead, please report it privately by emailing:

> **security@tellme-project.org**

### What to include in your report

- A clear description of the vulnerability
- Steps to reproduce (proof-of-concept preferred)
- Potential impact assessment
- Your suggested remediation (optional)

### What happens after you report

| Step | Timeframe |
|---|---|
| **Acknowledgment** | Within 48 hours |
| **Initial assessment** | Within 5 business days |
| **Patch development** | Depends on complexity |
| **Public disclosure** | After patch is released |

We will publicly credit you in the release notes if you consent.

---

## Security Architecture

### Authentication
- Token-based session authentication using opaque UUID tokens.
- Tokens are stored in the database and invalidated on logout.
- All API routes (`/api/**`) require a valid `Authorization: Bearer <token>` header, except `POST /api/auth/login` and `POST /api/users` (registration).

### Password Storage
- **Current:** Passwords are hashed with **BCrypt** (work factor 12), which is salted and resistant to rainbow table and brute-force attacks.
- **Migration:** Legacy SHA-256 hashes from earlier versions are transparently upgraded to BCrypt on the user's next successful login. No manual migration is required.

### File Upload Security
- Only `.jpg`, `.jpeg`, `.png`, and `.pdf` extensions are accepted.
- Uploaded filenames are replaced with cryptographically random UUIDs — original filenames are never preserved on disk.
- Path traversal sequences (`..`) in filenames are rejected.
- Maximum file size is configurable via `MAX_FILE_SIZE` (default: 5 MB per file, 25 MB per request).

### Error Handling
- Stack traces and exception class names are never exposed to clients.
- All error responses are normalized to a structured JSON format by the `GlobalExceptionHandler`.

### CORS
- Allowed origins are controlled by the `CORS_ALLOWED_ORIGINS` environment variable.
- Default: `http://localhost:8081` (local development only).
- **For production:** Set `CORS_ALLOWED_ORIGINS` to your specific deployment domain.

---

## Security Best Practices for Deployers

When deploying TellMe to a production environment:

| Practice | Details |
|---|---|
| **Use HTTPS** | Deploy behind a reverse proxy (Nginx, Caddy, etc.) with a valid TLS certificate |
| **Environment Variables** | Never commit credentials to source control — use environment variables or a secrets manager |
| **Database User** | Create a dedicated MySQL user with only the permissions TellMe needs (no `GRANT` or `DROP`) |
| **File Upload Directory** | Ensure the `uploads/` directory is not executable and is served by a web server, not Spring |
| **Swagger UI** | Disable in production by setting `SWAGGER_ENABLED=false` |
| **Flyway** | Use `spring.jpa.hibernate.ddl-auto=validate` and enable Flyway for schema management in production |

---

## Known Limitations

| Area | Status |
|---|---|
| Session token storage in DB | Requires DB lookup per request — fine at moderate scale; JWT migration is in the [ROADMAP](ROADMAP.md) |
| No built-in rate limiting | Planned for v1.1; mitigate with a reverse proxy (e.g., Nginx `limit_req`) |
| No CSRF protection | The app is stateless (token-based, no cookies), so CSRF is not applicable |
| Local file storage | Does not scale horizontally; S3-compatible storage is planned for v1.1 |

---

## Out-of-Scope Items

The following are generally **not** considered security vulnerabilities for this project:

- Denial-of-Service (DoS) attacks at the network layer
- Spam / bulk account creation (rate limiting is planned but not present)
- Theoretical vulnerabilities without a proof of concept
- Bugs in third-party dependencies that have already been disclosed upstream
