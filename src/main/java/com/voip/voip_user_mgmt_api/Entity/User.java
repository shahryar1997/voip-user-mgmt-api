package com.voip.voip_user_mgmt_api.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;

/*
JSON Format Example:
{
  "id": 1,
  "name": "John Doe",
  "extension": "1001"
}
*/

@Entity
@Table(name = "voip_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @NotBlank(message = "Name cannot be empty", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Pattern(regexp = "^[a-zA-Z\\s\\-']+$", message = "Name can only contain letters, spaces, hyphens, and apostrophes", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String name;
    
    @NotBlank(message = "Extension cannot be empty", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Size(min = 4, max = 6, message = "Extension must be between 4 and 6 characters", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Pattern(regexp = "^[0-9]+$", message = "Extension can only contain numbers", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String extension;

    // Default constructor required by JPA
    public User() {
    }

    public User(Long id, String name, String extension) {
        this.id = id;
        this.name = name;
        this.extension = extension;
    }
    
    public User(String name, String extension) {
        this.name = name;
        this.extension = extension;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    
    @Override
    public String toString() {
        return String.format(
                "Customer[id=%d, name='%s', extension='%s']",
                id, name, extension);
    }
}
