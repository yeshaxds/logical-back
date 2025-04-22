package com.example.logicalback.repository;

import com.example.logicalback.entity.Role;
import com.example.logicalback.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    List<User> findByRole(Role role);
    
    List<User> findByActiveTrue();
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u, COUNT(t) FROM User u LEFT JOIN u.tasks t GROUP BY u")
    List<Object[]> findAllUsersWithTaskCount();
    
    @Query("SELECT COUNT(t) FROM User u JOIN u.tasks t WHERE u.id = :userId")
    Long countByUserIdAndTasksIsNotNull(@Param("userId") Long userId);
} 