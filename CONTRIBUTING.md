# Contributing to TellMe

First off, thank you for considering contributing to TellMe! It's people like you that make TellMe a great tool for universities and student organizations.

## Philosophy

We believe in open collaboration, respectful communication, and code quality. Whether you are fixing a bug, adding a new feature, or improving documentation, your help is welcome. 

Please review our [Code of Conduct](CODE_OF_CONDUCT.md) before contributing to ensure a welcoming and inclusive environment for everyone.

## Ways to Contribute

There are many ways to contribute to TellMe:

- **Bug Reports**: If you find a bug, please open an issue using the Bug Report template.
- **Feature Requests**: Have an idea to make TellMe better? Open a feature request issue!
- **Code Contributions**: Submit a Pull Request (PR) to fix bugs or add new features.
- **Documentation**: Help us improve our README, wiki, and inline comments.
- **Translations**: Help make TellMe accessible globally.

## Development Setup

### Prerequisites
- JDK 17+
- MySQL 8.x
- Git

### Fork & Clone
1. Fork the repository on GitHub.
2. Clone your fork locally:
   ```bash
   git clone https://github.com/YOUR_USERNAME/tellme.git
   cd tellme
   ```
3. Add the upstream repository:
   ```bash
   git remote add upstream https://github.com/Zulfatah69/tellme.git
   ```

### Run Locally
Ensure your MySQL server is running and you have created the `tellme_db`. Start the application via Maven:
```bash
./mvnw spring-boot:run
```

## Branch Naming Conventions

Create a new branch for your contribution. Use the following prefixes to categorize your branch:

- `feat/` for new features (e.g., `feat/add-avatar-upload`)
- `fix/` for bug fixes (e.g., `fix/login-crash`)
- `docs/` for documentation updates (e.g., `docs/update-readme`)
- `chore/` for maintenance tasks, dependencies (e.g., `chore/bump-spring-version`)

## Commit Message Style

We follow [Conventional Commits](https://www.conventionalcommits.org/). This leads to more readable messages that are easy to follow when looking through the project history.

Format:
```
<type>[optional scope]: <description>

[optional body]
```

Examples:
- `feat(auth): add password reset functionality`
- `fix(ui): correct padding on mobile dashboard`
- `docs: update setup instructions`

## Coding Standards

- **Java**: We follow standard Java conventions (e.g., Google Java Style). Prefer constructor injection over field injection (`@Autowired`).
- **Frontend**: Keep HTML/CSS clean. Use ES6+ syntax for JavaScript.
- **Formatting**: Ensure your code is properly formatted before committing.

## Testing Requirements

- All new features should have accompanying unit tests (JUnit / Mockito).
- Ensure existing tests pass before submitting a PR.
- To run tests:
  ```bash
  ./mvnw test
  ```

## Pull Request Process

1. Ensure your code passes all tests and linting.
2. Push your branch to your fork.
3. Open a Pull Request against the `main` branch of the upstream repository.
4. Fill out the PR template completely.
5. Wait for a review. Maintainers may request changes.
6. Once approved, a maintainer will merge your PR.

Thank you for your contribution!
