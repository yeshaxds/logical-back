package com.example.logicalback.service.impl;

import com.example.logicalback.dto.TaskDTO;
import com.example.logicalback.exception.ResourceNotFoundException;
import com.example.logicalback.model.Category;
import com.example.logicalback.model.Tag;
import com.example.logicalback.model.Task;
import com.example.logicalback.model.User;
import com.example.logicalback.repository.CategoryRepository;
import com.example.logicalback.repository.TagRepository;
import com.example.logicalback.repository.TaskRepository;
import com.example.logicalback.repository.UserRepository;
import com.example.logicalback.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    @Override
    @Transactional
    public TaskDTO createTask(TaskDTO taskDTO) {
        log.info("创建新任务: {}", taskDTO.getTitle());
        
        User user = null;
        if (taskDTO.getUserId() != null) {
            user = userRepository.findById(taskDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("用户不存在, ID: " + taskDTO.getUserId()));
        }
        
        Category category = null;
        if (taskDTO.getCategoryId() != null) {
            category = categoryRepository.findById(taskDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("分类不存在, ID: " + taskDTO.getCategoryId()));
        }
        
        Task task = Task.builder()
                .title(taskDTO.getTitle())
                .description(taskDTO.getDescription())
                .status(taskDTO.getStatus() != null ? taskDTO.getStatus() : Task.TaskStatus.PENDING)
                .user(user)
                .category(category)
                .priority(taskDTO.getPriority())
                .dueDate(taskDTO.getDueDate())
                .build();
        
        Task savedTask = taskRepository.save(task);
        log.info("任务创建成功 ID: {}", savedTask.getId());
        
        return TaskDTO.fromEntity(savedTask);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskDTO getTaskById(Long id) {
        log.debug("获取任务 ID: {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("任务不存在, ID: " + id));
        
        return TaskDTO.fromEntity(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getAllTasks() {
        log.debug("获取所有任务");
        return taskRepository.findAll().stream()
                .map(TaskDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskDTO> getTasksPaginated(Pageable pageable) {
        log.debug("获取分页任务, 页码: {}, 每页大小: {}", pageable.getPageNumber(), pageable.getPageSize());
        return taskRepository.findAll(pageable)
                .map(TaskDTO::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getTasksByUserId(Long userId) {
        log.debug("获取用户 ID: {} 的所有任务", userId);
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("用户不存在, ID: " + userId);
        }
        
        return taskRepository.findByUserId(userId).stream()
                .map(TaskDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskDTO> getTasksByUserIdPaginated(Long userId, Pageable pageable) {
        log.debug("获取用户 ID: {} 的分页任务, 页码: {}, 每页大小: {}", 
                userId, pageable.getPageNumber(), pageable.getPageSize());
                
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("用户不存在, ID: " + userId);
        }
        
        return taskRepository.findByUserId(userId, pageable)
                .map(TaskDTO::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getTasksByCategoryId(Long categoryId) {
        log.debug("获取分类 ID: {} 的所有任务", categoryId);
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("分类不存在, ID: " + categoryId);
        }
        
        return taskRepository.findByCategoryId(categoryId).stream()
                .map(TaskDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getTasksByStatus(Task.TaskStatus status) {
        log.debug("获取状态为 {} 的所有任务", status);
        return taskRepository.findByStatus(status).stream()
                .map(TaskDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getTasksDueBetween(LocalDateTime start, LocalDateTime end) {
        log.debug("获取截止日期在 {} 和 {} 之间的任务", start, end);
        return taskRepository.findTasksDueBetween(start, end).stream()
                .map(TaskDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> searchTasksByKeyword(Long userId, String keyword) {
        log.debug("搜索用户 ID: {} 包含关键词 '{}' 的任务", userId, keyword);
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("用户不存在, ID: " + userId);
        }
        
        return taskRepository.searchTasksByKeyword(userId, keyword).stream()
                .map(TaskDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getTasksByTagId(Long tagId) {
        log.debug("获取标签 ID: {} 的所有任务", tagId);
        if (!tagRepository.existsById(tagId)) {
            throw new ResourceNotFoundException("标签不存在, ID: " + tagId);
        }
        
        return taskRepository.findByTagId(tagId).stream()
                .map(TaskDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        log.info("更新任务 ID: {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("任务不存在, ID: " + id));
        
        User user = null;
        if (taskDTO.getUserId() != null) {
            user = userRepository.findById(taskDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("用户不存在, ID: " + taskDTO.getUserId()));
        }
        
        Category category = null;
        if (taskDTO.getCategoryId() != null) {
            category = categoryRepository.findById(taskDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("分类不存在, ID: " + taskDTO.getCategoryId()));
        }
        
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());
        task.setUser(user);
        task.setCategory(category);
        task.setPriority(taskDTO.getPriority());
        task.setDueDate(taskDTO.getDueDate());
        
        Task updatedTask = taskRepository.save(task);
        log.info("任务更新成功 ID: {}", updatedTask.getId());
        
        return TaskDTO.fromEntity(updatedTask);
    }

    @Override
    @Transactional
    public TaskDTO updateTaskStatus(Long id, Task.TaskStatus status) {
        log.info("更新任务 ID: {} 的状态为 {}", id, status);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("任务不存在, ID: " + id));
        
        task.setStatus(status);
        if (status == Task.TaskStatus.COMPLETED) {
            task.setCompletedAt(LocalDateTime.now());
        } else {
            task.setCompletedAt(null);
        }
        
        Task updatedTask = taskRepository.save(task);
        log.info("任务状态更新成功 ID: {}", updatedTask.getId());
        
        return TaskDTO.fromEntity(updatedTask);
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        log.info("删除任务 ID: {}", id);
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("任务不存在, ID: " + id);
        }
        
        taskRepository.deleteById(id);
        log.info("任务删除成功 ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countTasksByUserAndStatus(Long userId, Task.TaskStatus status) {
        log.debug("统计用户 ID: {} 状态为 {} 的任务数量", userId, status);
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("用户不存在, ID: " + userId);
        }
        
        return taskRepository.countTasksByUserAndStatus(userId, status);
    }

    @Override
    @Transactional
    public void addTagToTask(Long taskId, Long tagId) {
        log.info("为任务 ID: {} 添加标签 ID: {}", taskId, tagId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("任务不存在, ID: " + taskId));
                
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("标签不存在, ID: " + tagId));
                
        task.getTags().add(tag);
        taskRepository.save(task);
        log.info("标签添加成功");
    }

    @Override
    @Transactional
    public void removeTagFromTask(Long taskId, Long tagId) {
        log.info("从任务 ID: {} 移除标签 ID: {}", taskId, tagId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("任务不存在, ID: " + taskId));
                
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("标签不存在, ID: " + tagId));
                
        task.getTags().remove(tag);
        taskRepository.save(task);
        log.info("标签移除成功");
    }
} 