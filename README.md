# VoIP User Management API

A Spring Boot REST API for managing users in a VoIP system. This application provides CRUD operations for VoIP users with their names and extensions.

## Features

- **User Management**: Create, read, update, and delete VoIP users
- **RESTful API**: Clean REST endpoints following best practices
- **PostgreSQL Database**: Robust relational database for production use
- **JPA/Hibernate**: Modern data persistence with Spring Data JPA
- **Comprehensive Testing**: Unit tests with MockMvc for all endpoints

## Technology Stack

- **Java 17**
- **Spring Boot 3.5.4**
- **Spring Data JPA**
- **PostgreSQL Database**
- **Maven** (build tool)
- **JUnit 5** (testing)

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- PostgreSQL 12 or higher
- Git

## Getting Started

### 1. Clone the Repository

```bash
git clone <your-repository-url>
cd voip-user-mgmt-api
```

### 2. Set Up PostgreSQL Database

1. Install PostgreSQL if not already installed
2. Create a new database:
```sql
CREATE DATABASE voip_user_mgmt;
CREATE USER voip_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE voip_user_mgmt TO voip_user;
```

### 3. Configure Database Connection

Update `application.properties` with your PostgreSQL credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/voip_user_mgmt
spring.datasource.username=voip_user
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver
```

### 4. Run the Application

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

### 5. Access the Application

- **API Base URL**: http://localhost:8080
- **Database**: PostgreSQL running on localhost:5432

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

The application uses PostgreSQL database with the following configuration:

- **Database**: PostgreSQL
- **URL**: `jdbc:postgresql://localhost:5432/voip_user_mgmt`
- **Username**: `voip_user`
- **Password**: `your_password`
- **DDL Auto**: `update` (automatically creates/updates tables)
- **Connection Pool**: HikariCP (default Spring Boot connection pool)

### Environment Variables

You can also configure the database using environment variables:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/voip_user_mgmt
export SPRING_DATASOURCE_USERNAME=voip_user
export SPRING_DATASOURCE_PASSWORD=your_password
```

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
- `spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect` - PostgreSQL dialect
- `spring.datasource.hikari.maximum-pool-size=10` - Connection pool size

## Deployment

### Build JAR
```bash
./mvnw clean package
```

### Run JAR
```bash
java -jar target/voip-user-mgmt-api-0.0.1-SNAPSHOT.jar
```

### Docker Deployment

Create a `docker-compose.yml` file for easy deployment:

```yaml
version: '3.8'
services:
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: voip_user_mgmt
      POSTGRES_USER: voip_user
      POSTGRES_PASSWORD: your_password
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/voip_user_mgmt
      SPRING_DATASOURCE_USERNAME: voip_user
      SPRING_DATASOURCE_PASSWORD: your_password
    depends_on:
      - postgres

volumes:
  postgres_data:
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request
