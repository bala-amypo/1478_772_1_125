package com.example.demo.service;

import com.example.demo.entity.RecipeIngredient;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.RecipeIngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RecipeIngredientServiceImpl implements RecipeIngredientService {

    @Autowired
    private RecipeIngredientRepository recipeIngredientRepository;

    @Override
    public List<RecipeIngredient> getAllRecipeIngredients() {
        return recipeIngredientRepository.findAll();
    }

    @Override
    public RecipeIngredient getRecipeIngredientById(Long id) {
        return recipeIngredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RecipeIngredient", "id", id));
    }

    @Override
    public List<RecipeIngredient> getRecipeIngredientsByMenuItem(Long menuItemId) {
        return recipeIngredientRepository.findByMenuItemId(menuItemId);
    }

    @Override
    public List<RecipeIngredient> getRecipeIngredientsByIngredient(Long ingredientId) {
        return recipeIngredientRepository.findByIngredientId(ingredientId);
    }

    @Override
    public RecipeIngredient createRecipeIngredient(RecipeIngredient recipeIngredient) {
        return recipeIngredientRepository.save(recipeIngredient);
    }

    @Override
    public RecipeIngredient updateRecipeIngredient(Long id, RecipeIngredient recipeIngredientDetails) {
        RecipeIngredient recipeIngredient = getRecipeIngredientById(id);
        recipeIngredient.setQuantityRequired(recipeIngredientDetails.getQuantityRequired());
        return recipeIngredientRepository.save(recipeIngredient);
    }

    @Override
    public void deleteRecipeIngredient(Long id) {
        RecipeIngredient recipeIngredient = getRecipeIngredientById(id);
        recipeIngredientRepository.delete(recipeIngredient);
    }

    @Override
    public void deleteByMenuItemId(Long menuItemId) {
        recipeIngredientRepository.deleteByMenuItemId(menuItemId);
    }

    @Override
    public void deleteByIngredientId(Long ingredientId) {
        recipeIngredientRepository.deleteByIngredientId(ingredientId);
    }
}