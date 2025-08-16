package com.voip.voip_user_mgmt_api.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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
    private String name;
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
