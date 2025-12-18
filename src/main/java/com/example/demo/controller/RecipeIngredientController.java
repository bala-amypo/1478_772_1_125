package com.example.demo.controller;

import com.example.demo.entity.RecipeIngredient;
import com.example.demo.service.RecipeIngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/recipe-ingredients")
public class RecipeIngredientController {

    @Autowired
    private RecipeIngredientService recipeIngredientService;

    @GetMapping
    public ResponseEntity<List<RecipeIngredient>> getAllRecipeIngredients() {
        List<RecipeIngredient> recipeIngredients = recipeIngredientService.getAllRecipeIngredients();
        return ResponseEntity.ok(recipeIngredients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeIngredient> getRecipeIngredientById(@PathVariable Long id) {
        RecipeIngredient recipeIngredient = recipeIngredientService.getRecipeIngredientById(id);
        return ResponseEntity.ok(recipeIngredient);
    }

    @GetMapping("/menu-item/{menuItemId}")
    public ResponseEntity<List<RecipeIngredient>> getRecipeIngredientsByMenuItem(@PathVariable Long menuItemId) {
        List<RecipeIngredient> recipeIngredients = recipeIngredientService.getRecipeIngredientsByMenuItem(menuItemId);
        return ResponseEntity.ok(recipeIngredients);
    }

    @GetMapping("/ingredient/{ingredientId}")
    public ResponseEntity<List<RecipeIngredient>> getRecipeIngredientsByIngredient(@PathVariable Long ingredientId) {
        List<RecipeIngredient> recipeIngredients = recipeIngredientService.getRecipeIngredientsByIngredient(ingredientId);
        return ResponseEntity.ok(recipeIngredients);
    }

    @PostMapping
    public ResponseEntity<RecipeIngredient> createRecipeIngredient(@Valid @RequestBody RecipeIngredient recipeIngredient) {
        RecipeIngredient createdRecipeIngredient = recipeIngredientService.createRecipeIngredient(recipeIngredient);
        return new ResponseEntity<>(createdRecipeIngredient, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipeIngredient> updateRecipeIngredient(@PathVariable Long id, 
                                                                   @Valid @RequestBody RecipeIngredient recipeIngredient) {
        RecipeIngredient updatedRecipeIngredient = recipeIngredientService.updateRecipeIngredient(id, recipeIngredient);
        return ResponseEntity.ok(updatedRecipeIngredient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipeIngredient(@PathVariable Long id) {
        recipeIngredientService.deleteRecipeIngredient(id);
        return ResponseEntity.noContent().build();
    }
}