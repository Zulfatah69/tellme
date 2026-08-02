# TellMe v1.0.0 Release Notes

We are thrilled to announce the `v1.0.0` production-ready release of TellMe, an open-source student feedback, complaint, and discussion platform!

This release transitions TellMe from an academic project into a robust, secure, and fully documented platform designed for universities, academic departments, and student organizations.

## 🚀 Key Features

*   **Student Submission System:** Students can easily submit complaints, suggestions, or feedback, with options for anonymity and image attachments.
*   **Status Tracking & Dashboard:** Administrators have a comprehensive dashboard to track submissions across different categories and manage their lifecycle (Pending, In Review, Resolved, Rejected) with feedback.
*   **Automated Email Notifications:** Asynchronous email dispatch notifies the appropriate department (e.g., Academic, Organization) when a new submission is made or when a status changes.
*   **Discussion Forum:** A fully featured threaded discussion forum for students to engage with each other.
*   **RESTful API:** Clean, structured REST API with JSON responses.

## 🛡️ Security & Architecture Enhancements in v1.0.0

*   **BCrypt Password Hashing:** Upgraded from SHA-256 to BCrypt with adaptive work factors. Legacy passwords are transparently upgraded upon the user's next login.
*   **Database Migrations:** Integrated **Flyway** for reliable schema versioning and reference data seeding.
*   **OpenAPI & Swagger UI:** Added interactive API documentation, accessible at `/swagger-ui.html`.
*   **Configuration Externalization:** All sensitive credentials and environment-specific settings have been extracted to environment variables (`.env`).
*   **Robust File Uploads:** Enforced MIME type whitelists (JPEG/PNG/GIF/WebP), size limits, and path traversal prevention.
*   **Error Handling:** Implemented a `GlobalExceptionHandler` to provide consistent, stack-trace-free JSON error responses.

## 📦 Deployment & DevOps

*   **Docker Ready:** Includes a multi-stage `Dockerfile` and `docker-compose.yml` for zero-configuration deployments alongside MySQL 8.
*   **GitHub Actions CI:** Automated Maven builds and test execution on every pull request.
*   **Security Scanning:** Automated weekly CodeQL analysis and Dependabot configuration.

## 📚 Documentation

The repository has been thoroughly documented to meet top-tier open-source standards:
*   [README.md](README.md) – Project overview and quickstart.
*   [CONTRIBUTING.md](CONTRIBUTING.md) – Guidelines for community contributions.
*   [ARCHITECTURE.md](docs/architecture.md) – C4 diagrams, ERDs, and sequence flows.
*   [API Reference](docs/api.md) – Detailed endpoint documentation.

## 🙏 Acknowledgements

Thank you to everyone who contributed to this milestone. We look forward to seeing how TellMe improves student communication at your institution!
