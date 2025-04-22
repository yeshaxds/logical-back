package com.example.logicalback.config;

import com.example.logicalback.entity.Category;
import com.example.logicalback.entity.Role;
import com.example.logicalback.entity.Tag;
import com.example.logicalback.entity.Task;
import com.example.logicalback.entity.TaskStatus;
import com.example.logicalback.entity.User;
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
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@Slf4j
@Profile("!prod")
public class DataInitializer {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TaskRepository taskRepository;
    private final TagRepository tagRepository;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // 检查数据库是否已经初始化过
            if (userRepository.count() > 0) {
                log.info("数据库中已存在数据，跳过初始化");
                return;
            }
            
            log.info("初始化示例数据...");
            
            // 创建用户
            log.info("创建用户...");
            User admin = createUser("admin", "admin@example.com", "管理员", Role.ADMIN);
            User user1 = createUser("user1", "user1@example.com", "用户一", Role.USER);
            User user2 = createUser("user2", "user2@example.com", "用户二", Role.USER);
            
            // 创建分类
            log.info("创建分类...");
            Category work = createCategory("工作", "工作相关任务", "#FF5733");
            Category study = createCategory("学习", "学习相关任务", "#33A8FF");
            Category life = createCategory("生活", "生活相关任务", "#33FF57");
            
            // 创建标签
            log.info("创建标签...");
            Tag urgent = createTag("紧急", "#FF0000");
            Tag important = createTag("重要", "#FF9900");
            Tag easy = createTag("简单", "#00FF00");
            Tag difficult = createTag("困难", "#0000FF");
            
            // 创建任务
            log.info("创建任务...");
            Task task1 = createTask(
                    "完成项目报告",
                    "需要整理上周的项目进展，并提交详细报告。",
                    admin,
                    work,
                    new HashSet<>(Arrays.asList(urgent, important)),
                    TaskStatus.PENDING,
                    1,
                    LocalDateTime.now().plusDays(2)
            );
            
            Task task2 = createTask(
                    "学习Spring框架",
                    "学习Spring Boot和Spring Security的基础知识。",
                    user1,
                    study,
                    new HashSet<>(Arrays.asList(important)),
                    TaskStatus.IN_PROGRESS,
                    2,
                    LocalDateTime.now().plusDays(5)
            );
            
            Task task3 = createTask(
                    "购买日用品",
                    "需要购买洗发水、沐浴露和牙膏。",
                    user2,
                    life,
                    new HashSet<>(Arrays.asList(easy)),
                    TaskStatus.PENDING,
                    3,
                    LocalDateTime.now().plusDays(1)
            );
            
            Task task4 = createTask(
                    "健身计划",
                    "制定每周健身计划，包括跑步和力量训练。",
                    user1,
                    life,
                    new HashSet<>(Arrays.asList(important, difficult)),
                    TaskStatus.PENDING,
                    2,
                    LocalDateTime.now().plusDays(3)
            );
            
            Task task5 = createTask(
                    "准备会议",
                    "为周一的项目会议准备演示文稿和材料。",
                    admin,
                    work,
                    new HashSet<>(Arrays.asList(urgent, difficult)),
                    TaskStatus.IN_PROGRESS,
                    1,
                    LocalDateTime.now().plusDays(1)
            );
            
            log.info("示例数据初始化完成！");
        };
    }
    
    private User createUser(String username, String email, String fullName, Role role) {
        // 检查用户是否已存在
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            log.info("用户已存在: {}", username);
            return existingUser.get();
        }
        
        User user = User.builder()
                .username(username)
                .email(email)
                .password("$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG") // password
                .fullName(fullName)
                .role(role)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        log.info("创建新用户: {}", username);
        return userRepository.save(user);
    }
    
    private Category createCategory(String name, String description, String color) {
        // 检查分类是否已存在
        Optional<Category> existingCategory = categoryRepository.findByName(name);
        if (existingCategory.isPresent()) {
            log.info("分类已存在: {}", name);
            return existingCategory.get();
        }
        
        Category category = Category.builder()
                .name(name)
                .description(description)
                .color(color)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        log.info("创建新分类: {}", name);
        return categoryRepository.save(category);
    }
    
    private Tag createTag(String name, String color) {
        // 检查标签是否已存在
        Optional<Tag> existingTag = tagRepository.findByName(name);
        if (existingTag.isPresent()) {
            log.info("标签已存在: {}", name);
            return existingTag.get();
        }
        
        Tag tag = Tag.builder()
                .name(name)
                .color(color)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        log.info("创建新标签: {}", name);
        return tagRepository.save(tag);
    }
    
    private Task createTask(String title, String description, User user, Category category, 
                          HashSet<Tag> tags, TaskStatus status, Integer priority, LocalDateTime dueDate) {
        Task task = Task.builder()
                .title(title)
                .description(description)
                .user(user)
                .category(category)
                .status(status)
                .priority(priority)
                .dueDate(dueDate)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        task.setTags(tags);
        log.info("创建新任务: {}", title);
        return taskRepository.save(task);
    }
} 