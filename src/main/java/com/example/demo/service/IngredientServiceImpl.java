
package com.example.demo.service;

import com.example.demo.entity.Ingredient;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class IngredientServiceImpl implements IngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Override
    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    @Override
    public Ingredient getIngredientById(Long id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient", "id", id));
    }

    @Override
    public Ingredient createIngredient(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    @Override
    public Ingredient updateIngredient(Long id, Ingredient ingredientDetails) {
        Ingredient ingredient = getIngredientById(id);
        ingredient.setName(ingredientDetails.getName());
        ingredient.setQuantity(ingredientDetails.getQuantity());
        // Update other fields as needed
        return ingredientRepository.save(ingredient);
    }

    @Override
    public void deleteIngredient(Long id) {
        Ingredient ingredient = getIngredientById(id);
        ingredientRepository.delete(ingredient);
    }
}
