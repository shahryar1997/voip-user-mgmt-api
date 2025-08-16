package com.voip.voip_user_mgmt_api.controller;

import com.voip.voip_user_mgmt_api.Entity.User;
import com.voip.voip_user_mgmt_api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user/")
public class UserController {

    @Autowired
    UserService userService;

    // @GetMapping("/default")
    // public com.voip.voip_user_mgmt_api.models.User getUser() {
    //     return userService.getDefaultUser();
    // }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/save")
    public void saveUser(@RequestBody User user) {
        userService.saveUser(user);
    }

    @PutMapping("/update/{id}")
    public void updateUser(@PathVariable Long id, @RequestBody User user) {
        userService.updateUser(id, user);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUserByID(id);
    }
}
