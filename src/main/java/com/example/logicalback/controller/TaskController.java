package com.example.logicalback.controller;

import com.example.logicalback.dto.TaskDTO;
import com.example.logicalback.model.Task;
import com.example.logicalback.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Slf4j
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDTO) {
        log.info("REST请求 - 创建新任务");
        return new ResponseEntity<>(taskService.createTask(taskDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        log.info("REST请求 - 获取任务 ID: {}", id);
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        log.info("REST请求 - 获取所有任务");
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<TaskDTO>> getTasksPaginated(Pageable pageable) {
        log.info("REST请求 - 获取分页任务, 页码: {}, 每页大小: {}", pageable.getPageNumber(), pageable.getPageSize());
        return ResponseEntity.ok(taskService.getTasksPaginated(pageable));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskDTO>> getTasksByUserId(@PathVariable Long userId) {
        log.info("REST请求 - 获取用户 ID: {} 的所有任务", userId);
        return ResponseEntity.ok(taskService.getTasksByUserId(userId));
    }

    @GetMapping("/user/{userId}/paginated")
    public ResponseEntity<Page<TaskDTO>> getTasksByUserIdPaginated(@PathVariable Long userId, Pageable pageable) {
        log.info("REST请求 - 获取用户 ID: {} 的分页任务, 页码: {}, 每页大小: {}", 
                userId, pageable.getPageNumber(), pageable.getPageSize());
        return ResponseEntity.ok(taskService.getTasksByUserIdPaginated(userId, pageable));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<TaskDTO>> getTasksByCategoryId(@PathVariable Long categoryId) {
        log.info("REST请求 - 获取分类 ID: {} 的所有任务", categoryId);
        return ResponseEntity.ok(taskService.getTasksByCategoryId(categoryId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TaskDTO>> getTasksByStatus(@PathVariable Task.TaskStatus status) {
        log.info("REST请求 - 获取状态为 {} 的所有任务", status);
        return ResponseEntity.ok(taskService.getTasksByStatus(status));
    }

    @GetMapping("/due-between")
    public ResponseEntity<List<TaskDTO>> getTasksDueBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        log.info("REST请求 - 获取截止日期在 {} 和 {} 之间的任务", start, end);
        return ResponseEntity.ok(taskService.getTasksDueBetween(start, end));
    }

    @GetMapping("/search")
    public ResponseEntity<List<TaskDTO>> searchTasksByKeyword(
            @RequestParam Long userId,
            @RequestParam String keyword) {
        log.info("REST请求 - 搜索用户 ID: {} 包含关键词 '{}' 的任务", userId, keyword);
        return ResponseEntity.ok(taskService.searchTasksByKeyword(userId, keyword));
    }

    @GetMapping("/tag/{tagId}")
    public ResponseEntity<List<TaskDTO>> getTasksByTagId(@PathVariable Long tagId) {
        log.info("REST请求 - 获取标签 ID: {} 的所有任务", tagId);
        return ResponseEntity.ok(taskService.getTasksByTagId(tagId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @Valid @RequestBody TaskDTO taskDTO) {
        log.info("REST请求 - 更新任务 ID: {}", id);
        return ResponseEntity.ok(taskService.updateTask(id, taskDTO));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskDTO> updateTaskStatus(
            @PathVariable Long id,
            @RequestParam Task.TaskStatus status) {
        log.info("REST请求 - 更新任务 ID: {} 的状态为 {}", id, status);
        return ResponseEntity.ok(taskService.updateTaskStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        log.info("REST请求 - 删除任务 ID: {}", id);
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count/user/{userId}/status/{status}")
    public ResponseEntity<Long> countTasksByUserAndStatus(
            @PathVariable Long userId,
            @PathVariable Task.TaskStatus status) {
        log.info("REST请求 - 统计用户 ID: {} 状态为 {} 的任务数量", userId, status);
        return ResponseEntity.ok(taskService.countTasksByUserAndStatus(userId, status));
    }

    @PostMapping("/{taskId}/tags/{tagId}")
    public ResponseEntity<Void> addTagToTask(@PathVariable Long taskId, @PathVariable Long tagId) {
        log.info("REST请求 - 为任务 ID: {} 添加标签 ID: {}", taskId, tagId);
        taskService.addTagToTask(taskId, tagId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{taskId}/tags/{tagId}")
    public ResponseEntity<Void> removeTagFromTask(@PathVariable Long taskId, @PathVariable Long tagId) {
        log.info("REST请求 - 从任务 ID: {} 移除标签 ID: {}", taskId, tagId);
        taskService.removeTagFromTask(taskId, tagId);
        return ResponseEntity.noContent().build();
    }
} 