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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RecipeIngredientServiceImpl implements RecipeIngredientService {
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final IngredientRepository ingredientRepository;
    private final MenuItemRepository menuItemRepository;

    public RecipeIngredientServiceImpl(RecipeIngredientRepository recipeIngredientRepository,
                                      IngredientRepository ingredientRepository,
                                      MenuItemRepository menuItemRepository) {
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.ingredientRepository = ingredientRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public RecipeIngredient addIngredientToMenuItem(RecipeIngredient recipeIngredient) {
        if (recipeIngredient.getQuantityRequired() == null || recipeIngredient.getQuantityRequired() <= 0) {
            throw new BadRequestException("Quantity required must be greater than 0");
        }

        Ingredient ingredient = ingredientRepository.findById(recipeIngredient.getIngredient().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient not found with id: " + 
                        recipeIngredient.getIngredient().getId()));
        
        if (!ingredient.getActive()) {
            throw new BadRequestException("Cannot use inactive ingredient: " + ingredient.getName());
        }

        MenuItem menuItem = menuItemRepository.findById(recipeIngredient.getMenuItem().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + 
                        recipeIngredient.getMenuItem().getId()));

        recipeIngredient.setIngredient(ingredient);
        recipeIngredient.setMenuItem(menuItem);

        return recipeIngredientRepository.save(recipeIngredient);
    }

    @Override
    public RecipeIngredient updateRecipeIngredient(Long id, Double quantity) {
        if (quantity == null || quantity <= 0) {
            throw new BadRequestException("Quantity required must be greater than 0");
        }

        RecipeIngredient existing = recipeIngredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe ingredient not found with id: " + id));

        existing.setQuantityRequired(quantity);
        return recipeIngredientRepository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecipeIngredient> getIngredientsByMenuItem(Long menuItemId) {
        return recipeIngredientRepository.findByMenuItemId(menuItemId);
    }

    @Override
    public void removeIngredientFromRecipe(Long id) {
        if (!recipeIngredientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recipe ingredient not found with id: " + id);
        }
        recipeIngredientRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getTotalQuantityOfIngredient(Long ingredientId) {
        if (!ingredientRepository.existsById(ingredientId)) {
            throw new ResourceNotFoundException("Ingredient not found with id: " + ingredientId);
        }
        
        Double total = recipeIngredientRepository.getTotalQuantityByIngredientId(ingredientId);
        return total != null ? total : 0.0;
    }
}