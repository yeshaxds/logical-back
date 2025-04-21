package com.example.logicalback.service.impl;

import com.example.logicalback.dto.CategoryDTO;
import com.example.logicalback.dto.TagDTO;
import com.example.logicalback.entity.Category;
import com.example.logicalback.entity.Tag;
import com.example.logicalback.entity.TaskStatus;
import com.example.logicalback.exception.ResourceNotFoundException;
import com.example.logicalback.model.Task;
import com.example.logicalback.repository.CategoryRepository;
import com.example.logicalback.repository.TagRepository;
import com.example.logicalback.repository.TaskRepository;
import com.example.logicalback.repository.UserRepository;
import com.example.logicalback.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getTaskStatusDistribution() {
        List<Map<String, Object>> result = taskRepository.getTaskStatusDistribution();
        Map<String, Long> distribution = new HashMap<>();
        
        // 确保所有状态都有值
        for (TaskStatus status : TaskStatus.values()) {
            distribution.put(status.name(), 0L);
        }
        
        // 添加数据库返回的值
        for (Map<String, Object> item : result) {
            TaskStatus status = (TaskStatus) item.get("status");
            Long count = ((Number) item.get("count")).longValue();
            distribution.put(status.name(), count);
        }
        
        return distribution;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Task.TaskStatus, Long> getUserTaskStatusDistribution(Long userId) {
        log.debug("获取用户 ID: {} 的任务状态分布", userId);
        
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("用户不存在, ID: " + userId);
        }
        
        Map<Task.TaskStatus, Long> distribution = new HashMap<>();
        for (Task.TaskStatus status : Task.TaskStatus.values()) {
            distribution.put(status, 0L);
        }
        
        List<Task> tasks = taskRepository.findByUserId(userId);
        for (Task task : tasks) {
            distribution.put(task.getStatus(), distribution.get(task.getStatus()) + 1);
        }
        
        return distribution;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getCategoriesWithMostTasks() {
        return categoryRepository.findCategoriesWithTaskCount().stream()
                .map(this::convertToCategoryDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagDTO> getMostUsedTags() {
        return tagRepository.findMostUsedTags().stream()
                .map(this::convertToTagDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getTasksCompletedByDay(LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("获取从 {} 到 {} 之间每天完成的任务数量", startDate, endDate);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Map<String, Long> tasksPerDay = new HashMap<>();
        
        // 初始化每一天的计数为0
        for (LocalDateTime date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            tasksPerDay.put(date.format(formatter), 0L);
        }
        
        // 查询在指定日期范围内完成的任务
        List<Task> completedTasks = taskRepository.findAll().stream()
                .filter(task -> task.getStatus() == Task.TaskStatus.COMPLETED)
                .filter(task -> task.getCompletedAt() != null)
                .filter(task -> !task.getCompletedAt().isBefore(startDate) && !task.getCompletedAt().isAfter(endDate))
                .collect(Collectors.toList());
        
        // 统计每天完成的任务数量
        for (Task task : completedTasks) {
            String dayKey = task.getCompletedAt().format(formatter);
            tasksPerDay.put(dayKey, tasksPerDay.getOrDefault(dayKey, 0L) + 1);
        }
        
        return tasksPerDay;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Double> getUserCompletionRate(Long userId) {
        log.debug("获取用户 ID: {} 的任务完成率", userId);
        
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("用户不存在, ID: " + userId);
        }
        
        List<Task> userTasks = taskRepository.findByUserId(userId);
        
        if (userTasks.isEmpty()) {
            return Collections.singletonMap("completionRate", 0.0);
        }
        
        long totalTasks = userTasks.size();
        long completedTasks = userTasks.stream()
                .filter(task -> task.getStatus() == Task.TaskStatus.COMPLETED)
                .count();
        
        double completionRate = (double) completedTasks / totalTasks * 100;
        
        Map<String, Double> result = new HashMap<>();
        result.put("completionRate", completionRate);
        result.put("totalTasks", (double) totalTasks);
        result.put("completedTasks", (double) completedTasks);
        
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Long getOverdueTasks() {
        log.debug("获取所有逾期任务数量");
        
        LocalDateTime now = LocalDateTime.now();
        
        return taskRepository.findAll().stream()
                .filter(task -> task.getStatus() != Task.TaskStatus.COMPLETED 
                        && task.getStatus() != Task.TaskStatus.CANCELLED)
                .filter(task -> task.getDueDate() != null && task.getDueDate().isBefore(now))
                .count();
    }

    @Override
    @Transactional(readOnly = true)
    public Long getUserOverdueTasks(Long userId) {
        log.debug("获取用户 ID: {} 的逾期任务数量", userId);
        
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("用户不存在, ID: " + userId);
        }
        
        LocalDateTime now = LocalDateTime.now();
        
        return taskRepository.findByUserId(userId).stream()
                .filter(task -> task.getStatus() != Task.TaskStatus.COMPLETED 
                        && task.getStatus() != Task.TaskStatus.CANCELLED)
                .filter(task -> task.getDueDate() != null && task.getDueDate().isBefore(now))
                .count();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Integer, Long> getTaskPriorityDistribution() {
        log.debug("获取所有任务优先级分布");
        
        Map<Integer, Long> distribution = new HashMap<>();
        
        List<Task> tasks = taskRepository.findAll();
        for (Task task : tasks) {
            Integer priority = task.getPriority() != null ? task.getPriority() : 0;
            distribution.put(priority, distribution.getOrDefault(priority, 0L) + 1);
        }
        
        return distribution;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Integer, Long> getUserTaskPriorityDistribution(Long userId) {
        log.debug("获取用户 ID: {} 的任务优先级分布", userId);
        
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("用户不存在, ID: " + userId);
        }
        
        Map<Integer, Long> distribution = new HashMap<>();
        
        List<Task> tasks = taskRepository.findByUserId(userId);
        for (Task task : tasks) {
            Integer priority = task.getPriority() != null ? task.getPriority() : 0;
            distribution.put(priority, distribution.getOrDefault(priority, 0L) + 1);
        }
        
        return distribution;
    }

    private CategoryDTO convertToCategoryDTO(Object[] result) {
        Category category = (Category) result[0];
        Long taskCount = (Long) result[1];
        
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .color(category.getColor())
                .taskCount(taskCount.intValue())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
    
    private TagDTO convertToTagDTO(Object[] result) {
        Tag tag = (Tag) result[0];
        Long taskCount = (Long) result[1];
        
        return TagDTO.builder()
                .id(tag.getId())
                .name(tag.getName())
                .color(tag.getColor())
                .taskCount(taskCount.intValue())
                .build();
    }
} 