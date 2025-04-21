package com.example.logicalback.service;

import com.example.logicalback.dto.CategoryDTO;
import com.example.logicalback.dto.TagDTO;
import com.example.logicalback.entity.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface StatisticsService {
    
    Map<String, Long> getTaskStatusDistribution();
    
    Map<TaskStatus, Long> getUserTaskStatusDistribution(Long userId);
    
    List<CategoryDTO> getCategoriesWithMostTasks();
    
    List<TagDTO> getMostUsedTags();
    
    Map<String, Long> getTasksCompletedByDay(LocalDateTime startDate, LocalDateTime endDate);
    
    Map<String, Double> getUserCompletionRate(Long userId);
    
    Long getOverdueTasks();
    
    Long getUserOverdueTasks(Long userId);
    
    Map<Integer, Long> getTaskPriorityDistribution();
    
    Map<Integer, Long> getUserTaskPriorityDistribution(Long userId);
} 