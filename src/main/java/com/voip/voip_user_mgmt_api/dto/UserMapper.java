package com.voip.voip_user_mgmt_api.dto;

import com.voip.voip_user_mgmt_api.Entity.User;

/**
 * Mapper utility for converting between DTOs and User entities
 */
public class UserMapper {

    /**
     * Converts UserCreateRequest to User entity
     */
    public static User toEntity(UserCreateRequest request) {
        if (request == null) {
            return null;
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setName(request.getName());
        user.setExtension(request.getExtension());
        return user;
    }

    /**
     * Converts UserUpdateRequest to User entity
     */
    public static User toEntity(UserUpdateRequest request) {
        if (request == null) {
            return null;
        }
        
        User user = new User();
        user.setName(request.getName());
        user.setExtension(request.getExtension());
        return user;
    }

    /**
     * Updates existing User entity with UserUpdateRequest data
     */
    public static void updateEntity(User existingUser, UserUpdateRequest request) {
        if (existingUser == null || request == null) {
            return;
        }
        
        if (request.getName() != null) {
            existingUser.setName(request.getName());
        }
        
        if (request.getExtension() != null) {
            existingUser.setExtension(request.getExtension());
        }
    }

    /**
     * Creates UserCreateRequest from User entity
     */
    public static UserCreateRequest toCreateRequest(User user) {
        if (user == null) {
            return null;
        }
        
        return new UserCreateRequest(user.getUsername(), user.getPassword(), user.getName(), user.getExtension());
    }

    /**
     * Creates UserUpdateRequest from User entity
     */
    public static UserUpdateRequest toUpdateRequest(User user) {
        if (user == null) {
            return null;
        }
        
        return new UserUpdateRequest(user.getName(), user.getExtension());
    }
}
