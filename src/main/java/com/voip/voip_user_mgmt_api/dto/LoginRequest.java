package com.voip.voip_user_mgmt_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * STEP 1: Login Request DTO
 * 
 * This is the entry point of the authentication flow.
 * When a user wants to login, they send their credentials in this format.
 * 
 * Flow Sequence:
 * 1. User sends POST request to /api/auth/login with username/password
 * 2. Spring validates this DTO using Bean Validation annotations
 * 3. If validation passes, the request proceeds to AuthController
 * 4. If validation fails, Spring returns 400 Bad Request with validation errors
 */
public class LoginRequest {
    
    /**
     * Username field - must be 3-50 characters, alphanumeric + underscore only
     * This will be used to look up the user in the database
     */
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    
    /**
     * Password field - must be 6-100 characters
     * This will be compared against the encrypted password stored in database
     */
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;

    // Default constructor required by Spring for JSON deserialization
    public LoginRequest() {
    }

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
