package com.example.demo.service;

import com.example.demo.entity.Ingredient;
import java.util.List;

public interface IngredientService {
    List<Ingredient> getAllIngredients();
    Ingredient getIngredientById(Long id);
    Ingredient createIngredient(Ingredient ingredient);
    Ingredient updateIngredient(Long id, Ingredient ingredient);
    void deleteIngredient(Long id);
}