package com.voip.voip_user_mgmt_api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * STEP 8: JWT Authentication Filter
 * 
 * This filter intercepts EVERY HTTP request to the application and:
 * 1. Checks if the request contains a JWT token in Authorization header
 * 2. If token exists, validates it and sets up user authentication
 * 3. If no token or invalid token, request continues without authentication
 * 
 * Authentication Flow Context (for subsequent requests after login):
 * 1. User makes request to protected endpoint (e.g., GET /api/users/all)
 * 2. User includes JWT token in Authorization: Bearer <token> header
 * 3. This filter intercepts the request before it reaches the controller
 * 4. Filter extracts and validates the JWT token
 * 5. If valid, sets up Spring Security context for the request
 * 6. Request continues to controller with user already authenticated
 * 7. If invalid/no token, request continues without authentication (will be rejected by SecurityConfig)
 * 
 * Key Responsibilities:
 * - Extract JWT token from Authorization header
 * - Validate JWT token using JwtUtils
 * - Load user details from database if token is valid
 * - Set up Spring Security authentication context
 * - Allow request to continue (authentication success/failure handled by SecurityConfig)
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    
    /**
     * JwtUtils - Utility class for JWT operations
     * Used to extract username from token and validate token
     */
    private final JwtUtils jwtUtils;
    
    /**
     * UserDetailsService - Service for loading user information
     * Called to load user details from database based on username from token
     */
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }
    
    /**
     * STEP 8A: Filter every HTTP request
     * 
     * This method is called by Spring Security for every single HTTP request
     * It runs before the request reaches any controller
     * 
     * The filter:
     * 1. Extracts JWT token from Authorization header
     * 2. If token exists, validates it and sets up authentication
     * 3. If no token or invalid, continues without authentication
     * 4. Always calls filterChain.doFilter() to continue request processing
     * 
     * @param request - HTTP request being processed
     * @param response - HTTP response (not modified by this filter)
     * @param filterChain - Chain of filters to continue processing
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        try {
            // STEP 8B: Extract JWT token from request
            String jwt = parseJwt(request);
            
            // STEP 8C: Process JWT token if it exists and no authentication is set
            if (jwt != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // STEP 8D: Extract username from JWT token
                String username = jwtUtils.extractUsername(jwt);
                
                if (username != null) {
                    // STEP 8E: Load user details from database
                    // This calls our UserDetailsServiceImpl.loadUserByUsername()
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    
                    // STEP 8F: Validate JWT token against user details
                    // Checks if token is valid and not expired
                    if (jwtUtils.validateToken(jwt, userDetails)) {
                        
                        // STEP 8G: Create authentication token for Spring Security
                        // This token represents an authenticated user
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        
                        // STEP 8H: Add request details to authentication
                        // Includes IP address, session ID, etc. for audit purposes
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        
                        // STEP 8I: Set authentication in Spring Security context
                        // This makes the user authenticated for the current request
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        
                        logger.debug("STEP 8I: Authentication set for user: {} in request to: {}", 
                                   username, request.getRequestURI());
                    } else {
                        logger.debug("STEP 8F: JWT token validation failed for user: {}", username);
                    }
                } else {
                    logger.debug("STEP 8D: Could not extract username from JWT token");
                }
            } else if (jwt == null) {
                logger.debug("STEP 8B: No JWT token found in request to: {}", request.getRequestURI());
            } else {
                logger.debug("STEP 8C: Authentication already set for request to: {}", request.getRequestURI());
            }
            
        } catch (Exception e) {
            // STEP 8J: Handle any errors during JWT processing
            // Log error but don't block the request
            // SecurityConfig will handle unauthorized requests
            logger.error("STEP 8J: Error processing JWT token: {}", e.getMessage());
        }
        
        // STEP 8K: Continue request processing
        // This filter never blocks requests - it only sets up authentication
        // SecurityConfig will decide if the request is allowed based on authentication status
        filterChain.doFilter(request, response);
    }
    
    /**
     * STEP 8L: Extract JWT token from Authorization header
     * 
     * Looks for "Authorization: Bearer <token>" header
     * Returns the token part without "Bearer " prefix
     * 
     * @param request - HTTP request to extract token from
     * @return JWT token string, or null if not found
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        
        // Check if header exists and starts with "Bearer "
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            // Return token part (remove "Bearer " prefix)
            return headerAuth.substring(7);
        }
        
        return null;
    }

}
