package com.example.demo.service;

import com.example.demo.entity.Ingredient;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.IngredientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class IngredientServiceImpl implements IngredientService {
    
    private final IngredientRepository ingredientRepository;

    // Constructor injection as required
    public IngredientServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public Ingredient createIngredient(Ingredient ingredient) {
        // Validate unique name
        if (ingredientRepository.existsByName(ingredient.getName())) {
            throw new BadRequestException("Ingredient with name '" + ingredient.getName() + "' already exists");
        }
        
        // Validate cost
        if (ingredient.getCostPerUnit() == null || ingredient.getCostPerUnit().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Cost per unit must be greater than 0");
        }
        
        // Ensure active defaults to true
        if (ingredient.getActive() == null) {
            ingredient.setActive(true);
        }
        
        return ingredientRepository.save(ingredient);
    }

    @Override
    public Ingredient updateIngredient(Long id, Ingredient ingredient) {
        Ingredient existingIngredient = ingredientRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Ingredient not found with id: " + id));
        
        // Check if name is being changed and if new name already exists
        if (!existingIngredient.getName().equals(ingredient.getName()) 
                && ingredientRepository.existsByName(ingredient.getName())) {
            throw new BadRequestException("Ingredient with name '" + ingredient.getName() + "' already exists");
        }
        
        // Validate cost
        if (ingredient.getCostPerUnit() == null || ingredient.getCostPerUnit().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Cost per unit must be greater than 0");
        }
        
        existingIngredient.setName(ingredient.getName());
        existingIngredient.setUnit(ingredient.getUnit());
        existingIngredient.setCostPerUnit(ingredient.getCostPerUnit());
        
        // Note: We don't update 'active' status here - use toggle method instead
        
        return ingredientRepository.save(existingIngredient);
    }

    @Override
    public Ingredient getIngredientById(Long id) {
        return ingredientRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Ingredient not found with id: " + id));
    }

    @Override
    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    @Override
    public Ingredient toggleIngredientStatus(Long id) {
        Ingredient ingredient = ingredientRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Ingredient not found with id: " + id));
        
        ingredient.setActive(!ingredient.getActive());
        return ingredientRepository.save(ingredient);
    }

    @Override
    public Long getTotalUsageByIngredient(Long ingredientId) {
        if (!ingredientRepository.existsById(ingredientId)) {
            throw new NotFoundException("Ingredient not found with id: " + ingredientId);
        }
        
        return ingredientRepository.getTotalUsageByIngredient(ingredientId);
    }
}