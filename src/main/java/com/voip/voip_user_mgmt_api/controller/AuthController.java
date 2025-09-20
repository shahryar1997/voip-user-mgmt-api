package com.voip.voip_user_mgmt_api.controller;

import com.voip.voip_user_mgmt_api.dto.JwtResponse;
import com.voip.voip_user_mgmt_api.dto.LoginRequest;
import com.voip.voip_user_mgmt_api.security.JwtUtils;
import com.voip.voip_user_mgmt_api.security.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * STEP 2: Authentication Controller
 * 
 * This controller handles the login endpoint and orchestrates the authentication process.
 * It receives the validated LoginRequest and coordinates with Spring Security components.
 * 
 * Complete Authentication Flow:
 * 1. User sends POST /api/auth/login with username/password (LoginRequest DTO)
 * 2. Spring validates the request using Bean Validation (@Valid)
 * 3. AuthController receives the validated request
 * 4. Controller creates UsernamePasswordAuthenticationToken
 * 5. AuthenticationManager.authenticate() is called
 * 6. AuthenticationManager delegates to UserDetailsService
 * 7. UserDetailsService loads user from database
 * 8. PasswordEncoder compares submitted password with stored hash
 * 9. If authentication succeeds, JWT token is generated
 * 10. Token is returned to user in JwtResponse
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    /**
     * AuthenticationManager - Core Spring Security component that orchestrates authentication
     * It will use our custom UserDetailsService and PasswordEncoder to validate credentials
     */
    @Autowired
    private AuthenticationManager authenticationManager;
    
    /**
     * JwtUtils - Our custom utility class for generating and validating JWT tokens
     */
    @Autowired
    private JwtUtils jwtUtils;
    
    /**
     * STEP 3: Login Endpoint - Main authentication entry point
     * 
     * This method handles the actual login process:
     * 1. Receives validated LoginRequest from user
     * 2. Creates authentication token for Spring Security
     * 3. Delegates authentication to Spring Security framework
     * 4. Generates JWT token if authentication succeeds
     * 5. Returns JWT token to user
     * 
     * @param loginRequest - Validated username/password from user
     * @return JwtResponse containing the JWT token and user info
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("STEP 3: Login request received for user: {}", loginRequest.getUsername());
        
        try {
            //toxsec-1
            // STEP 4: Create authentication token for Spring Security
            // This token contains the raw username/password that needs to be validated
            UsernamePasswordAuthenticationToken authToken = 
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
            
            logger.debug("STEP 4: Created authentication token for user: {}", loginRequest.getUsername());
            
            // STEP 5: Delegate authentication to Spring Security
            // This will trigger the following sequence:
            // 1. AuthenticationManager calls our UserDetailsService
            // 2. UserDetailsService loads user from database
            // 3. PasswordEncoder compares passwords
            // 4. If successful, returns authenticated Authentication object
            //toxsec-2
            Authentication authentication = authenticationManager.authenticate(authToken);
            
            logger.debug("STEP 5: Authentication successful for user: {}", loginRequest.getUsername());
            
            // STEP 6: Set authentication in Spring Security context
            // This is useful for the current request, but not required for JWT flow
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // STEP 7: Generate JWT token
            // Extract user details from the authenticated principal and generate token
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
             //toxsec-6
            String jwt = jwtUtils.generateToken(userDetails);
            
            logger.info("STEP 7: JWT token generated successfully for user: {}", userDetails.getUsername());
            
            // STEP 8: Return JWT response to user
            // User will use this token in Authorization header for subsequent requests
             //toxsec-9
            JwtResponse response = new JwtResponse(jwt, userDetails.getUsername(), userDetails.getName());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            // STEP 9: Handle authentication failures
            // This could be wrong password, user not found, or other authentication errors
            logger.error("STEP 9: Authentication failed for user: {}", loginRequest.getUsername(), e);
            return ResponseEntity.badRequest().body("Invalid username or password");
        }
    }
    /**
     * . The Login Flow: Getting Your ID Card
     * This flow starts at your AuthController's /login endpoint and ends with a token being sent back.
     *
     * Step A: The Request Hits Your Controller (AuthController): A POST request to /api/auth/login is received. Your authenticateUser method is the first piece of your custom code to run. It holds the key to the entire flow: authenticationManager.authenticate(authToken).
     *
     * Step B: The AuthenticationManager Takes Over: This call is the magical trigger. The AuthenticationManager is like a manager who says, "I've received an authentication request. I will now find the right team to handle it." It knows to use the DaoAuthenticationProvider because you configured it in your SecurityConfig's filterChain bean (http.authenticationProvider(...)).
     *
     * Step C: The DaoAuthenticationProvider Goes to Work: The DaoAuthenticationProvider is the engine that does the heavy lifting. It uses the UserDetailsService (your UserDetailsServiceImpl) to find the user in the database.
     *
     * Step D: UserDetailsServiceImpl Fetches the User: The loadUserByUsername method in UserDetailsServiceImpl is called. It queries your UserRepository to find the user by their username. It returns a UserDetailsImpl object, which is your User entity wrapped in a format Spring Security understands. This object contains the stored, hashed password.
     *
     * Step E: The Password Is Verified: The DaoAuthenticationProvider uses your PasswordEncoder bean (your BCryptPasswordEncoder) to compare the plain-text password from the login request with the hashed password from the UserDetailsImpl object. If they match, the authentication is successful.
     *
     * Step F: The Token is Generated: The authenticate method returns an Authentication object to your AuthController. You then explicitly call jwtUtils.generateToken(...) to create the JWT. The token is finally sent back to the client.
     */
}
