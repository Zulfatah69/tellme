# Configuration Reference

The TellMe application is strictly configured via Environment Variables. Use a `.env` file in the root directory (or `docker/` directory for Docker setups) to provide these values.

## Environment Variables

| Variable | Default | Required | Description |
|---|---|---|---|
| `SERVER_PORT` | `8081` | No | The port the Spring Boot embedded server will run on. |
| `DB_URL` | `jdbc:mysql://localhost:3306/tellme` | Yes | JDBC URL for the MySQL database. |
| `DB_USERNAME` | - | Yes | MySQL database user. |
| `DB_PASSWORD` | - | Yes | MySQL database password. |
| `JPA_DDL_AUTO` | `update` | No | Hibernate DDL strategy (`update`, `validate`, `none`). |
| `JPA_SHOW_SQL` | `false` | No | Print SQL queries to standard out (useful for dev). |
| `MAIL_HOST` | `smtp.gmail.com` | Yes | SMTP server host. |
| `MAIL_PORT` | `587` | Yes | SMTP server port. |
| `MAIL_USERNAME` | - | Yes | Email account used to authenticate with SMTP. |
| `MAIL_PASSWORD` | - | Yes | App password or SMTP password. |
| `MAIL_FROM` | - | Yes | The "From" address for sent emails. |
| `MAIL_ROUTING_ORGANISASI` | - | Yes | Target email address for organizational complaints. |
| `MAIL_ROUTING_AKADEMIK` | - | Yes | Target email address for academic complaints. |
| `UPLOAD_DIR` | `uploads` | No | Path to store user-uploaded files. |
| `UPLOAD_MAX_FILES` | `5` | No | Max number of files allowed per upload. |
| `MAX_FILE_SIZE` | `5MB` | No | Max allowed size for a single file upload. |
| `MAX_REQUEST_SIZE` | `20MB` | No | Max total size for a multipart request. |

## Gmail App Password Setup
If you are using Gmail for the SMTP server (`MAIL_HOST=smtp.gmail.com`), you cannot use your regular account password due to security restrictions.
1. Enable 2-Step Verification on your Google Account.
2. Go to Google Account Settings -> Security -> App Passwords.
3. Generate a new App Password for "Mail".
4. Copy the 16-character password into `MAIL_PASSWORD` in your `.env`.

## JPA DDL Strategies
- **Development**: Use `JPA_DDL_AUTO=update` so Spring Boot creates and modifies tables automatically based on entities.
- **Production**: Set `JPA_DDL_AUTO=validate` or `none` and manage schema changes using a tool like Flyway or Liquibase to prevent accidental data loss.

## Upload Directory Setup
The `UPLOAD_DIR` variable defines where images and attachments are saved.
- Locally: It defaults to a folder named `uploads` in the project root.
- Docker: It is mapped to `/app/uploads` inside the container, backed by a persistent Docker volume to survive container restarts.
