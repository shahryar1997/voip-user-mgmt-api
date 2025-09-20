package com.voip.voip_user_mgmt_api.security;

import com.voip.voip_user_mgmt_api.Entity.User;
import com.voip.voip_user_mgmt_api.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * STEP 6: UserDetailsService Implementation
 * 
 * This service is called by Spring Security's AuthenticationManager during login.
 * It's responsible for loading user information from the database and converting it
 * to Spring Security's UserDetails format.
 * 
 * Authentication Flow Context:
 * 1. User submits login request with username/password
 * 2. AuthController creates UsernamePasswordAuthenticationToken
 * 3. AuthenticationManager.authenticate() is called
 * 4. AuthenticationManager calls this UserDetailsService.loadUserByUsername()
 * 5. This service loads user from database and returns UserDetailsImpl
 * 6. Spring Security then compares the submitted password with stored password
 * 7. If passwords match, authentication succeeds
 * 
 * Key Responsibilities:
 * - Load user from database by username
 * - Convert database User entity to Spring Security UserDetails
 * - Handle cases where user is not found
 * - Provide user information for password comparison
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    
    /**
     * UserRepository - Data access layer for user operations
     * Used to find users by username in the database
     */
    @Autowired
    private UserRepository userRepository;
    
    /**
     * STEP 6A: Load User by Username
     * 
     * This method is automatically called by Spring Security when:
     * 1. AuthenticationManager needs to validate user credentials
     * 2. A JWT token is being validated (in JwtAuthenticationFilter)
     * 
     * The method:
     * 1. Searches database for user with the given username
     * 2. If found, converts User entity to UserDetailsImpl
     * 3. If not found, throws UsernameNotFoundException
     * 
     * @param username - The username to search for
     * @return UserDetailsImpl - Spring Security compatible user representation
     * @throws UsernameNotFoundException - If user doesn't exist
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("STEP 6A: Loading user by username: {}", username);
        
        //toxsec-3
        // STEP 6B: Search database for user
        // This will either find the user or throw UsernameNotFoundException
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("STEP 6B: User not found with username: {}", username);
                    return new UsernameNotFoundException("User not found with username: " + username);
                });
        
        logger.debug("STEP 6C: User found in database: {}", user.getUsername());
        
        // STEP 6D: Convert User entity to UserDetailsImpl
        // This creates a Spring Security compatible user object
        UserDetails userDetails = UserDetailsImpl.build(user);
        
        logger.debug("STEP 6D: UserDetails created successfully for: {}", username);
        return userDetails;
    }
}
