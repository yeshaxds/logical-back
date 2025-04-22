package com.example.logicalback.service;

import com.example.logicalback.dto.UserDTO;
import com.example.logicalback.entity.Role;

import java.util.List;

public interface UserService {
    
    UserDTO createUser(UserDTO userDTO);
    
    UserDTO getUserById(Long id);
    
    UserDTO getUserByUsername(String username);
    
    List<UserDTO> getAllUsers();
    
    List<UserDTO> getActiveUsers();
    
    List<UserDTO> getUsersByRole(Role role);
    
    UserDTO updateUser(Long id, UserDTO userDTO);
    
    void deleteUser(Long id);
    
    Long getUserTaskCount(Long userId);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);

    List<UserDTO> findAllUsers();
    
    UserDTO findUserById(Long id);
    
    List<UserDTO> findActiveUsers();
} 