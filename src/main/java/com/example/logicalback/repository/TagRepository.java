package com.example.logicalback.repository;

import com.example.logicalback.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    
    Optional<Tag> findByName(String name);
    
    boolean existsByName(String name);
    
    @Query("SELECT t FROM Tag t JOIN t.tasks task WHERE task.id = :taskId")
    List<Tag> findTagsByTaskId(Long taskId);
    
    @Query("SELECT t FROM Tag t JOIN t.tasks task GROUP BY t.id ORDER BY COUNT(task) DESC")
    List<Tag> findMostUsedTags();
} 