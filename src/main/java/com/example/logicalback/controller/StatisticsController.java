package com.example.logicalback.controller;

import com.example.logicalback.dto.CategoryDTO;
import com.example.logicalback.dto.TagDTO;
import com.example.logicalback.entity.TaskStatus;
import com.example.logicalback.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
@Slf4j
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/task-status")
    public ResponseEntity<Map<String, Long>> getTaskStatusDistribution() {
        log.info("REST请求 - 获取所有任务状态分布");
        return ResponseEntity.ok(statisticsService.getTaskStatusDistribution());
    }

    @GetMapping("/task-status/user/{userId}")
    public ResponseEntity<Map<TaskStatus, Long>> getUserTaskStatusDistribution(@PathVariable Long userId) {
        log.info("REST请求 - 获取用户 ID: {} 的任务状态分布", userId);
        return ResponseEntity.ok(statisticsService.getUserTaskStatusDistribution(userId));
    }

    @GetMapping("/categories/most-tasks")
    public ResponseEntity<List<CategoryDTO>> getCategoriesWithMostTasks() {
        log.info("REST请求 - 获取任务数量最多的分类");
        return ResponseEntity.ok(statisticsService.getCategoriesWithMostTasks());
    }

    @GetMapping("/tags/most-used")
    public ResponseEntity<List<TagDTO>> getMostUsedTags() {
        log.info("REST请求 - 获取使用最多的标签");
        return ResponseEntity.ok(statisticsService.getMostUsedTags());
    }

    @GetMapping("/tasks-completed-by-day")
    public ResponseEntity<Map<String, Long>> getTasksCompletedByDay(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        log.info("REST请求 - 获取从 {} 到 {} 之间每天完成的任务数量", startDate, endDate);
        return ResponseEntity.ok(statisticsService.getTasksCompletedByDay(startDate, endDate));
    }

    @GetMapping("/user/{userId}/completion-rate")
    public ResponseEntity<Map<String, Double>> getUserCompletionRate(@PathVariable Long userId) {
        log.info("REST请求 - 获取用户 ID: {} 的任务完成率", userId);
        return ResponseEntity.ok(statisticsService.getUserCompletionRate(userId));
    }

    @GetMapping("/overdue-tasks")
    public ResponseEntity<Long> getOverdueTasks() {
        log.info("REST请求 - 获取所有逾期任务数量");
        return ResponseEntity.ok(statisticsService.getOverdueTasks());
    }

    @GetMapping("/user/{userId}/overdue-tasks")
    public ResponseEntity<Long> getUserOverdueTasks(@PathVariable Long userId) {
        log.info("REST请求 - 获取用户 ID: {} 的逾期任务数量", userId);
        return ResponseEntity.ok(statisticsService.getUserOverdueTasks(userId));
    }

    @GetMapping("/task-priority")
    public ResponseEntity<Map<Integer, Long>> getTaskPriorityDistribution() {
        log.info("REST请求 - 获取所有任务优先级分布");
        return ResponseEntity.ok(statisticsService.getTaskPriorityDistribution());
    }

    @GetMapping("/task-priority/user/{userId}")
    public ResponseEntity<Map<Integer, Long>> getUserTaskPriorityDistribution(@PathVariable Long userId) {
        log.info("REST请求 - 获取用户 ID: {} 的任务优先级分布", userId);
        return ResponseEntity.ok(statisticsService.getUserTaskPriorityDistribution(userId));
    }
} 