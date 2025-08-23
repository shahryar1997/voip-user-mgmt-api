# VoIP User Management API

A Spring Boot REST API for managing users in a VoIP system. This application provides CRUD operations for VoIP users with their names and extensions.

## 🆕 Recent Updates (Latest Release)

### **Comprehensive Validation System & Structured Logging** ✨
- **🔒 Multi-layer Validation**: Bean validation, business rules, and custom validation logic
- **📝 Validation Groups**: Separate validation for Create, Update, and PartialUpdate operations
- **📊 DTOs**: Clean separation between API requests and entity validation
- **📋 Structured Logging**: Comprehensive logging with SLF4J LoggerFactory
- **📁 File Logging**: Optimized log rotation with separate files for errors, validation, and general logs
- **⚡ Performance**: Automatic log rotation, compression, and cleanup

### **New API Endpoints**
- `POST /api/user/create` - Create user with validation
- `PUT /api/user/update/{id}` - Update user with validation
- `GET /api/user/by-extension` - Find user by extension
- `GET /api/user/check-extension` - Check extension availability

### **Enhanced Features**
- **Business Rule Validation**: Extension uniqueness, reserved numbers (0000, 9999)
- **Global Exception Handling**: Consistent error responses with detailed logging
- **Repository Enhancements**: Extension-based queries and existence checks
- **Service Layer Improvements**: Comprehensive validation orchestration

---

## Features

- **User Management**: Create, read, update, and delete VoIP users
- **RESTful API**: Clean REST endpoints following best practices
- **PostgreSQL Database**: Robust relational database for production use
- **JPA/Hibernate**: Modern data persistence with Spring Data JPA
- **Comprehensive Testing**: Unit tests with MockMvc for all endpoints
- **🔒 Advanced Validation**: Multi-layer validation system with business rules
- **📊 Structured Logging**: Production-ready logging with file rotation
- **📝 Clean Architecture**: Separation of concerns with DTOs and validation services

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
| `GET` | `/by-extension?extension={ext}` | Get user by extension | - | User object |
| `GET` | `/check-extension?extension={ext}` | Check if extension is available | - | Boolean |
| `POST` | `/create` | Create new user with validation | UserCreateRequest JSON | Created user |
| `PUT` | `/update/{id}` | Update user by ID with validation | UserUpdateRequest JSON | Updated user |
| `DELETE` | `/delete/{id}` | Delete user by ID | - | - |

### Request/Response Models

#### UserCreateRequest (for POST /create)
```json
{
  "name": "John Doe",
  "extension": "1001"
}
```

#### UserUpdateRequest (for PUT /update/{id})
```json
{
  "name": "John Doe Updated",
  "extension": "1002"
}
```

#### User Response Model
```json
{
  "id": 1,
  "name": "John Doe",
  "extension": "1001"
}
```

**Fields:**
- `id`: Unique identifier (auto-generated)
- `name`: User's full name (2-100 characters, letters/spaces/hyphens/apostrophes only)
- `extension`: VoIP extension number (4-6 digits, must be unique, reserved: 0000, 9999)

### Example API Calls

#### Create a User
```bash
curl -X POST http://localhost:8080/api/user/create \
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

#### Get User by Extension
```bash
curl http://localhost:8080/api/user/by-extension?extension=1001
```

#### Check Extension Availability
```bash
curl http://localhost:8080/api/user/check-extension?extension=1001
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
- **URL**: `jdbc:postgresql://192.168.0.108:5432/voip_db`
- **Username**: `postgres`
- **Password**: `mysecretpassword`
- **DDL Auto**: `update` (automatically creates/updates tables)
- **Connection Pool**: HikariCP (default Spring Boot connection pool)

### Environment Variables

You can also configure the database using environment variables:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://192.168.0.108:5432/voip_db
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=mysecretpassword
```

## Project Structure

```
voip-user-mgmt-api/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/voip/voip_user_mgmt_api/
│   │   │       ├── controller/          # REST controllers
│   │   │       ├── Entity/              # JPA entities with validation
│   │   │       ├── dto/                 # Data Transfer Objects
│   │   │       ├── repository/          # Data access layer
│   │   │       ├── services/            # Business logic & validation
│   │   │       ├── exceptions/          # Custom exception handling
│   │   │       └── VoipUserMgmtApiApplication.java
│   │   └── resources/
│   │       ├── application.properties   # Configuration
│   │       └── logback-spring.xml       # Advanced logging configuration
│   └── test/                            # Test files
├── logs/                                # Application log files
├── pom.xml                              # Maven configuration
├── README.md                            # This file
└── VALIDATION_README.md                 # Detailed validation system docs
```

## Configuration

Key configuration options in `application.properties`:

- `spring.jpa.show-sql=false` - SQL queries logging (disabled for production)
- `spring.jpa.hibernate.ddl-auto=update` - Auto-updates database schema
- `logging.file.name=logs/voip-user-api.log` - File logging configuration
- `logging.file.max-size=100MB` - Log file size limit
- `logging.file.max-history=30` - Log retention period

## Logging Configuration

The application includes comprehensive structured logging:

- **File Logging**: Automatic log rotation with daily and size-based rollover
- **Separate Log Files**: 
  - `voip-user-api.log` - General application logs
  - `errors.log` - Error-only logs
  - `validation.log` - Validation-specific logs
- **Log Levels**: Configurable per component (INFO, DEBUG, WARN, ERROR)
- **Performance**: Asynchronous logging with buffered output

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
      POSTGRES_DB: voip_db
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: mysecretpassword
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/voip_db
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: mysecretpassword
    depends_on:
      - postgres
    volumes:
      - ./logs:/app/logs

volumes:
  postgres_data:
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## Additional Documentation

- **VALIDATION_README.md**: Comprehensive guide to the validation system
- **Logging**: Advanced logging configuration and usage
- **API Documentation**: Detailed endpoint specifications and examples