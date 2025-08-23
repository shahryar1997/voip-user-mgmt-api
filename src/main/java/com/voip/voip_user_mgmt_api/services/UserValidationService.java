package com.voip.voip_user_mgmt_api.services;

import com.voip.voip_user_mgmt_api.Entity.User;
import com.voip.voip_user_mgmt_api.Entity.ValidationGroups;
import com.voip.voip_user_mgmt_api.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserValidationService {

    private static final Logger log = LoggerFactory.getLogger(UserValidationService.class);
    private final Validator validator;

    @Autowired
    public UserValidationService(Validator validator) {
        this.validator = validator;
        log.info("UserValidationService initialized with validator: {}", validator.getClass().getSimpleName());
    }

    /**
     * Validates a user for creation
     * @param user the user to validate
     * @throws ValidationException if validation fails
     */
    public void validateForCreate(User user) {
        log.debug("Starting validation for user creation: {}", user.getName());
        validateUser(user, ValidationGroups.Create.class, "User creation validation failed");
        log.debug("User creation validation completed successfully for: {}", user.getName());
    }

    /**
     * Validates a user for update
     * @param user the user to validate
     * @throws ValidationException if validation fails
     */
    public void validateForUpdate(User user) {
        log.debug("Starting validation for user update: {}", user.getName());
        validateUser(user, ValidationGroups.Update.class, "User update validation failed");
        log.debug("User update validation completed successfully for: {}", user.getName());
    }

    /**
     * Validates a user for partial update
     * @param user the user to validate
     * @throws ValidationException if validation fails
     */
    public void validateForPartialUpdate(User user) {
        log.debug("Starting validation for user partial update: {}", user.getName());
        validateUser(user, ValidationGroups.PartialUpdate.class, "User partial update validation failed");
        log.debug("User partial update validation completed successfully for: {}", user.getName());
    }

    /**
     * Core validation method
     * @param user the user to validate
     * @param groups the validation groups to apply
     * @param errorMessage the base error message
     * @throws ValidationException if validation fails
     */
    private void validateUser(User user, Class<?> groups, String errorMessage) {
        if (user == null) {
            log.error("Validation failed: User object is null");
            throw new ValidationException("User object cannot be null");
        }

        log.debug("Validating user with groups: {}, user: {}", groups.getSimpleName(), user.getName());
        Set<ConstraintViolation<User>> violations = validator.validate(user, groups);
        
        if (!violations.isEmpty()) {
            String detailedErrors = violations.stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .collect(Collectors.joining("; "));
            
            log.warn("Validation violations found for user {}: {}", user.getName(), detailedErrors);
            throw new ValidationException(errorMessage + ": " + detailedErrors);
        }
        
        log.debug("Bean validation passed for user: {}", user.getName());
    }

    /**
     * Business rule validation - checks for duplicate extensions
     * @param user the user to validate
     * @param existingExtension the existing extension (if any)
     * @throws ValidationException if business rules are violated
     */
    public void validateBusinessRules(User user, String existingExtension) {
        log.debug("Validating business rules for user: {}, existing extension: {}", 
                 user.getName(), existingExtension);
        
        if (user.getExtension() != null && !user.getExtension().equals(existingExtension)) {
            log.debug("Extension changed, validating new extension: {}", user.getExtension());
            validateExtensionFormat(user.getExtension());
        }
        
        log.debug("Business rule validation completed successfully for user: {}", user.getName());
    }

    /**
     * Validates extension format and business rules
     * @param extension the extension to validate
     * @throws ValidationException if extension is invalid
     */
    private void validateExtensionFormat(String extension) {
        log.debug("Validating extension format: {}", extension);
        
        if (extension == null || extension.trim().isEmpty()) {
            log.warn("Extension validation failed: extension is null or empty");
            throw new ValidationException("Extension cannot be null or empty");
        }

        if (!extension.matches("^[0-9]+$")) {
            log.warn("Extension validation failed: extension {} contains non-numeric characters", extension);
            throw new ValidationException("Extension can only contain numbers");
        }

        if (extension.length() < 4 || extension.length() > 6) {
            log.warn("Extension validation failed: extension {} length {} is not between 4-6", 
                    extension, extension.length());
            throw new ValidationException("Extension must be between 4 and 6 characters");
        }

        // Additional business rules can be added here
        // For example, checking for reserved extensions
        if ("0000".equals(extension) || "9999".equals(extension)) {
            log.warn("Extension validation failed: extension {} is reserved", extension);
            throw new ValidationException("Extension " + extension + " is reserved and cannot be used");
        }
        
        log.debug("Extension format validation passed: {}", extension);
    }

    /**
     * Validates user name format and business rules
     * @param name the name to validate
     * @throws ValidationException if name is invalid
     */
    public void validateName(String name) {
        log.debug("Validating name format: {}", name);
        
        if (name == null || name.trim().isEmpty()) {
            log.warn("Name validation failed: name is null or empty");
            throw new ValidationException("Name cannot be null or empty");
        }

        if (name.trim().length() < 2) {
            log.warn("Name validation failed: name '{}' length {} is less than 2", 
                    name, name.trim().length());
            throw new ValidationException("Name must be at least 2 characters long");
        }

        if (name.trim().length() > 100) {
            log.warn("Name validation failed: name '{}' length {} exceeds 100", 
                    name, name.trim().length());
            throw new ValidationException("Name cannot exceed 100 characters");
        }

        if (!name.matches("^[a-zA-Z\\s\\-']+$")) {
            log.warn("Name validation failed: name '{}' contains invalid characters", name);
            throw new ValidationException("Name can only contain letters, spaces, hyphens, and apostrophes");
        }
        
        log.debug("Name format validation passed: {}", name);
    }
}
