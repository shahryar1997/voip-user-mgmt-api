package com.voip.voip_user_mgmt_api.config;

import com.voip.voip_user_mgmt_api.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * STEP 9: Spring Security Configuration
 * 
 * This is the main configuration class that sets up the entire security framework.
 * It defines:
 * 1. Which endpoints are public vs protected
 * 2. How authentication is handled
 * 3. How passwords are encoded
 * 4. The order of security filters
 * 5. Session management strategy
 * 
 * Authentication Flow Context:
 * 1. User makes request to any endpoint
 * 2. JwtAuthenticationFilter processes JWT token (if present)
 * 3. SecurityConfig checks if endpoint requires authentication
 * 4. If protected and no valid authentication, returns 401 Unauthorized
 * 5. If public or valid authentication, request proceeds to controller
 * 
 * Key Responsibilities:
 * - Configure endpoint security (public vs protected)
 * - Set up authentication providers
 * - Configure password encoding
 * - Define filter chain order
 * - Handle unauthorized access
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    /**
     * STEP 9A: Password Encoder Bean
     * 
     * BCryptPasswordEncoder is used to:
     * 1. Hash passwords when storing them in database (during user creation)
     * 2. Compare submitted passwords with stored hashes (during login)
     * 
     * BCrypt automatically handles salt generation and is very secure
     * 
     * @return BCryptPasswordEncoder for password hashing and comparison
     */
     //toxsec-5.1
    @Bean
        public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * STEP 9B: Authentication Provider Bean
     * 
     * DaoAuthenticationProvider connects Spring Security with our custom components:
     * 1. UserDetailsService - loads user information from database
     * 2. PasswordEncoder - compares submitted password with stored hash
     * 
     * This is what actually performs the authentication during login
     * 
     * @return DaoAuthenticationProvider configured with our services
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(org.springframework.security.core.userdetails.UserDetailsService userDetailsService,
                                                            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }
    
    /**
     * STEP 9C: Authentication Manager Bean
     * 
     * AuthenticationManager is the main entry point for authentication operations.
     * It's used by AuthController to authenticate users during login.
     * 
     * @param authConfig - Spring's authentication configuration
     * @return AuthenticationManager for handling authentication
     * @throws Exception if configuration fails
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    /**
     * STEP 9D: Security Filter Chain Configuration
     * 
     * This is the heart of the security configuration. It defines:
     * 1. Which endpoints are public vs protected
     * 2. How to handle CSRF protection
     * 3. Session management strategy
     * 4. Filter order and configuration
     * 
     * Security Flow:
     * 1. Request comes in
     * 2. JwtAuthenticationFilter processes JWT token (if present)
     * 3. SecurityConfig checks endpoint security rules
     * 4. If protected and no valid auth, returns 401
     * 5. If public or valid auth, request proceeds
     * 
     * @param http - HttpSecurity builder for configuring security
     * @return SecurityFilterChain with all security rules applied
     * @throws Exception if configuration fails
     */
     //toxsec-10
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           JwtAuthenticationFilter jwtAuthenticationFilter,
                                           org.springframework.security.authentication.AuthenticationProvider authenticationProvider) throws Exception {
        http
            // STEP 9E: Disable CSRF protection
            // CSRF is not needed for stateless JWT-based APIs
            .csrf(csrf -> csrf.disable())
            
            // STEP 9F: Configure session management
            // STATELESS means no server-side sessions (perfect for JWT)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // STEP 9G: Configure endpoint security
            .authorizeHttpRequests(auth -> 
                auth
                    // Public endpoints - no authentication required
                    .requestMatchers("/api/auth/**").permitAll()  // Login, register, etc.
                    .requestMatchers("/ping").permitAll()        // Health check
                    
                    // Protected endpoints - authentication required
                    .requestMatchers("/api/users/**").authenticated()  // All user management endpoints
                    
                    // Default rule - all other endpoints require authentication
                    .anyRequest().authenticated()
            );
        
        // STEP 9H: Add our custom authentication provider
        // This enables username/password authentication during login
        http.authenticationProvider(authenticationProvider);
        
        // STEP 9I: Add JWT authentication filter
        // This filter runs before Spring Security's default filters
        // It processes JWT tokens and sets up authentication context
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        // STEP 9J: Build and return the security configuration
        return http.build();
    }


    /**
     *  The Authorization Flow: Using Your ID Card
     * This flow happens for every single request to a protected endpoint (like /api/users/all). It's entirely automated by the SecurityFilterChain.
     *
     * Step A: The Request Enters the Filter Chain: A client sends a request with the JWT in the Authorization header. This request doesn't go straight to the controller; it's first intercepted by the SecurityFilterChain that you configured in SecurityConfig.
     *
     * Step B: JwtAuthenticationFilter Jumps In: Thanks to http.addFilterBefore(jwtAuthenticationFilter, ...), your custom filter runs very early in the chain. Its doFilterInternal method is called automatically.
     *
     * Step C: The JWT is Processed: The doFilterInternal method extracts the token, and then uses your JwtUtils to validate it (validateToken). It also uses JwtUtils to get the username from the token's claims.
     *
     * Step D: UserDetailsService Confirms the User: The filter calls userDetailsService.loadUserByUsername again. This time, it's not for a password check, but to retrieve the user's details and authorities from the database.
     *
     * Step E: The Security Context is Set: If the token is valid and the user is found, the JwtAuthenticationFilter creates an Authentication object and stores it in the SecurityContextHolder. This is the single most important action in this flow. This is where the magic happens.
     *
     * Step F: The Request Continues: After your filter finishes, it calls filterChain.doFilter(...). The request now proceeds down the chain to your controller, but now, it has a "stamp of approval." Any controller method with @PreAuthorize or any endpoint configured with .authenticated() in SecurityConfig will see the user as authenticated and allow the request to proceed. If the filter failed to authenticate, SecurityConfig would block the request and return a 401 Unauthorized error.
     */
    /**
     * STEP 9K: UserDetailsService Bean
     * 
     * This service is used by Spring Security to load user information.
     * It's called during:
     * 1. Login authentication (by AuthenticationManager)
     * 2. JWT token validation (by JwtAuthenticationFilter)
     * 
     * @return UserDetailsServiceImpl for loading user details
     */
    // Rely on component-scanned UserDetailsServiceImpl (@Service)
}
