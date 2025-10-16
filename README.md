# Shopping Application
## BACKEND

This is the backend for the **Shopping Application**, a Spring Boot-based RESTful API for managing users, products, orders, and categories. It includes secure JWT-based authentication and role-based authorization.

---

## Tech Stack

* **Java 17**
* **Spring Boot 3.3.4**
* **Spring Security** with JWT
* **Spring Data JPA**
* **MySQL**
* **ModelMapper**
* **Java Faker** (for generating test data)
* **Maven** (build tool)

---

## Features

* JWT Authentication (login & register endpoints)
* Role-based authorization (ADMIN, USER)
* CRUD APIs for:

  * Users
  * Categories
  * Products (with image upload)
  * Orders & Order Details
* Pagination & validation support

---

## Getting Started

### 1. Clone the Repository

```bash
git clone <application-url>
cd shopapp
```

### 2. Set Up MySQL

Make sure you have MySQL running and create a database:

```sql
CREATE DATABASE shopapp;
```

### 3. Configuration

Edit `src/main/resources/application.yml` if needed:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/shopapp?useSSL=false&serverTimezone=UTC
    username: <username>
    password: <your-db-password>
jwt:
  expiration: <expiration>
  secretKey: <secretKey>
```

### 4. Run the Application

Using Maven:

```bash
./mvnw spring-boot:run
```

Or:

```bash
mvn spring-boot:run
```

### 5. Access the API

The base URL is:

```
http://localhost:8088/api/v1
```

Examples:

* `POST /users/register`
* `POST /users/login`
* `GET /products?page=0&limit=10`

---

## Directory Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/example/shopapp/
│   │       ├── controllers/        # All REST controllers
│   │       ├── configurations/     # Security & Mapper configs
│   │       ├── filters/            # JWT filter
│   │       ├── components/         # JWT utility
│   │       ├── dtos/               # Data Transfer Objects
│   │       ├── models/             # JPA entities
│   │       ├── repositories/       # Spring Data JPA Repos
│   │       ├── responses/          # Custom response classes
│   │       └── services/           # Business logic layer
│   └── resources/
│       └── application.yml         # Configuration file
```

---

## Security Overview

* **JWT Authentication:**

  * Token generated on login and verified on every secured request.
  * Roles used to restrict access at controller and endpoint level.

* **Endpoints that require no auth:**

  * `POST /users/register`
  * `POST /users/login`
  * `GET /products` (read-only)
  * `GET /categories` (read-only)

All other endpoints are secured based on user roles.

---

## Environment Variables

The following properties are required (already included in `application.yml`):

| Key                   | Description                     |
| --------------------- | ------------------------------- |
| `server.port`         | Port to run the backend         |
| `spring.datasource.*` | MySQL DB connection config      |
| `jwt.secretKey`       | Secret key to sign JWT tokens   |
| `jwt.expiration`      | JWT expiration in seconds       |
| `api.prefix`          | API prefix path (e.g., /api/v1) |

---

## Build

To package the application:

```bash
mvn clean package
```

The resulting `.jar` will be in the `target/` directory.

---

## License

This project is for educational/demo purposes.
