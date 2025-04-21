package com.example.logicalback.repository;

import com.example.logicalback.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    List<Task> findByUserId(Long userId);
    
    Page<Task> findByUserId(Long userId, Pageable pageable);
    
    List<Task> findByCategoryId(Long categoryId);
    
    @Query("SELECT t FROM Task t WHERE t.status = :status")
    List<Task> findByStatus(Task.TaskStatus status);
    
    @Query("SELECT t FROM Task t WHERE t.dueDate BETWEEN :start AND :end")
    List<Task> findTasksDueBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT t FROM Task t WHERE t.user.id = :userId AND t.title LIKE %:keyword% OR t.description LIKE %:keyword%")
    List<Task> searchTasksByKeyword(Long userId, String keyword);
    
    @Query("SELECT t FROM Task t JOIN t.tags tag WHERE tag.id = :tagId")
    List<Task> findByTagId(Long tagId);
    
    @Query("SELECT COUNT(t) FROM Task t WHERE t.user.id = :userId AND t.status = :status")
    Long countTasksByUserAndStatus(Long userId, Task.TaskStatus status);
} 