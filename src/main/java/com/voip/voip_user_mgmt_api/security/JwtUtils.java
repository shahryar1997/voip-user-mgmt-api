package com.voip.voip_user_mgmt_api.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * STEP 7: JWT Utility Class
 * 
 * This class handles all JWT (JSON Web Token) operations including:
 * 1. Token generation during successful login
 * 2. Token validation when accessing protected endpoints
 * 3. Token parsing and claim extraction
 * 
 * Authentication Flow Context:
 * 1. After successful login, AuthController calls generateToken()
 * 2. Generated token is returned to user in JwtResponse
 * 3. User includes token in Authorization header for subsequent requests
 * 4. JwtAuthenticationFilter calls validateToken() to verify token
 * 5. If valid, user is authenticated for the request
 * 
 * JWT Token Structure:
 * - Header: Algorithm and token type
 * - Payload: Claims (username, expiration, issued date)
 * - Signature: HMAC-SHA256 signature for verification
 * 
 * Security Features:
 * - Tokens expire after configured time (default: 24 hours)
 * - Signed with secret key to prevent tampering
 * - Contains minimal user information (username only)
 */
@Component
public class JwtUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    
    /**
     * JWT Secret Key - Used to sign and verify tokens
     * In production, this should be a long, randomly generated secret
     * Default value is provided for development
     */
    @Value("${jwt.secret:defaultSecretKey}")
    private String secret;
    
    /**
     * JWT Expiration Time - How long tokens remain valid
     * Default: 24 hours (86400000 milliseconds)
     * Can be configured in application.properties
     */
    @Value("${jwt.expiration:86400000}")
    private Long expiration;
    
    /**
     * STEP 7A: Create signing key from secret
     * 
     * Converts the string secret to a cryptographic key for HMAC-SHA256 signing
     * This key is used to both sign new tokens and verify existing ones
     * 
     * @return SecretKey for JWT signing and verification
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
    
    /**
     * STEP 7B: Extract username from JWT token
     * 
     * Called by JwtAuthenticationFilter to identify the user from the token
     * The username is stored in the JWT subject claim
     * 
     * @param token - JWT token from Authorization header
     * @return Username extracted from token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    /**
     * STEP 7C: Extract expiration date from JWT token
     * 
     * Used to check if token has expired
     * 
     * @param token - JWT token to check
     * @return Date when token expires
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    /**
     * STEP 7D: Generic claim extraction method
     * 
     * Allows extraction of any claim from the JWT token
     * Used by other methods to get specific claims
     * 
     * @param token - JWT token to extract from
     * @param claimsResolver - Function to extract specific claim
     * @return The extracted claim value
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * STEP 7E: Parse JWT token and extract all claims
     * 
     * This is the core method that actually parses the JWT token
     * It verifies the signature using our secret key
     * If signature is invalid, JwtException is thrown
     * 
     * @param token - JWT token to parse
     * @return Claims object containing all token data
     * @throws JwtException if token is malformed or signature is invalid
     */
    private Claims extractAllClaims(String token) {
        try {
            logger.info("Received token "+token);
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            logger.error("STEP 7E: JWT token parsing error: {}", e.getMessage());
            throw e;
        }
    }
    
    /**
     * STEP 7F: Check if JWT token has expired
     * 
     * Compares current time with token expiration time
     * Used during token validation
     * 
     * @param token - JWT token to check
     * @return true if token is expired, false if still valid
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    /**
     * STEP 7G: Generate JWT token for authenticated user
     * 
     * This method is called by AuthController after successful login
     * Creates a new JWT token containing:
     * - Username (subject)
     * - Issue date (current time)
     * - Expiration date (current time + expiration period)
     * - HMAC-SHA256 signature
     * 
     * @param userDetails - UserDetails object from successful authentication
     * @return JWT token string
     */
     //toxsec-7
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }
    
    /**
     * STEP 7H: Create JWT token with claims
     * 
     * Internal method that builds the actual JWT token
     * Sets standard claims and signs with our secret key
     * 
     * @param claims - Additional claims to include (currently empty)
     * @param subject - Username (subject claim)
     * @return Signed JWT token
     */
     //toxsec-8
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)  // Username
                .setIssuedAt(new Date(System.currentTimeMillis()))  // Current time
                .setExpiration(new Date(System.currentTimeMillis() + expiration))  // Expiration time
                .signWith(getSigningKey(), Jwts.SIG.HS256)  // HMAC-SHA256 signature
                .compact();
    }
    
    /**
     * STEP 7I: Validate JWT token
     * 
     * This method is called by JwtAuthenticationFilter for every protected request
     * It performs two checks:
     * 1. Token username matches the userDetails username
     * 2. Token has not expired
     * 
     * @param token - JWT token from Authorization header
     * @param userDetails - UserDetails loaded from database
     * @return true if token is valid, false otherwise
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
