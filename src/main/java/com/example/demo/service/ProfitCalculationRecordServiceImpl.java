package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ProfitCalculationRecordRepository;
import com.example.demo.repository.MenuItemRepository;
import com.example.demo.repository.RecipeIngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Transactional
public class ProfitCalculationRecordServiceImpl implements ProfitCalculationRecordService {

    @Autowired
    private ProfitCalculationRecordRepository recordRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private RecipeIngredientRepository recipeIngredientRepository;

    @Override
    public List<ProfitCalculationRecord> getAllRecords() {
        return recordRepository.findAll();
    }

    @Override
    public ProfitCalculationRecord getRecordById(Long id) {
        return recordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProfitCalculationRecord", "id", id));
    }

    @Override
    public List<ProfitCalculationRecord> getRecordsByMenuItem(Long menuItemId) {
        return recordRepository.findByMenuItemId(menuItemId);
    }

    @Override
    public ProfitCalculationRecord createRecord(ProfitCalculationRecord record) {
        return recordRepository.save(record);
    }

    @Override
    public void deleteRecord(Long id) {
        ProfitCalculationRecord record = getRecordById(id);
        recordRepository.delete(record);
    }

    @Override
    public ProfitCalculationRecord calculateProfitForMenuItem(Long menuItemId) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem", "id", menuItemId));
        
        // Calculate total cost from recipe ingredients
        List<RecipeIngredient> recipeIngredients = recipeIngredientRepository.findByMenuItemId(menuItemId);
        
        BigDecimal totalCost = BigDecimal.ZERO;
        
        for (RecipeIngredient ri : recipeIngredients) {
            Ingredient ingredient = ri.getIngredient();
            if (ingredient.getUnitCost() != null) {
                BigDecimal ingredientCost = ingredient.getUnitCost()
                        .multiply(BigDecimal.valueOf(ri.getQuantityRequired()));
                totalCost = totalCost.add(ingredientCost);
            }
        }
        
        // Calculate profit margin
        BigDecimal sellingPrice = menuItem.getPrice();
        BigDecimal profit = sellingPrice.subtract(totalCost);
        BigDecimal profitMargin = profit.divide(sellingPrice, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
        
        // Create and save record
        ProfitCalculationRecord record = new ProfitCalculationRecord();
        record.setMenuItem(menuItem);
        record.setTotalCost(totalCost);
        record.setProfitMargin(profitMargin);
        
        return recordRepository.save(record);
    }
}