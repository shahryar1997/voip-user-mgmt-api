package com.voip.voip_user_mgmt_api.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.voip.voip_user_mgmt_api.Entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * STEP 6E: UserDetails Implementation
 * 
 * This class implements Spring Security's UserDetails interface, which is the
 * standard way Spring Security represents authenticated users.
 * 
 * Authentication Flow Context:
 * 1. UserDetailsService loads User entity from database
 * 2. UserDetailsService calls UserDetailsImpl.build() to convert User to UserDetails
 * 3. Spring Security uses this UserDetails object for:
 *    - Password comparison during login
 *    - JWT token generation
 *    - Authorization decisions
 *    - User context in security filters
 * 
 * Key Responsibilities:
 * - Provide user information in Spring Security format
 * - Handle password comparison (Spring Security will compare submitted password with this)
 * - Define user authorities/roles for authorization
 * - Control account status (enabled, locked, expired, etc.)
 */
public class UserDetailsImpl implements UserDetails {
    
    private static final long serialVersionUID = 1L;
    
    // User identification and profile information
    private Long id;
    private String username;
    private String name;
    private String extension;
    
    /**
     * Password field - marked with @JsonIgnore to prevent password exposure in JSON responses
     * This field contains the encrypted password from the database
     * Spring Security will use this to compare with the submitted password during login
     */
    @JsonIgnore
    private String password;
    
    /**
     * User authorities/roles - defines what the user can do in the system
     * Currently set to ROLE_USER for all users
     * This can be expanded to support different user roles (ADMIN, MODERATOR, etc.)
     */
    private Collection<? extends GrantedAuthority> authorities;
    
    /**
     * Constructor for creating UserDetailsImpl from database User entity
     * 
     * @param id - Database user ID
     * @param username - Unique username for login
     * @param password - Encrypted password from database
     * @param name - Display name
     * @param extension - VoIP extension number
     */
    public UserDetailsImpl(Long id, String username, String password, String name, String extension) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.extension = extension;
        
        // STEP 6F: Set user authorities/roles
        // Currently all users get ROLE_USER authority
        // This can be expanded based on user type or database role field
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }
    
    /**
     * STEP 6G: Factory method to create UserDetailsImpl from User entity
     * 
     * This method is called by UserDetailsService to convert database User to Spring Security UserDetails
     * 
     * @param user - Database User entity
     * @return UserDetailsImpl - Spring Security compatible user representation
     */
    public static UserDetailsImpl build(User user) {
         //toxsec-4
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getExtension());
    }
    
    /**
     * STEP 6H: Get user authorities/roles
     * 
     * Spring Security uses this to determine what the user can access
     * Currently returns ROLE_USER for all users
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    
    // Custom getters for additional user information
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getExtension() {
        return extension;
    }
    
    /**
     * STEP 6I: Get encrypted password for comparison
     * 
     * Spring Security calls this method to get the stored password
     * It then uses PasswordEncoder to compare submitted password with this stored hash
     */
     //toxsec-5
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * STEP 6J: Get username for authentication
     * 
     * Spring Security uses this to identify the user
     * Must match the username submitted during login
     */
    @Override
    public String getUsername() {
        return username;
    }
    
    /**
     * STEP 6K: Account status methods
     * 
     * These methods control whether the user account is active and usable
     * Currently all return true (account is active)
     * Can be modified to support account locking, expiration, etc.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true; // Account never expires
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true; // Account is never locked
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Credentials never expire
    }
    
    @Override
    public boolean isEnabled() {
        return true; // Account is always enabled
    }
    
    /**
     * Equals and hashCode methods for proper user comparison
     * Users are considered equal if they have the same ID
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
