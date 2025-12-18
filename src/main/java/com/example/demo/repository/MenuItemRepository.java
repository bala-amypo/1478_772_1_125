package com.example.demo.repository;

import com.example.demo.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    Optional<MenuItem> findByName(String name);
    boolean existsByName(String name);
    List<MenuItem> findByCategory(String category);
    List<MenuItem> findByPriceLessThanEqual(Double maxPrice);
}