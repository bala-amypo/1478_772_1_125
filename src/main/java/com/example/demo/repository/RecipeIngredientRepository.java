package com.example.demo.repository;

import com.example.demo.entity.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
    List<RecipeIngredient> findByMenuItemId(Long menuItemId);
    List<RecipeIngredient> findByIngredientId(Long ingredientId);
    List<RecipeIngredient> findByMenuItemIdAndIngredientId(Long menuItemId, Long ingredientId);
    void deleteByMenuItemId(Long menuItemId);
    void deleteByIngredientId(Long ingredientId);
}