package com.example.demo.repository;

import com.example.demo.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    Optional<MenuItem> findByName(String name);
    boolean existsByName(String name);
    List<MenuItem> findByIsAvailable(Boolean isAvailable);
    List<MenuItem> findByNameContainingIgnoreCase(String name);
    
    // Query to find menu items by category name
    @Query("SELECT m FROM MenuItem m JOIN m.categories c WHERE c.name = :categoryName")
    List<MenuItem> findByCategoriesName(@Param("categoryName") String categoryName);
}