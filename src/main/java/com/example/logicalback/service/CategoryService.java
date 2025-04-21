package com.example.logicalback.service;

import com.example.logicalback.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    
    List<CategoryDTO> findAllCategories();
    
    CategoryDTO findCategoryById(Long id);
    
    List<CategoryDTO> findCategoriesWithMostTasks();
    
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    
    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);
    
    void deleteCategory(Long id);
    
    boolean existsByName(String name);
} 