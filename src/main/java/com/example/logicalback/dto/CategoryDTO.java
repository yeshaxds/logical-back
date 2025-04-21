package com.example.logicalback.dto;

import com.example.logicalback.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long id;
    
    @NotBlank(message = "分类名称不能为空")
    private String name;
    
    private String description;
    private String color;
    private Long taskCount;
    
    public static CategoryDTO fromEntity(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .color(category.getColor())
                .taskCount((long) category.getTasks().size())
                .build();
    }
} 