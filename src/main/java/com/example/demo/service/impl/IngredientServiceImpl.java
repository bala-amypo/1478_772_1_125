package com.example.demo.service.impl;

import com.example.demo.entity.Ingredient;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.IngredientRepository;
import com.example.demo.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {
    
    private final IngredientRepository ingredientRepository;
    
    @Override
    @Transactional
    public Ingredient createIngredient(Ingredient ingredient) {
        // Check for duplicate name
        ingredientRepository.findByNameIgnoreCase(ingredient.getName())
            .ifPresent(existing -> {
                throw new BadRequestException("Ingredient with name '" + ingredient.getName() + "' already exists");
            });
        
        // Validate cost per unit
        if (ingredient.getCostPerUnit() == null || ingredient.getCostPerUnit().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Cost per unit must be greater than 0");
        }
        
        ingredient.setActive(true);
        return ingredientRepository.save(ingredient);
    }
    
    @Override
    @Transactional
    public Ingredient updateIngredient(Long id, Ingredient updated) {
        Ingredient existing = ingredientRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Ingredient not found with id: " + id));
        
        // Check for duplicate name if name is being changed
        if (!existing.getName().equalsIgnoreCase(updated.getName())) {
            ingredientRepository.findByNameIgnoreCase(updated.getName())
                .ifPresent(duplicate -> {
                    throw new BadRequestException("Ingredient with name '" + updated.getName() + "' already exists");
                });
        }
        
        // Validate cost per unit
        if (updated.getCostPerUnit() == null || updated.getCostPerUnit().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Cost per unit must be greater than 0");
        }
        
        existing.setName(updated.getName());
        existing.setUnit(updated.getUnit());
        existing.setCostPerUnit(updated.getCostPerUnit());
        
        return ingredientRepository.save(existing);
    }
    
    @Override
    public Ingredient getIngredientById(Long id) {
        return ingredientRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Ingredient not found with id: " + id));
    }
    
    @Override
    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }
    
    @Override
    @Transactional
    public void deactivateIngredient(Long id) {
        Ingredient ingredient = getIngredientById(id);
        ingredient.setActive(false);
        ingredientRepository.save(ingredient);
    }
}