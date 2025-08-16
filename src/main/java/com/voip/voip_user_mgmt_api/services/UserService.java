package com.voip.voip_user_mgmt_api.services;


import com.voip.voip_user_mgmt_api.Entity.User;
import com.voip.voip_user_mgmt_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    // public com.voip.voip_user_mgmt_api.models.User getDefaultUser(){
    //     return new com.voip.voip_user_mgmt_api.models.User(1,"sherry","3434");
    // }

    public void saveUser(User user){
         userRepository.save(user);
    }
    public List<User> getAllUsers(){
       return (List<User>) userRepository.findAll();
    }
    public void deleteUserByID(Long id){
         userRepository.deleteById(id);
    }
    public void updateUser(Long id, User user){
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            existingUser.setName(user.getName());
            existingUser.setExtension(user.getExtension());
            // Add more fields here as needed
            userRepository.save(existingUser);
        }
    }

    
}
