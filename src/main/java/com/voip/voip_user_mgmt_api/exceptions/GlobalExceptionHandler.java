package com.voip.voip_user_mgmt_api.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles ValidationException
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(ValidationException ex) {
        log.warn("Handling ValidationException: {}", ex.getMessage());
        
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Validation Error");
        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Handles MethodArgumentNotValidException (Bean Validation errors)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.warn("Handling MethodArgumentNotValidException: {}", ex.getMessage());
        
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Validation Error");
        response.put("message", "Invalid input parameters");
        response.put("status", HttpStatus.BAD_REQUEST.value());
        
        // Add field-specific errors
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            fieldErrors.put(error.getField(), error.getDefaultMessage())
        );
        response.put("fieldErrors", fieldErrors);
        
        log.debug("Field validation errors: {}", fieldErrors);
        
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Handles generic RuntimeException
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        log.error("Handling RuntimeException: {}", ex.getMessage(), ex);
        
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Internal Server Error");
        response.put("message", "An unexpected error occurred");
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
