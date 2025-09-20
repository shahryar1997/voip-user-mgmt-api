# Spring Security JWT Authentication Flow Summary

## Complete Flow Sequence

### **LOGIN PROCESS (Steps 1-9)**

1. **User submits POST /api/auth/login** with username/password
2. **LoginRequest validation** - Spring validates input
3. **AuthController receives** validated request
4. **Creates authentication token** for Spring Security
5. **AuthenticationManager.authenticate()** called
6. **UserDetailsService loads user** from database
7. **UserDetailsImpl created** with Spring Security format
8. **JWT token generated** using JwtUtils
9. **JwtResponse returned** to user with token

### **PROTECTED REQUEST PROCESS (Steps 10-13)**

10. **User makes request** to protected endpoint with JWT token
11. **JwtAuthenticationFilter intercepts** and validates token
12. **SecurityConfig checks** endpoint access rules
13. **Request proceeds** to controller if authorized

## Key Classes and Their Roles

- **LoginRequest**: Step 1 - User credentials input
- **AuthController**: Steps 2-4, 8-9 - Orchestrates authentication
- **UserDetailsServiceImpl**: Step 6 - Loads user from database
- **UserDetailsImpl**: Step 7 - Spring Security user representation
- **JwtUtils**: Step 8 - JWT token generation and validation
- **JwtAuthenticationFilter**: Step 11 - Processes JWT tokens
- **SecurityConfig**: Step 12 - Defines security rules
- **JwtResponse**: Step 9 - Returns token to user

## Security Features

- BCrypt password hashing
- JWT token expiration (24 hours)
- Stateless authentication
- Protected endpoint configuration
- Input validation
- Username/password uniqueness
