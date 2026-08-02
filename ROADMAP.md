# TellMe Roadmap

This document outlines the planned development and feature timeline for the TellMe project. Please note that these plans are subject to change based on community feedback and contributor availability.

## v1.0 — Current Release
*The foundation of the TellMe platform.*
- [x] Authentication & Authorization
- [x] Student Complaint/Submission System
- [x] Suggestions & Feedback
- [x] Discussion Forum with Comments
- [x] Admin Dashboard & Status Tracking
- [x] Image Uploads
- [x] Email Notifications
- [x] BCrypt Password Hashing (Transparent Upgrade)
- [x] OpenAPI / Swagger UI Documentation
- [x] Flyway Database Migrations

## v1.1 — Near Term
*Focusing on performance and robustness.*
- [ ] **Pagination**: Implement server-side pagination for complaints and forum posts to handle large datasets.
- [ ] **Input Validation Improvements**: Stricter backend validation and sanitization.
- [ ] **API Rate Limiting**: Prevent abuse by limiting the frequency of requests.
- [ ] **Cloud File Storage**: Configurable support for AWS S3 or similar cloud storage for image uploads.

## v1.2 — Medium Term
*Expanding usability and configuration.*
- [ ] **Email Templates**: Customizable HTML email templates for notifications.
- [ ] **Role-based Permissions**: Granular permissions allowing specific admins to handle specific complaint categories.
- [ ] **Audit Log**: Keep track of admin actions and status changes.
- [ ] **Internationalization (i18n)**: Multi-language support for the frontend to cater to global institutions.

## v2.0 — Long Term
*Major architectural updates and external integrations.*
- [ ] **REST API Versioning**: Formalizing the API versions (e.g. /api/v1/...) for external consumers.
- [ ] **Spring Security Integration**: Comprehensive security overhaul using Spring Security context.
- [ ] **Multi-tenant Support**: Allow a single instance to serve multiple departments or sub-organizations.
- [ ] **REST Client / Mobile SDK**: Provide official SDKs to ease the development of custom mobile apps.

---

## Community

We rely on the community to guide our roadmap. Have a feature request? 

1. Check our [GitHub Issues](https://github.com/Zulfatah69/tellme/issues) to see if it has already been proposed.
2. If not, open a new **Feature Request** issue and let's discuss it!
3. Feel free to submit a Pull Request if you'd like to implement a feature yourself. See [CONTRIBUTING.md](CONTRIBUTING.md) for details.

