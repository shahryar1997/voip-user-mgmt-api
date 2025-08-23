package com.voip.voip_user_mgmt_api.controller;

import com.voip.voip_user_mgmt_api.Entity.User;
import com.voip.voip_user_mgmt_api.dto.UserCreateRequest;
import com.voip.voip_user_mgmt_api.dto.UserUpdateRequest;
import com.voip.voip_user_mgmt_api.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user/")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("GET /api/user/all - Retrieving all users");
        List<User> users = userService.getAllUsers();
        log.debug("Retrieved {} users successfully", users.size());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/by-id")
    public ResponseEntity<User> getUserById(@RequestParam Long id) {
        log.info("GET /api/user/by-id - Retrieving user with ID: {}", id);
        User user = userService.getUserById(id);
        log.debug("Retrieved user successfully: ID: {}, name: {}", user.getId(), user.getName());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/by-extension")
    public ResponseEntity<User> getUserByExtension(@RequestParam String extension) {
        log.info("GET /api/user/by-extension - Retrieving user with extension: {}", extension);
        User user = userService.getUserByExtension(extension);
        log.debug("Retrieved user successfully: ID: {}, name: {}, extension: {}", 
                user.getId(), user.getName(), user.getExtension());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/check-extension")
    public ResponseEntity<Boolean> isExtensionAvailable(@RequestParam String extension) {
        log.info("GET /api/user/check-extension - Checking availability for extension: {}", extension);
        boolean available = userService.isExtensionAvailable(extension);
        log.debug("Extension {} availability check result: {}", extension, available);
        return ResponseEntity.ok(available);
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody UserCreateRequest request) {
        log.info("POST /api/user/create - Creating user with name: {}, extension: {}", 
                request.getName(), request.getExtension());
        User createdUser = userService.createUser(request);
        log.info("User created successfully: ID: {}, name: {}, extension: {}", 
                createdUser.getId(), createdUser.getName(), createdUser.getExtension());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
        log.info("PUT /api/user/update/{} - Updating user with name: {}, extension: {}", 
                id, request.getName(), request.getExtension());
        User updatedUser = userService.updateUser(id, request);
        log.info("User updated successfully: ID: {}, name: {}, extension: {}", 
                updatedUser.getId(), updatedUser.getName(), updatedUser.getExtension());
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("DELETE /api/user/delete/{} - Deleting user", id);
        userService.deleteUserById(id);
        log.info("User deleted successfully: ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
