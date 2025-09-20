package com.voip.voip_user_mgmt_api.repository;

import com.voip.voip_user_mgmt_api.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByName(String name);
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByExtension(String extension);
    
    boolean existsByExtension(String extension);
    
    boolean existsByUsername(String username);
}
