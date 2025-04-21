package com.example.logicalback.repository;

import com.example.logicalback.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE u.active = true")
    List<User> findAllActiveUsers();
    
    @Query("SELECT u, COUNT(t) AS taskCount FROM User u LEFT JOIN u.tasks t GROUP BY u.id")
    List<Object[]> findAllUsersWithTaskCount();
} 