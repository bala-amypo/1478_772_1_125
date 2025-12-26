package com.example.demo.repository;

import com.example.demo.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    Optional<MenuItem> findByNameIgnoreCase(String name);
    
    @Query("SELECT mi FROM MenuItem mi WHERE mi.active = true")
    List<MenuItem> findAllActiveWithCategories();
}