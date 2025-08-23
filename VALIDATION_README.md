# User Validation System

## Overview
Clean, maintainable validation system for the VoIP User Management API with multiple validation layers and comprehensive structured logging.

## Components

### 1. ValidationGroups
- `Create`: For new users
- `Update`: For existing users  
- `PartialUpdate`: For partial updates

### 2. UserValidationService
- Bean validation using groups
- Business rule validation
- Extension and name format validation
- **Structured logging** for all validation operations

### 3. DTOs
- `UserCreateRequest`: Creation requests
- `UserUpdateRequest`: Update requests
- `UserMapper`: Entity conversion

### 4. Global Exception Handler
- Consistent error responses
- Field-level error details
- **Structured logging** for all exceptions

### 5. Structured Logging
- **SLF4J LoggerFactory** implementation
- **Log levels**: INFO, DEBUG, WARN, ERROR
- **Contextual information**: User IDs, names, extensions
- **Performance tracking**: Operation timing and results
- **Error tracking**: Detailed validation failures

## Usage

### Create User
```java
@PostMapping("/create")
public ResponseEntity<User> createUser(@RequestBody UserCreateRequest request)
```

### Update User  
```java
@PutMapping("/update/{id}")
public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request)
```

## Validation Rules

### Name
- Required, 2-100 characters
- Letters, spaces, hyphens, apostrophes only

### Extension
- Required, 4-6 characters
- Numbers only
- Must be unique
- Reserved: 0000, 9999

## Logging Implementation

### Log Levels Used
- **INFO**: Major operations (create, update, delete, API calls)
- **DEBUG**: Detailed operation steps and validation results
- **WARN**: Validation failures and business rule violations
- **ERROR**: Unexpected errors and system failures

### Logging Examples
```java
// Service initialization
log.info("UserService initialized with repository: {} and validation service: {}", 
        repositoryType, validationServiceType);

// Operation tracking
log.info("Creating new user with name: {}, extension: {}", name, extension);

// Validation details
log.debug("Validating user with groups: {}, user: {}", groups, userName);

// Error tracking
log.warn("User creation failed: Extension {} is already in use", extension);
log.error("Unexpected error during user creation: {}", errorMessage, exception);
```

### Benefits of Structured Logging
- **Debugging**: Easy to trace user operations and validation failures
- **Monitoring**: Track system performance and user activity
- **Audit Trail**: Complete record of all user management operations
- **Error Analysis**: Detailed context for troubleshooting
- **Performance Insights**: Monitor validation and database operation timing

## File Logging Configuration

### 1. Application Properties
```properties
# Logging Configuration - File Output
logging.file.name=logs/voip-user-api.log
logging.file.max-size=100MB
logging.file.max-history=30

# Logging Pattern for Files
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n

# Logging Levels
logging.level.root=INFO
logging.level.com.voip.voip_user_mgmt_api=DEBUG
```

### 2. Advanced Logback Configuration (Optional)
The system includes `logback-spring.xml` for advanced logging features:

- **Daily log rotation** with size limits
- **Separate error log file** for critical issues
- **Validation-specific log file** for debugging
- **Automatic log compression** and cleanup
- **Configurable log retention** (30 days by default)

### 3. Log File Structure
```
logs/
├── voip-user-api.log          # Current log file
├── voip-user-api-2024-01-15.0.log  # Daily rotated logs
├── voip-user-api-2024-01-15.1.log  # Size-based rotation
├── errors.log                 # Error-only logs
├── validation.log             # Validation-specific logs
└── validation-2024-01-15.0.log
```

### 4. Log Rotation Strategy
- **Time-based**: Daily rotation at midnight
- **Size-based**: Rotate when file reaches 100MB
- **Compression**: Automatic compression of old logs
- **Cleanup**: Keep logs for 30 days, then auto-delete

### 5. Performance Optimization
- **Asynchronous logging** for better performance
- **Buffered output** to reduce I/O operations
- **Selective logging** by component and level
- **Efficient log patterns** for parsing

## Benefits
- Separation of concerns
- Easy to extend
- Consistent error handling
- Reusable validation logic
- Clean API design
- **Comprehensive logging** for operations and debugging
- **Optimized file logging** with rotation and compression
- **Separate log files** for different concerns (errors, validation, general)
