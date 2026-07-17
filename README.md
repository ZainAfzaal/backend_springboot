EventHub — Event Organizer Backend

A production-style backend for an event management platform, built with **Java, Spring Boot, and MySQL**. Handles user registration, event creation, join/leave registration with concurrency-safe capacity limits, role-based admin control, and automated email notifications.

Link: https://www.eventoo.online

---

## Features

- **Authentication & Authorization** — stateless JWT auth, Google OAuth2 login, BCrypt password hashing, and Role-Based Access Control (Admin / User)
- **Event Registration System** — join/leave endpoints with **optimistic and pessimistic locking** to prevent overbooking under concurrent requests
- **Dynamic Filtering** — search and filter events using Spring Data JPA Specifications (by date, category, location, etc.)
- **Automated Email Notifications** — registration confirmations and event-update alerts via Spring Boot Mail + Thymeleaf templates
- **Admin Panel** — dedicated endpoints for platform management (manage users, events, and registrations)
- **Layered Architecture** — clean separation between controller, service, repository, and DTO layers
- **Global Exception Handling** — consistent, structured API error responses
- **Containerized Deployment** — Dockerized and deployed on Railway.app

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java |
| Framework | Spring Boot, Spring Security, Spring Data JPA, Hibernate |
| Database | MySQL |
| Auth | JWT, OAuth2 (Google), BCrypt |
| Email | Spring Boot Mail, Thymeleaf |
| DevOps | Docker, Maven, Railway.app |
| Tooling | Git/GitHub, Postman |

## Architecture

```
Client
  │
  ▼
Controller Layer   ──▶  DTOs (request/response separation)
  │
  ▼
Service Layer       ──▶  Business logic, locking, filtering
  │
  ▼
Repository Layer    ──▶  Spring Data JPA + Specifications
  │
  ▼
MySQL Database
```

### Prerequisites
- Java 17+
- Maven
- MySQL instance (local or hosted)
- (Optional) Docker

### Clone and configure

```bash
git clone https://github.com/ZainAfzaal/Event_Management.git
cd Event_Management
```

Create an `application.properties` (or `.env`, depending on your config setup) with:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/eventdb
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password

jwt.secret=your_jwt_secret
jwt.expiration=86400000

spring.security.oauth2.client.registration.google.client-id=your_google_client_id
spring.security.oauth2.client.registration.google.client-secret=your_google_client_secret

spring.mail.username=your_email
spring.mail.password=your_email_app_password
```

### Run locally

```bash
mvn clean install
mvn spring-boot:run
```

### Run with Docker

```bash
docker build -t eventhub-backend .
docker run -p 8080:8080 eventhub-backend
```

The API will be available at `http://localhost:8080`.

## API Overview

> Adjust these to match your actual controller mappings — this is a template based on the feature set above.

| Method | Endpoint | Description | Auth |
|---|---|---|---|
| POST | `/api/auth/register` | Register a new user | Public |
| POST | `/api/auth/login` | Log in, receive JWT | Public |
| GET | `/oauth2/authorization/google` | Google OAuth2 login | Public |
| GET | `/api/events` | List/filter events | Public |
| POST | `/api/events` | Create an event | Admin |
| POST | `/api/events/{id}/join` | Join an event | User |
| POST | `/api/events/{id}/leave` | Leave an event | User |
| GET | `/api/admin/users` | Manage users | Admin |


## License

This project is available under the MIT License.

## Author
**Zain Afzaal**
[LinkedIn](https://linkedin.com/in/zainafzaal) · [GitHub](https://github.com/ZainAfzaal) · [Live Demo](https://www.eventoo.online)
