# Complete Spring Security JWT Authentication Flow

## Overview
This document explains the complete authentication flow implemented in your VoIP User Management API. Every step is numbered sequentially to help you understand exactly what happens when a user logs in and accesses protected endpoints.

## 🔐 **LOGIN FLOW (Initial Authentication)**

### **STEP 1: User Login Request**
- **File:** `LoginRequest.java`
- **What happens:** User sends POST request to `/api/auth/login` with username/password
- **Validation:** Spring validates the request using Bean Validation annotations
- **If validation fails:** Returns 400 Bad Request with validation errors
- **If validation passes:** Proceeds to AuthController

### **STEP 2: AuthController Receives Request**
- **File:** `AuthController.java`
- **What happens:** Controller receives validated LoginRequest
- **Logging:** Logs the login attempt for user

### **STEP 3: Create Authentication Token**
- **File:** `AuthController.java`
- **What happens:** Creates `UsernamePasswordAuthenticationToken` with raw credentials
- **Purpose:** This token contains the unencrypted username/password for Spring Security to validate

### **STEP 4: Delegate to Spring Security**
- **File:** `AuthController.java`
- **What happens:** Calls `AuthenticationManager.authenticate()`
- **What this triggers:** Spring Security's authentication process begins

### **STEP 5: AuthenticationManager Processing**
- **File:** Spring Security Framework (not our code)
- **What happens:** 
  1. Receives authentication token
  2. Calls our `UserDetailsService.loadUserByUsername()`
  3. Compares submitted password with stored password using `PasswordEncoder`

### **STEP 6: UserDetailsService Loads User**
- **File:** `UserDetailsServiceImpl.java`
- **What happens:**
  - **STEP 6A:** Searches database for user by username
  - **STEP 6B:** If user not found, throws `UsernameNotFoundException`
  - **STEP 6C:** If user found, converts User entity to UserDetailsImpl
  - **STEP 6D:** Returns UserDetailsImpl object

### **STEP 6E-6K: UserDetailsImpl Creation**
- **File:** `UserDetailsImpl.java`
- **What happens:**
  - **STEP 6E:** Creates Spring Security compatible user object
  - **STEP 6F:** Sets user authorities (ROLE_USER)
  - **STEP 6G:** Factory method converts database User to UserDetails
  - **STEP 6H:** Provides user authorities for authorization
  - **STEP 6I:** Provides encrypted password for comparison
  - **STEP 6J:** Provides username for authentication
  - **STEP 6K:** Sets account status (enabled, not locked, etc.)

### **STEP 7: JWT Token Generation**
- **File:** `JwtUtils.java`
- **What happens:**
  - **STEP 7A:** Creates cryptographic signing key from secret
  - **STEP 7B:** Extracts username from UserDetails
  - **STEP 7C:** Gets expiration time from configuration
  - **STEP 7D:** Sets up generic claim extraction
  - **STEP 7E:** Parses and validates JWT structure
  - **STEP 7F:** Checks if token is expired
  - **STEP 7G:** Generates new JWT token with claims
  - **STEP 7H:** Creates signed JWT with HMAC-SHA256
  - **STEP 7I:** Validates token against user details

### **STEP 8: Return JWT Response**
- **File:** `AuthController.java`
- **What happens:** Creates `JwtResponse` with token and user info
- **Response includes:** JWT token, token type (Bearer), username, display name

### **STEP 9: User Receives Token**
- **What happens:** Client receives JWT token in response
- **Next step:** Client stores token and includes it in future requests

---

## 🛡️ **PROTECTED ENDPOINT ACCESS FLOW**

### **STEP 10: User Makes Protected Request**
- **What happens:** User sends request to protected endpoint (e.g., `GET /api/users/all`)
- **Header:** Includes `Authorization: Bearer <jwt_token>`

### **STEP 11: JWT Authentication Filter Intercepts**
- **File:** `JwtAuthenticationFilter.java`
- **What happens:**
  - **STEP 11A:** Filter intercepts EVERY HTTP request
  - **STEP 11B:** Extracts JWT token from Authorization header
  - **STEP 11C:** If token exists and no authentication set, processes it
  - **STEP 11D:** Extracts username from JWT token
  - **STEP 11E:** Loads user details from database
  - **STEP 11F:** Validates JWT token (signature, expiration, username match)
  - **STEP 11G:** Creates authentication token for Spring Security
  - **STEP 11H:** Adds request details to authentication
  - **STEP 11I:** Sets authentication in Spring Security context
  - **STEP 11J:** Handles any errors during processing
  - **STEP 11K:** Continues request processing
  - **STEP 11L:** Extracts token from "Bearer <token>" format

