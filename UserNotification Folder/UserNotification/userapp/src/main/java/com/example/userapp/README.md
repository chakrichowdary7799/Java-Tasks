# Notification System API

## How to Run
1. Clone the project
2. Open in VS Code
3. Run: ./mvnw spring-boot:run
4. Open: http://localhost:8080

## APIs

### User APIs
- POST /api/users - Create user
- GET /api/users - Get all users
- GET /api/users/{id} - Get user by ID

### Preference APIs
- PUT /api/users/{id}/preferences/{channel}?enabled=true
- GET /api/users/{id}/preferences

### Notification APIs
- POST /api/notifications/send?userId=1&channel=EMAIL&message=Hello
- GET /api/notifications/user/{id}
- GET /api/notifications/user/{id}/status/SENT
- GET /api/notifications/user/{id}/channel/EMAIL

## Tech Stack
- Java + Spring Boot
- H2 Database
- JPA/Hibernate
- Layered Architecture