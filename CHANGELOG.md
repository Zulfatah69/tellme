# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.0.0] - 2024-01-01

### Added
- Authentication system for students and administrators.
- Student Complaint/Submission System.
- Categorized Feedback and Suggestions modules.
- Discussion Forum with deeply nested comments.
- Status Tracking (Pending, In Progress, Resolved) for feedback.
- Admin Dashboard for managing user submissions.
- Image Upload capability for complaints and evidence.
- Email Notifications upon status updates or forum replies.

### Changed
- Refactored core services to use constructor injection over field injection for better testability.
- Replaced generic exceptions with domain-specific typed exceptions.
- Updated API responses to use proper HTTP status codes.

### Security
- Removed all hardcoded credentials; moved database and mail configurations to environment variables.
- Implemented file upload validation to prevent malicious file types.
