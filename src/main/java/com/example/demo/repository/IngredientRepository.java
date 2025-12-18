package com.example.demo.repository;

import com.example.demo.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    
    Optional<Ingredient> findByName(String name);
    
    boolean existsByName(String name);
    
    // Query method as specified in requirements
    @Query("SELECT COUNT(ri) FROM RecipeIngredient ri WHERE ri.ingredient.id = :ingredientId")
    Long getTotalUsageByIngredient(Long ingredientId);
}