package com.example.logicalback.config;

import com.example.logicalback.model.Category;
import com.example.logicalback.model.Tag;
import com.example.logicalback.model.Task;
import com.example.logicalback.model.User;
import com.example.logicalback.repository.CategoryRepository;
import com.example.logicalback.repository.TagRepository;
import com.example.logicalback.repository.TaskRepository;
import com.example.logicalback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final TaskRepository taskRepository;

    @Bean
    @Profile("!prod")
    public CommandLineRunner initDatabase() {
        return args -> {
            log.info("开始初始化示例数据...");
            
            // 1. 创建用户
            User admin = User.builder()
                    .username("admin")
                    .password("$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG") // password
                    .email("admin@example.com")
                    .fullName("系统管理员")
                    .role(User.UserRole.ADMIN)
                    .active(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            User user1 = User.builder()
                    .username("user1")
                    .password("$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG") // password
                    .email("user1@example.com")
                    .fullName("测试用户1")
                    .role(User.UserRole.USER)
                    .active(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            User user2 = User.builder()
                    .username("user2")
                    .password("$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG") // password
                    .email("user2@example.com")
                    .fullName("测试用户2")
                    .role(User.UserRole.USER)
                    .active(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            userRepository.saveAll(Arrays.asList(admin, user1, user2));
            log.info("用户数据初始化完成");
            
            // 2. 创建分类
            Category workCategory = Category.builder()
                    .name("工作")
                    .description("工作相关任务")
                    .color("#FF5733")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            Category studyCategory = Category.builder()
                    .name("学习")
                    .description("学习相关任务")
                    .color("#3498DB")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            Category lifeCategory = Category.builder()
                    .name("生活")
                    .description("生活相关任务")
                    .color("#2ECC71")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            categoryRepository.saveAll(Arrays.asList(workCategory, studyCategory, lifeCategory));
            log.info("分类数据初始化完成");
            
            // 3. 创建标签
            Tag urgentTag = Tag.builder()
                    .name("紧急")
                    .color("#E74C3C")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            Tag importantTag = Tag.builder()
                    .name("重要")
                    .color("#F39C12")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            Tag easyTag = Tag.builder()
                    .name("简单")
                    .color("#3498DB")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            Tag difficultTag = Tag.builder()
                    .name("困难")
                    .color("#9B59B6")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            tagRepository.saveAll(Arrays.asList(urgentTag, importantTag, easyTag, difficultTag));
            log.info("标签数据初始化完成");
            
            // 4. 创建任务
            Task task1 = Task.builder()
                    .title("完成项目报告")
                    .description("编写本季度项目进展报告，包括完成情况、问题和下一步计划")
                    .status(Task.TaskStatus.PENDING)
                    .user(user1)
                    .category(workCategory)
                    .priority(1)
                    .dueDate(LocalDateTime.now().plusDays(3))
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .tags(new HashSet<>(Arrays.asList(urgentTag, importantTag)))
                    .build();
            
            Task task2 = Task.builder()
                    .title("学习Spring Boot")
                    .description("学习Spring Boot框架，完成在线课程的第1-5章节内容")
                    .status(Task.TaskStatus.IN_PROGRESS)
                    .user(user1)
                    .category(studyCategory)
                    .priority(2)
                    .dueDate(LocalDateTime.now().plusDays(7))
                    .createdAt(LocalDateTime.now().minusDays(2))
                    .updatedAt(LocalDateTime.now())
                    .tags(new HashSet<>(Arrays.asList(importantTag)))
                    .build();
            
            Task task3 = Task.builder()
                    .title("购买生日礼物")
                    .description("为朋友下周的生日聚会购买礼物")
                    .status(Task.TaskStatus.PENDING)
                    .user(user1)
                    .category(lifeCategory)
                    .priority(3)
                    .dueDate(LocalDateTime.now().plusDays(5))
                    .createdAt(LocalDateTime.now().minusDays(1))
                    .updatedAt(LocalDateTime.now())
                    .tags(new HashSet<>(Arrays.asList(easyTag)))
                    .build();
            
            Task task4 = Task.builder()
                    .title("准备技术演讲")
                    .description("为下周的团队会议准备技术分享演讲，主题是微服务架构")
                    .status(Task.TaskStatus.PENDING)
                    .user(user2)
                    .category(workCategory)
                    .priority(1)
                    .dueDate(LocalDateTime.now().plusDays(6))
                    .createdAt(LocalDateTime.now().minusDays(3))
                    .updatedAt(LocalDateTime.now())
                    .tags(new HashSet<>(Arrays.asList(importantTag, difficultTag)))
                    .build();
            
            Task task5 = Task.builder()
                    .title("学习React框架")
                    .description("学习React框架基础知识，完成一个简单的TodoList应用")
                    .status(Task.TaskStatus.IN_PROGRESS)
                    .user(user2)
                    .category(studyCategory)
                    .priority(2)
                    .dueDate(LocalDateTime.now().plusDays(10))
                    .createdAt(LocalDateTime.now().minusDays(5))
                    .updatedAt(LocalDateTime.now())
                    .tags(new HashSet<>(Arrays.asList(difficultTag)))
                    .build();
            
            taskRepository.saveAll(Arrays.asList(task1, task2, task3, task4, task5));
            log.info("任务数据初始化完成");
            
            log.info("示例数据初始化完成！");
        };
    }
} 