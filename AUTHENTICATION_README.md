# VoIP User Management API - Authentication Guide

## Overview
This API now includes Spring Security with JWT-based authentication. All user management endpoints are protected and require a valid JWT token.

## Authentication Flow

### 1. Login
**Endpoint:** `POST /api/auth/login`

**Request Body:**
```json
{
  "username": "testuser",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "username": "testuser",
  "name": "Test User"
}
```

### 2. Using the Token
Include the JWT token in the `Authorization` header for all protected endpoints:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

## Protected Endpoints

All `/api/users/**` endpoints require authentication:

- `GET /api/users/all` - Get all users
- `GET /api/users/by-id?id={id}` - Get user by ID
- `GET /api/users/by-extension?extension={extension}` - Get user by extension
- `GET /api/users/check-extension?extension={extension}` - Check extension availability
- `POST /api/users/create` - Create new user
- `PUT /api/users/update/{id}` - Update user
- `DELETE /api/users/delete/{id}` - Delete user

## Public Endpoints

- `GET /ping` - Health check
- `POST /api/auth/login` - Authentication

## Test User

A test user is automatically created with the following credentials:
- **Username:** `testuser`
- **Password:** `password123`
- **Name:** `Test User`
- **Extension:** `1001`

## User Creation

When creating a new user, the request must include:
- `username` (3-50 characters, alphanumeric + underscore)
- `password` (6-100 characters)
- `name` (2-100 characters, letters, spaces, hyphens, apostrophes)
- `extension` (4-6 digits)

**Example:**
```json
{
  "username": "johndoe",
  "password": "securepass123",
  "name": "John Doe",
  "extension": "1002"
}
```

## Security Features

- **Password Encryption:** All passwords are encrypted using BCrypt
- **JWT Tokens:** Stateless authentication with configurable expiration
- **Username Uniqueness:** Usernames must be unique across the system
- **Extension Uniqueness:** Extensions must be unique across the system
- **Input Validation:** Comprehensive validation for all user inputs

## Configuration

JWT settings can be configured in `application.properties`:
```properties
jwt.secret=your-secret-key-here-make-it-long-and-secure-in-production
jwt.expiration=86400000
```

**Note:** In production, change the JWT secret to a secure, randomly generated key.

## Testing the API

1. **Login to get a token:**
   ```bash
   curl -X POST http://localhost:8080/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username":"testuser","password":"password123"}'
   ```

2. **Use the token to access protected endpoints:**
   ```bash
   curl -X GET http://localhost:8080/api/users/all \
     -H "Authorization: Bearer YOUR_TOKEN_HERE"
   ```

3. **Create a new user:**
   ```bash
   curl -X POST http://localhost:8080/api/users/create \
     -H "Authorization: Bearer YOUR_TOKEN_HERE" \
     -H "Content-Type: application/json" \
     -d '{"username":"newuser","password":"newpass123","name":"New User","extension":"1003"}'
   ```
