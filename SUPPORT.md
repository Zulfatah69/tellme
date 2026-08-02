# Support

Thank you for using TellMe! We want to ensure you have the resources you need to successfully deploy and use the platform.

## How to Get Help

If you have questions, need assistance, or want to discuss the project:

- **GitHub Discussions**: The best place to ask questions, share ideas, and connect with other users is our [GitHub Discussions](#) board.
- **GitHub Issues**: If you have found a bug or have a concrete feature request, please open an issue in our [Issue Tracker](#).

## Frequently Asked Questions (FAQ)

### 1. How do I set up the database?
Make sure you have MySQL 8.0+ installed. Create an empty database named `tellme_db`. The Spring Boot application will automatically generate the schema on startup via Hibernate when run.

### 2. My email notifications aren't working. What's wrong?
Ensure your `MAIL_USERNAME` and `MAIL_PASSWORD` are correct. If you are using Gmail, you likely need to generate an **App Password** rather than using your standard account password, as Google restricts standard SMTP logins.

### 3. I forgot the admin password, how do I reset it?
Since TellMe does not yet have a built-in "forgot password" flow for admins, you can manually update the password hash in the `users` table of your MySQL database. 

### 4. Where are uploaded images stored?
By default, images are stored in the local `/uploads` directory relative to where the application is run. You can change this by modifying the `UPLOAD_DIR` environment variable.

### 5. Can I use PostgreSQL instead of MySQL?
TellMe is officially tested and supported with MySQL. However, since it uses Spring Data JPA, you can theoretically switch to PostgreSQL by changing the JDBC driver in `pom.xml` and updating the `SPRING_DATASOURCE_URL` and dialect properties.

## Reporting Bugs

If you believe you've found a bug in TellMe, we want to hear about it! Please follow these steps:
1. Search the existing [Issues](#) to see if it has already been reported.
2. If not, open a new Issue using the **Bug Report** template.
3. Provide as much detail as possible, including logs, steps to reproduce, and environment details.

## Community

Join our community of developers and academic administrators! 
- Contribute code: [CONTRIBUTING.md](CONTRIBUTING.md)
- Code of Conduct: [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md)

## Commercial Support Note

**This is a community-driven open-source project with no paid or commercial support tier.** 

Support is provided by volunteers on a best-effort basis. We encourage you to participate in discussions and help out others where you can!
