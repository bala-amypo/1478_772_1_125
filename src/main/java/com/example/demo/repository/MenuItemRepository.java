package com.example.demo.repository;

import com.example.demo.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    
    // Find by name (exact match)
    Optional<MenuItem> findByName(String name);
    
    // Check if name exists
    boolean existsByName(String name);
    
    // Find by category
    List<MenuItem> findByCategory(String category);
    
    // Find by availability
    List<MenuItem> findByIsAvailable(Boolean isAvailable);
    
    // Find by price range
    List<MenuItem> findByPriceBetween(Double minPrice, Double maxPrice);
    
    // Find by price less than or equal
    List<MenuItem> findByPriceLessThanEqual(Double maxPrice);
    
    // Find by name containing (search)
    List<MenuItem> findByNameContainingIgnoreCase(String name);
    
    // Find by category and availability
    List<MenuItem> findByCategoryAndIsAvailable(String category, Boolean isAvailable);
}