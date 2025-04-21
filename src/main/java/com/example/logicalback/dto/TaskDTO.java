package com.example.logicalback.dto;

import com.example.logicalback.model.Task;
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
    private Task.TaskStatus status;
    
    private Long userId;
    private String username;
    
    private Long categoryId;
    private String categoryName;
    
    private Integer priority;
    private LocalDateTime dueDate;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    private List<TagDTO> tags;
    
    public static TaskDTO fromEntity(Task task) {
        return TaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .userId(task.getUser() != null ? task.getUser().getId() : null)
                .username(task.getUser() != null ? task.getUser().getUsername() : null)
                .categoryId(task.getCategory() != null ? task.getCategory().getId() : null)
                .categoryName(task.getCategory() != null ? task.getCategory().getName() : null)
                .priority(task.getPriority())
                .dueDate(task.getDueDate())
                .completedAt(task.getCompletedAt())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .tags(task.getTags().stream().map(TagDTO::fromEntity).collect(Collectors.toList()))
                .build();
    }
} 