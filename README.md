# Accounting Application

This "Accounting Application" is a Maven-based Spring Boot application, leveraging version `2.7.7` of Spring Boot and Java `17`. It's designed to provide accounting functionalities and integrates with tools and platforms like PostgreSQL, Docker, and AWS.

## Prerequisites

Before you begin, ensure you have met the following requirements:

- Java 17
- Maven
- Docker
- Access to Postgres (locally or through a service)


## Setup & Running the Service

Follow the steps below to get the _Demo Application_ up and running:

1. Navigate to the project root directory in your terminal.
2. Run the following command:
   If you're using _Docker Compose v2_ or later, you should use `docker compose up` and for earlier version, use
   `docker-compose up --remove-orphans`

---

# Java and Spring Boot Coding Standards

## 1. Code Formatting

- **Indentation:**
    - Use tabs for indentation, not spaces.
- **Braces:**
    - Always use braces, even for single statement blocks.
- **Line Length:**
    - Keep lines to a maximum of 120 characters.
- **Whitespace:**
    - Avoid trailing whitespaces.

## 2. Naming Conventions

- **Classes:**
    - Start with an uppercase and use CamelCase.
    - *Example:* `UserService`
- **Branching Strategy:**
    - use jira ticket number and names.
    - *Example:* `JD2-34 User CRUD Operations`
- **Commit Messages:**
    - *Example:* `Adds user list and create`
- **Pull Request Messages:**
  - use jira ticket number and names.
  - *Example:* `JD2-34 User CRUD Operations`
- **Methods:**
    - Start with a lowercase and use camelCase.
    - *Example:* `getUserData()`
- **Variables:**
    - Use meaningful names and avoid single-letter names (except for loop indexes).
    - *Example:* `userList`, `accountStatus`
- **Constants:**
    - Use uppercase with underscores.
    - *Example:* `MAX_RETRY_COUNT`

## 3. Comments

- Write meaningful comments and avoid obvious comments.
- Use Javadoc style comments for classes and methods.
- Comment any code that might appear non-trivial or has business implications.

## 4. Spring Boot Specifics

- **Property Injection:**
    - Use constructor injection over field injection for better testability.
- **Exception Handling:**
    - Use `@ControllerAdvice` and `@ExceptionHandler` to handle exceptions globally.

## 5. General Guidelines

- **Single Responsibility Principle:**
    - A class should have only one reason to change.
- **Avoid Magic Numbers:**
    - Instead of hard coded numbers, use named constants.
- **Null Safety:**
    - Always check for `null` before using an object or provide default values using `Optional`.
- **Logging:**
    - Use appropriate logging levels (INFO, DEBUG, ERROR) and always log exceptions.
- **Unit Testing:**
    - Always write unit tests for your service layers. Aim for a high code coverage.

## 6. Dependencies

- Keep your dependencies up to date.
- Avoid using deprecated libraries or methods.
- Use Maven's `scope` to ensure runtime-only libraries don't get bundled at compile-time.

## 7. Database

- **Naming:**
    - Use `snake_case` for table and column names.
- **Indexes:**
    - Always index foreign keys and frequently searched fields.
    - Example: If `user_id` is frequently searched in a `comments` table, index the `user_id` column.
- **Lazy Loading:**
    - Be cautious when using lazy loading with Hibernate to avoid _N+1 problems_. (Lazy loading can lead to inefficient database queries, especially in scenarios where multiple related entities are loaded separately. Consider using eager loading or fetching strategies to optimize data retrieval.)




---