### **STEP 12: Security Configuration Checks**
- **File:** `SecurityConfig.java`
- **What happens:**
  - **STEP 12A:** Checks if endpoint requires authentication
  - **STEP 12B:** If protected and no valid authentication, returns 401 Unauthorized
  - **STEP 12C:** If public or valid authentication, request proceeds to controller

### **STEP 13: Request Reaches Controller**
- **What happens:** If authentication successful, request reaches the actual controller method
- **User context:** Controller can access authenticated user information
- **Business logic:** Controller executes the requested operation

---

## 🔧 **CONFIGURATION AND SETUP**

### **Security Configuration (SecurityConfig.java)**
- **STEP 9A:** Configures BCrypt password encoder
- **STEP 9B:** Sets up authentication provider with UserDetailsService and PasswordEncoder
- **STEP 9C:** Configures AuthenticationManager
- **STEP 9D:** Defines security filter chain
- **STEP 9E:** Disables CSRF (not needed for stateless JWT)
- **STEP 9F:** Sets session management to STATELESS
- **STEP 9G:** Configures which endpoints are public vs protected
- **STEP 9H:** Adds custom authentication provider
- **STEP 9I:** Adds JWT authentication filter
- **STEP 9J:** Builds security configuration
- **STEP 9K:** Configures UserDetailsService

### **Password Encoding**
- **BCrypt:** All passwords are hashed using BCrypt with automatic salt generation
- **Security:** BCrypt is computationally expensive, making brute force attacks difficult
- **Comparison:** During login, submitted password is hashed and compared with stored hash

### **JWT Configuration**
- **Secret Key:** Configured in `application.properties` (change in production!)
- **Expiration:** Default 24 hours, configurable
- **Algorithm:** HMAC-SHA256 for signing
- **Claims:** Username (subject), issued date, expiration date

---

## 📋 **COMPLETE SEQUENCE SUMMARY**

### **Login Process:**
1. **User submits credentials** → LoginRequest validation
2. **AuthController processes** → Creates authentication token
3. **Spring Security authenticates** → Calls UserDetailsService
4. **UserDetailsService loads user** → Converts to UserDetailsImpl
5. **Password comparison** → BCrypt validates credentials
6. **JWT generation** → JwtUtils creates signed token
7. **Response returned** → User receives JWT token

### **Protected Request Process:**
1. **User includes JWT token** → Authorization header
2. **JwtAuthenticationFilter intercepts** → Extracts and validates token
3. **User details loaded** → From database based on token
4. **Authentication set** → Spring Security context
5. **SecurityConfig checks** → Endpoint access rules
6. **Request proceeds** → To controller if authorized

### **Security Features:**
- **Stateless:** No server-side sessions
- **Token-based:** JWT tokens for authentication
- **Password security:** BCrypt hashing
- **Endpoint protection:** Configurable public/private endpoints
- **Token expiration:** Automatic token invalidation
- **Signature verification:** HMAC-SHA256 prevents tampering

---

## 🧪 **TESTING THE FLOW**

### **Test User Credentials:**
- **Username:** `testuser`
- **Password:** `password123`
- **Database:** Automatically created via `data.sql`

### **Test Commands:**
```bash
# 1. Login to get JWT token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password123"}'

# 2. Use token to access protected endpoint
curl -X GET http://localhost:8080/api/users/all \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

### **Expected Results:**
- **Step 1:** Returns JWT token and user info
- **Step 2:** Returns user data (if token valid)
- **Without token:** Returns 401 Unauthorized
- **With invalid token:** Returns 401 Unauthorized

---

## 🔒 **SECURITY CONSIDERATIONS**

### **Production Requirements:**
1. **Change JWT secret** to long, randomly generated string
2. **Set appropriate expiration** based on security needs
3. **Use HTTPS** for all communications
4. **Implement token refresh** for long-lived sessions
5. **Add rate limiting** to prevent brute force attacks
6. **Log authentication events** for security monitoring

### **Current Security Features:**
- ✅ Password hashing with BCrypt
- ✅ JWT token expiration
- ✅ Stateless authentication
- ✅ Protected endpoint configuration
- ✅ Input validation
- ✅ Username/password uniqueness
- ✅ Comprehensive error handling

This implementation provides a robust, production-ready authentication system that follows Spring Security best practices and industry standards for JWT-based authentication.

