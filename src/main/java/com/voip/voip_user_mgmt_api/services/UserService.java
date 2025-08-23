package com.voip.voip_user_mgmt_api.services;

import com.voip.voip_user_mgmt_api.Entity.User;
import com.voip.voip_user_mgmt_api.dto.UserCreateRequest;
import com.voip.voip_user_mgmt_api.dto.UserUpdateRequest;
import com.voip.voip_user_mgmt_api.dto.UserMapper;
import com.voip.voip_user_mgmt_api.repository.UserRepository;
import com.voip.voip_user_mgmt_api.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserValidationService validationService;

    @Autowired
    public UserService(UserRepository userRepository, UserValidationService validationService) {
        this.userRepository = userRepository;
        this.validationService = validationService;
        log.info("UserService initialized with repository: {} and validation service: {}", 
                userRepository.getClass().getSimpleName(), validationService.getClass().getSimpleName());
    }

    /**
     * Creates a new user with comprehensive validation
     */
    public User createUser(UserCreateRequest request) {
        log.info("Creating new user with name: {}, extension: {}", request.getName(), request.getExtension());
        
        try {
            // Validate the request DTO
            validationService.validateForCreate(UserMapper.toEntity(request));
            
            // Convert to entity
            User user = UserMapper.toEntity(request);
            
            // Validate business rules
            validationService.validateBusinessRules(user, null);
            
            // Check if extension already exists
            if (userRepository.existsByExtension(user.getExtension())) {
                log.warn("User creation failed: Extension {} is already in use", user.getExtension());
                throw new ValidationException("Extension " + user.getExtension() + " is already in use");
            }
            
            User savedUser = userRepository.save(user);
            log.info("User created successfully with ID: {}, name: {}, extension: {}", 
                    savedUser.getId(), savedUser.getName(), savedUser.getExtension());
            return savedUser;
            
        } catch (ValidationException e) {
            log.error("User creation failed for name: {}, extension: {}: {}", 
                    request.getName(), request.getExtension(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during user creation for name: {}, extension: {}", 
                    request.getName(), request.getExtension(), e.getMessage(), e);
            throw new ValidationException("Failed to create user: " + e.getMessage());
        }
    }

    /**
     * Updates an existing user with comprehensive validation
     */
    public User updateUser(Long id, UserUpdateRequest request) {
        log.info("Updating user with ID: {}, new name: {}, new extension: {}", 
                id, request.getName(), request.getExtension());
        
        try {
            User existingUser = userRepository.findById(id)
                    .orElseThrow(() -> new ValidationException("User with ID " + id + " not found"));
            
            log.debug("Found existing user: ID: {}, name: {}, extension: {}", 
                    existingUser.getId(), existingUser.getName(), existingUser.getExtension());
            
            // Validate the request DTO
            validationService.validateForUpdate(UserMapper.toEntity(request));
            
            // Validate business rules
            validationService.validateBusinessRules(UserMapper.toEntity(request), existingUser.getExtension());
            
            // Check if new extension conflicts with existing users
            if (!request.getExtension().equals(existingUser.getExtension())) {
                if (userRepository.existsByExtension(request.getExtension())) {
                    log.warn("User update failed: Extension {} is already in use by another user", request.getExtension());
                    throw new ValidationException("Extension " + request.getExtension() + " is already in use");
                }
                log.debug("Extension changed from {} to {}", existingUser.getExtension(), request.getExtension());
            }
            
            // Update the entity
            UserMapper.updateEntity(existingUser, request);
            
            User updatedUser = userRepository.save(existingUser);
            log.info("User updated successfully: ID: {}, name: {}, extension: {}", 
                    updatedUser.getId(), updatedUser.getName(), updatedUser.getExtension());
            return updatedUser;
            
        } catch (ValidationException e) {
            log.error("User update failed for ID: {}: {}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during user update for ID: {}", id, e.getMessage(), e);
            throw new ValidationException("Failed to update user: " + e.getMessage());
        }
    }

    /**
     * Retrieves all users
     */
    public List<User> getAllUsers() {
        log.debug("Retrieving all users");
        List<User> users = userRepository.findAll();
        log.debug("Retrieved {} users", users.size());
        return users;
    }

    /**
     * Retrieves a user by ID
     */
    public User getUserById(Long id) {
        log.debug("Retrieving user by ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ValidationException("User with ID " + id + " not found"));
        log.debug("Retrieved user: ID: {}, name: {}, extension: {}", 
                user.getId(), user.getName(), user.getExtension());
        return user;
    }

    /**
     * Deletes a user by ID
     */
    public void deleteUserById(Long id) {
        log.info("Deleting user with ID: {}", id);
        
        if (!userRepository.existsById(id)) {
            log.warn("User deletion failed: User with ID {} not found", id);
            throw new ValidationException("User with ID " + id + " not found");
        }
        
        userRepository.deleteById(id);
        log.info("User deleted successfully: ID: {}", id);
    }

    /**
     * Finds a user by extension
     */
    public User getUserByExtension(String extension) {
        log.debug("Retrieving user by extension: {}", extension);
        User user = userRepository.findByExtension(extension)
                .orElseThrow(() -> new ValidationException("User with extension " + extension + " not found"));
        log.debug("Retrieved user by extension: ID: {}, name: {}, extension: {}", 
                user.getId(), user.getName(), user.getExtension());
        return user;
    }

    /**
     * Checks if extension is available
     */
    public boolean isExtensionAvailable(String extension) {
        log.debug("Checking if extension is available: {}", extension);
        boolean available = !userRepository.existsByExtension(extension);
        log.debug("Extension {} availability: {}", extension, available);
        return available;
    }
}
