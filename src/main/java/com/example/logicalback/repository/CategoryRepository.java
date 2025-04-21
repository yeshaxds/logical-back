package com.example.logicalback.repository;

import com.example.logicalback.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    Optional<Category> findByName(String name);
    
    boolean existsByName(String name);
    
    @Query("SELECT c FROM Category c LEFT JOIN c.tasks t GROUP BY c.id ORDER BY COUNT(t) DESC")
    List<Category> findAllOrderByTaskCountDesc();
    
    @Query("SELECT c, COUNT(t) AS taskCount FROM Category c LEFT JOIN c.tasks t GROUP BY c.id ORDER BY COUNT(t) DESC")
    List<Object[]> findCategoriesWithTaskCount();
} 