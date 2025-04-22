package com.example.logicalback.service.impl;

import com.example.logicalback.dto.UserDTO;
import com.example.logicalback.entity.Role;
import com.example.logicalback.entity.User;
import com.example.logicalback.exception.ResourceNotFoundException;
import com.example.logicalback.repository.UserRepository;
import com.example.logicalback.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        log.info("创建新用户: {}", userDTO.getUsername());
        
        if (existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("用户名已存在");
        }
        
        if (existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("邮箱已存在");
        }
        
        User user = User.builder()
                .username(userDTO.getUsername())
                .password("$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG") // 默认密码: password
                .email(userDTO.getEmail())
                .fullName(userDTO.getFullName())
                .role(userDTO.getRole() != null ? userDTO.getRole() : Role.USER)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        User savedUser = userRepository.save(user);
        log.info("用户创建成功: {} (ID: {})", savedUser.getUsername(), savedUser.getId());
        
        return UserDTO.fromEntity(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        log.debug("获取用户信息 ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在, ID: " + id));
        
        return UserDTO.fromEntity(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserByUsername(String username) {
        log.debug("通过用户名获取用户: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在, 用户名: " + username));
        
        return UserDTO.fromEntity(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        log.debug("获取所有用户");
        return userRepository.findAll().stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getActiveUsers() {
        log.debug("获取所有活跃用户");
        return userRepository.findByActiveTrue().stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getUsersByRole(Role role) {
        log.debug("获取角色为 {} 的所有用户", role);
        return userRepository.findByRole(role).stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        log.info("更新用户 ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在, ID: " + id));
        
        // 如果更改了用户名，检查是否已存在
        if (!user.getUsername().equals(userDTO.getUsername()) && existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("用户名已存在");
        }
        
        // 如果更改了邮箱，检查是否已存在
        if (!user.getEmail().equals(userDTO.getEmail()) && existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("邮箱已存在");
        }
        
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setFullName(userDTO.getFullName());
        user.setRole(userDTO.getRole());
        user.setActive(userDTO.isActive());
        user.setUpdatedAt(LocalDateTime.now());
        
        User updatedUser = userRepository.save(user);
        log.info("用户更新成功 ID: {}", updatedUser.getId());
        
        return UserDTO.fromEntity(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.info("删除用户 ID: {}", id);
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("用户不存在, ID: " + id);
        }
        
        userRepository.deleteById(id);
        log.info("用户删除成功 ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getUserTaskCount(Long userId) {
        log.debug("获取用户任务数量 userId: {}", userId);
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("用户不存在, ID: " + userId);
        }
        
        return userRepository.countByUserIdAndTasksIsNotNull(userId);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> findAllUsers() {
        List<Object[]> usersWithTaskCount = userRepository.findAllUsersWithTaskCount();
        List<UserDTO> userDTOs = new ArrayList<>();
        
        for (Object[] result : usersWithTaskCount) {
            User user = (User) result[0];
            Long taskCount = (Long) result[1];
            
            UserDTO userDTO = UserDTO.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .fullName(user.getFullName())
                    .role(user.getRole())
                    .active(user.isActive())
                    .taskCount(taskCount.intValue())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt())
                    .build();
            
            userDTOs.add(userDTO);
        }
        
        return userDTOs;
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserDTO findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .active(user.isActive())
                .taskCount(user.getTasks().size())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> findActiveUsers() {
        return userRepository.findByActiveTrue().stream()
                .map(user -> UserDTO.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .fullName(user.getFullName())
                        .role(user.getRole())
                        .active(user.isActive())
                        .taskCount(user.getTasks().size())
                        .createdAt(user.getCreatedAt())
                        .updatedAt(user.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }
} 