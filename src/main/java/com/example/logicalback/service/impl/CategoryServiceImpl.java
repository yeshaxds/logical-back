package com.example.logicalback.service.impl;

import com.example.logicalback.dto.CategoryDTO;
import com.example.logicalback.entity.Category;
import com.example.logicalback.exception.ResourceNotFoundException;
import com.example.logicalback.repository.CategoryRepository;
import com.example.logicalback.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> findAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertToCategoryDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO findCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        
        return convertToCategoryDTO(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> findCategoriesWithMostTasks() {
        return categoryRepository.findCategoriesWithTaskCount().stream()
                .map(result -> {
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
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        if (existsByName(categoryDTO.getName())) {
            throw new IllegalArgumentException("Category name already exists: " + categoryDTO.getName());
        }
        
        Category category = Category.builder()
                .name(categoryDTO.getName())
                .description(categoryDTO.getDescription())
                .color(categoryDTO.getColor())
                .build();
        
        Category savedCategory = categoryRepository.save(category);
        
        return convertToCategoryDTO(savedCategory);
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        
        // 检查名称是否已存在（如果更改了名称）
        if (!category.getName().equals(categoryDTO.getName()) && existsByName(categoryDTO.getName())) {
            throw new IllegalArgumentException("Category name already exists: " + categoryDTO.getName());
        }
        
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setColor(categoryDTO.getColor());
        category.setUpdatedAt(LocalDateTime.now());
        
        Category updatedCategory = categoryRepository.save(category);
        
        return convertToCategoryDTO(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        
        categoryRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }
    
    private CategoryDTO convertToCategoryDTO(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .color(category.getColor())
                .taskCount(category.getTasks().size())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
} 