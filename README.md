# VoIP User Management API

A Spring Boot REST API for managing users in a VoIP system. This application provides CRUD operations for VoIP users with their names and extensions.

## Features

- **User Management**: Create, read, update, and delete VoIP users
- **RESTful API**: Clean REST endpoints following best practices
- **In-Memory Database**: H2 database for development and testing
- **JPA/Hibernate**: Modern data persistence with Spring Data JPA
- **Comprehensive Testing**: Unit tests with MockMvc for all endpoints

## Technology Stack

- **Java 17**
- **Spring Boot 3.5.4**
- **Spring Data JPA**
- **H2 Database** (in-memory)
- **Maven** (build tool)
- **JUnit 5** (testing)

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Git

## Getting Started

### 1. Clone the Repository

```bash
git clone <your-repository-url>
cd voip-user-mgmt-api
```

### 2. Run the Application

#### Option A: Using Maven Wrapper (Recommended)
```bash
# On Windows
./mvnw.cmd spring-boot:run

# On Unix/Mac
./mvnw spring-boot:run
```

#### Option B: Using Maven
```bash
mvn spring-boot:run
```

### 3. Access the Application

- **API Base URL**: http://localhost:8080
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: `password`

## API Endpoints

### Base URL
```
http://localhost:8080/api/user
```

### Endpoints

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| `GET` | `/all` | Get all users | - | List of users |
| `GET` | `/by-id?id={id}` | Get user by ID | - | User object |
| `POST` | `/save` | Create new user | User JSON | - |
| `PUT` | `/update/{id}` | Update user by ID | User JSON | - |
| `DELETE` | `/delete/{id}` | Delete user by ID | - | - |

### User Model

```json
{
  "id": 1,
  "name": "John Doe",
  "extension": "1001"
}
```

**Fields:**
- `id`: Unique identifier (auto-generated)
- `name`: User's full name
- `extension`: VoIP extension number

### Example API Calls

#### Create a User
```bash
curl -X POST http://localhost:8080/api/user/save \
  -H "Content-Type: application/json" \
  -d '{"name": "John Doe", "extension": "1001"}'
```

#### Get All Users
```bash
curl http://localhost:8080/api/user/all
```

#### Get User by ID
```bash
curl http://localhost:8080/api/user/by-id?id=1
```

#### Update User
```bash
curl -X PUT http://localhost:8080/api/user/update/1 \
  -H "Content-Type: application/json" \
  -d '{"name": "John Doe Updated", "extension": "1002"}'
```

#### Delete User
```bash
curl -X DELETE http://localhost:8080/api/user/delete/1
```

## Running Tests

```bash
# Run all tests
./mvnw test

# Run tests with coverage
./mvnw test jacoco:report
```

## Database Configuration

The application uses H2 in-memory database with the following configuration:

- **Database**: H2 (in-memory)
- **URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: `password`
- **DDL Auto**: `update` (automatically creates/updates tables)
- **Console**: Enabled at `/h2-console`

## Project Structure

```
voip-user-mgmt-api/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/voip/voip_user_mgmt_api/
│   │   │       ├── controller/          # REST controllers
│   │   │       ├── Entity/              # JPA entities
│   │   │       ├── repository/          # Data access layer
│   │   │       ├── services/            # Business logic
│   │   │       └── VoipUserMgmtApiApplication.java
│   │   └── resources/
│   │       └── application.properties   # Configuration
│   └── test/                            # Test files
├── pom.xml                              # Maven configuration
└── README.md                            # This file
```

## Configuration

Key configuration options in `application.properties`:

- `spring.jpa.show-sql=true` - Shows SQL queries in console
- `spring.jpa.hibernate.ddl-auto=update` - Auto-updates database schema
- `spring.h2.console.enabled=true` - Enables H2 database console

## Deployment

### Build JAR
```bash
./mvnw clean package
```

### Run JAR
```bash
java -jar target/voip-user-mgmt-api-0.0.1-SNAPSHOT.jar
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request
