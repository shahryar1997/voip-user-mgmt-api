package com.voip.voip_user_mgmt_api.exceptions;

/**
 * Custom exception for validation errors
 */
public class ValidationException extends RuntimeException {
    
    public ValidationException(String message) {
        super(message);
    }
    
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
