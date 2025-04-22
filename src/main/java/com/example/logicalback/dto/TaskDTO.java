package com.example.logicalback.dto;

import com.example.logicalback.entity.Task;
import com.example.logicalback.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private Long id;
    
    @NotBlank(message = "任务标题不能为空")
    private String title;
    
    private String description;
    
    @NotNull(message = "任务状态不能为空")
    private TaskStatus status;
    
    private Integer priority;
    private LocalDateTime dueDate;
    
    private Long categoryId;
    private String categoryName;
    
    private Long userId;
    private String username;
    
    private List<TagDTO> tags;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static TaskDTO fromEntity(Task task) {
        return TaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .dueDate(task.getDueDate())
                .categoryId(task.getCategory() != null ? task.getCategory().getId() : null)
                .categoryName(task.getCategory() != null ? task.getCategory().getName() : null)
                .userId(task.getUser() != null ? task.getUser().getId() : null)
                .username(task.getUser() != null ? task.getUser().getUsername() : null)
                .tags(task.getTags().stream().map(tag -> TagDTO.builder()
                        .id(tag.getId())
                        .name(tag.getName())
                        .color(tag.getColor())
                        .build()).collect(Collectors.toList()))
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
} 