package com.project.playtrack.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    // Optional<User> findByUserName(String userName);
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
    User findByUserName(String userName);
    
    @Query("SELECT u.userName FROM User u")
    List<String> findAllUsernames();
}
