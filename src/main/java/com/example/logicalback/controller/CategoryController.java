package com.example.logicalback.controller;

import com.example.logicalback.dto.CategoryDTO;
import com.example.logicalback.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    
    private final CategoryService categoryService;
    
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.findAllCategories());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findCategoryById(id));
    }
    
    @GetMapping("/most-tasks")
    public ResponseEntity<List<CategoryDTO>> getCategoriesWithMostTasks() {
        return ResponseEntity.ok(categoryService.findCategoriesWithMostTasks());
    }
} 