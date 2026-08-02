# Security Policy

## Supported Versions

Please see the following table to know which versions of TellMe are currently being supported with security updates.

| Version | Supported          |
| ------- | ------------------ |
| 1.0.x   | :white_check_mark: |
| < 1.0   | :x:                |

## Reporting a Vulnerability

We take the security of TellMe seriously. If you discover a security vulnerability in this project, **please do not create a public issue**. 

Instead, please privately report it by emailing us at [security@tellme-project.org](mailto:security@tellme-project.org).

### What happens after reporting

1. **Acknowledgment**: We will acknowledge receipt of your vulnerability report within 48 hours.
2. **Investigation**: We will investigate the issue and determine its severity and validity.
3. **Resolution**: If confirmed, we will develop a patch.
4. **Disclosure**: Once the patch is released, we may publicly acknowledge your contribution if you consent.

## Out-of-Scope Items

The following items are generally out of scope for our security program:
- Denial of Service (DoS) attacks at the network layer.
- Spamming features (e.g., creating bulk users/complaints). Rate limiting is a planned feature but not a severe vulnerability.
- Theoretical vulnerabilities without a valid proof of concept.

## Known Limitations

Please be aware of the following known limitation in the current version of the application:
- **Password Hashing**: Currently, user passwords are encrypted using SHA-256 without a salt. This is a known limitation in `v1.0.x`. A migration to `BCrypt` is actively planned for `v1.1`. Please consider this when deploying in a high-security environment.

## Security Best Practices for Deployers

When deploying TellMe to a production environment, please follow these best practices:
- **Use HTTPS**: Always deploy the application behind a reverse proxy (like Nginx or Apache) with TLS/SSL enabled to encrypt traffic.
- **Environment Variables**: Never hardcode credentials in `application.properties`. Always use environment variables for database credentials, SMTP passwords, and API keys.
- **File Uploads**: Ensure that the `UPLOAD_DIR` is strictly configured to not allow executable scripts and is ideally served via a dedicated web server to prevent arbitrary code execution.
