package com.example.logicalback.dto;

import com.example.logicalback.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO {
    private Long id;
    
    @NotBlank(message = "标签名称不能为空")
    private String name;
    
    private String color;
    private Integer taskCount;
    
    public static TagDTO fromEntity(Tag tag) {
        return TagDTO.builder()
                .id(tag.getId())
                .name(tag.getName())
                .color(tag.getColor())
                .taskCount(tag.getTasks().size())
                .build();
    }
} 