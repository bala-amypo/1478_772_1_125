package com.example.demo.repository;

import com.example.demo.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    // Add this method for checking name existence
    boolean existsByName(String name);
    
    // Optional: Find by name
    Optional<Ingredient> findByName(String name);
}