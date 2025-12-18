package com.example.demo.service;

import com.example.demo.entity.RecipeIngredient;
import java.util.List;

public interface RecipeIngredientService {
    List<RecipeIngredient> getAllRecipeIngredients();
    RecipeIngredient getRecipeIngredientById(Long id);
    List<RecipeIngredient> getRecipeIngredientsByMenuItem(Long menuItemId);
    List<RecipeIngredient> getRecipeIngredientsByIngredient(Long ingredientId);
    RecipeIngredient createRecipeIngredient(RecipeIngredient recipeIngredient);
    RecipeIngredient updateRecipeIngredient(Long id, RecipeIngredient recipeIngredient);
    void deleteRecipeIngredient(Long id);
    void deleteByMenuItemId(Long menuItemId);
    void deleteByIngredientId(Long ingredientId);
}