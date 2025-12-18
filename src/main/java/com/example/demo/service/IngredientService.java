package com.example.demo.service;

import com.example.demo.entity.Ingredient;
import java.util.List;

public interface IngredientService {
    
    Ingredient createIngredient(Ingredient ingredient);
    
    Ingredient updateIngredient(Long id, Ingredient ingredient);
    
    Ingredient getIngredientById(Long id);
    
    List<Ingredient> getAllIngredients();
    
    Ingredient toggleIngredientStatus(Long id);
    
    Long getTotalUsageByIngredient(Long ingredientId);
}