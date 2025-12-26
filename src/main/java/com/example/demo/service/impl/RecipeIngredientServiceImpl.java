package com.example.demo.service.impl;

import com.example.demo.entity.Ingredient;
import com.example.demo.entity.MenuItem;
import com.example.demo.entity.RecipeIngredient;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.IngredientRepository;
import com.example.demo.repository.MenuItemRepository;
import com.example.demo.repository.RecipeIngredientRepository;
import com.example.demo.service.RecipeIngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeIngredientServiceImpl implements RecipeIngredientService {
    
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final IngredientRepository ingredientRepository;
    private final MenuItemRepository menuItemRepository;
    
    @Override
    @Transactional
    public RecipeIngredient addIngredientToMenuItem(RecipeIngredient recipeIngredient) {
        // Validate quantity > 0
        if (recipeIngredient.getQuantityRequired() == null || recipeIngredient.getQuantityRequired() <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }
        
        // Check if ingredient exists and is active
        Ingredient ingredient = ingredientRepository.findById(recipeIngredient.getIngredient().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Ingredient not found with id: " + recipeIngredient.getIngredient().getId()));
        
        if (!ingredient.isActive()) {
            throw new BadRequestException("Cannot use inactive ingredient");
        }
        
        // Check if menu item exists
        MenuItem menuItem = menuItemRepository.findById(recipeIngredient.getMenuItem().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + recipeIngredient.getMenuItem().getId()));
        
        recipeIngredient.setIngredient(ingredient);
        recipeIngredient.setMenuItem(menuItem);
        
        return recipeIngredientRepository.save(recipeIngredient);
    }
    
    @Override
    @Transactional
    public RecipeIngredient updateRecipeIngredient(Long id, Double quantity) {
        RecipeIngredient existing = recipeIngredientRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Recipe ingredient not found with id: " + id));
        
        if (quantity == null || quantity <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }
        
        existing.setQuantityRequired(quantity);
        return recipeIngredientRepository.save(existing);
    }
    
    @Override
    public List<RecipeIngredient> getIngredientsByMenuItem(Long menuItemId) {
        return recipeIngredientRepository.findByMenuItemId(menuItemId);
    }
    
    @Override
    @Transactional
    public void removeIngredientFromRecipe(Long id) {
        if (!recipeIngredientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recipe ingredient not found with id: " + id);
        }
        recipeIngredientRepository.deleteById(id);
    }
    
    @Override
    public Double getTotalQuantityOfIngredient(Long ingredientId) {
        Double total = recipeIngredientRepository.getTotalQuantityByIngredientId(ingredientId);
        return total != null ? total : 0.0;
    }
}