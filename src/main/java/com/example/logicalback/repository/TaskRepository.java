package com.example.logicalback.repository;

import com.example.logicalback.entity.Task;
import com.example.logicalback.entity.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    List<Task> findByUserId(Long userId);
    
    Page<Task> findByUserId(Long userId, Pageable pageable);
    
    List<Task> findByCategoryId(Long categoryId);
    
    List<Task> findByStatus(TaskStatus status);
    
    @Query("SELECT t FROM Task t WHERE t.dueDate BETWEEN :start AND :end")
    List<Task> findTasksDueBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    @Query("SELECT t FROM Task t JOIN t.user u WHERE u.id = :userId AND (t.title LIKE %:keyword% OR t.description LIKE %:keyword%)")
    List<Task> searchTasksByKeyword(@Param("userId") Long userId, @Param("keyword") String keyword);
    
    @Query("SELECT t FROM Task t JOIN t.tags tag WHERE tag.id = :tagId")
    List<Task> findByTagId(@Param("tagId") Long tagId);
    
    @Query("SELECT COUNT(t) FROM Task t WHERE t.user.id = :userId AND t.status = :status")
    Long countTasksByUserAndStatus(@Param("userId") Long userId, @Param("status") TaskStatus status);
    
    @Query("SELECT t.status as status, COUNT(t) as count FROM Task t GROUP BY t.status")
    List<Map<String, Object>> getTaskStatusDistribution();
} 