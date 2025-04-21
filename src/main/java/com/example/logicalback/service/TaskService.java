package com.example.logicalback.service;

import com.example.logicalback.dto.TaskDTO;
import com.example.logicalback.entity.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskService {
    
    TaskDTO createTask(TaskDTO taskDTO);
    
    TaskDTO getTaskById(Long id);
    
    List<TaskDTO> getAllTasks();
    
    Page<TaskDTO> getTasksPaginated(Pageable pageable);
    
    List<TaskDTO> getTasksByUserId(Long userId);
    
    Page<TaskDTO> getTasksByUserIdPaginated(Long userId, Pageable pageable);
    
    List<TaskDTO> getTasksByCategoryId(Long categoryId);
    
    List<TaskDTO> getTasksByStatus(TaskStatus status);
    
    List<TaskDTO> getTasksDueBetween(LocalDateTime start, LocalDateTime end);
    
    List<TaskDTO> searchTasksByKeyword(Long userId, String keyword);
    
    List<TaskDTO> getTasksByTagId(Long tagId);
    
    TaskDTO updateTask(Long id, TaskDTO taskDTO);
    
    TaskDTO updateTaskStatus(Long id, TaskStatus status);
    
    void deleteTask(Long id);
    
    Long countTasksByUserAndStatus(Long userId, TaskStatus status);
    
    void addTagToTask(Long taskId, Long tagId);
    
    void removeTagFromTask(Long taskId, Long tagId);

    List<TaskDTO> findAllTasks();
    
    TaskDTO findTaskById(Long id);
    
    List<TaskDTO> findTasksByUserId(Long userId);
    
    List<TaskDTO> findTasksByCategoryId(Long categoryId);
    
    List<TaskDTO> findTasksByStatus(String status);
} 