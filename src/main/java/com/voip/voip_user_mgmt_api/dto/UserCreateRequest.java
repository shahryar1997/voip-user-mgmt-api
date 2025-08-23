package com.voip.voip_user_mgmt_api.dto;

import com.voip.voip_user_mgmt_api.Entity.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO for user creation requests
 */
public class UserCreateRequest {
    
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

    public UserCreateRequest(String name, String extension) {
        this.name = name;
        this.extension = extension;
    }

    // Getters and setters
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
