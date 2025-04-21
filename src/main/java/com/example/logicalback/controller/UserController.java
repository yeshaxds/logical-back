package com.example.logicalback.controller;

import com.example.logicalback.dto.UserDTO;
import com.example.logicalback.model.User;
import com.example.logicalback.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        log.info("REST请求 - 创建新用户");
        return new ResponseEntity<>(userService.createUser(userDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        log.info("REST请求 - 获取用户ID: {}", id);
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        log.info("REST请求 - 获取用户名: {}", username);
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        log.info("REST请求 - 获取所有用户");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/active")
    public ResponseEntity<List<UserDTO>> getActiveUsers() {
        log.info("REST请求 - 获取所有活跃用户");
        return ResponseEntity.ok(userService.getActiveUsers());
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserDTO>> getUsersByRole(@PathVariable User.UserRole role) {
        log.info("REST请求 - 获取角色为 {} 的所有用户", role);
        return ResponseEntity.ok(userService.getUsersByRole(role));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        log.info("REST请求 - 更新用户 ID: {}", id);
        return ResponseEntity.ok(userService.updateUser(id, userDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("REST请求 - 删除用户 ID: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/task-count")
    public ResponseEntity<Long> getUserTaskCount(@PathVariable Long id) {
        log.info("REST请求 - 获取用户 ID: {} 的任务数量", id);
        return ResponseEntity.ok(userService.getUserTaskCount(id));
    }
} 