package com.voip.voip_user_mgmt_api.dto;

import com.voip.voip_user_mgmt_api.Entity.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO for user creation requests
 */
public class UserCreateRequest {
    
    @NotBlank(message = "Username cannot be empty", groups = ValidationGroups.Create.class)
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters", groups = ValidationGroups.Create.class)
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores", groups = ValidationGroups.Create.class)
    private String username;
    
    @NotBlank(message = "Password cannot be empty", groups = ValidationGroups.Create.class)
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters", groups = ValidationGroups.Create.class)
    private String password;
    
    @NotBlank(message = "Name cannot be empty", groups = ValidationGroups.Create.class)
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters", groups = ValidationGroups.Create.class)
    @Pattern(regexp = "^[a-zA-Z\\s\\-']+$", message = "Name can only contain letters, spaces, hyphens, and apostrophes", groups = ValidationGroups.Create.class)
    private String name;
    
    @NotBlank(message = "Extension cannot be empty", groups = ValidationGroups.Create.class)
    @Size(min = 4, max = 6, message = "Extension must be between 4 and 6 characters", groups = ValidationGroups.Create.class)
    @Pattern(regexp = "^[0-9]+$", message = "Extension can only contain numbers", groups = ValidationGroups.Create.class)
    private String extension;

    // Default constructor
    public UserCreateRequest() {}

    public UserCreateRequest(String username, String password, String name, String extension) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.extension = extension;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
