package com.example.demo.service.impl;

import com.example.demo.entity.Ingredient;
import com.example.demo.entity.MenuItem;
import com.example.demo.entity.ProfitCalculationRecord;
import com.example.demo.entity.RecipeIngredient;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.IngredientRepository;
import com.example.demo.repository.MenuItemRepository;
import com.example.demo.repository.ProfitCalculationRecordRepository;
import com.example.demo.repository.RecipeIngredientRepository;
import com.example.demo.service.ProfitCalculationService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ProfitCalculationServiceImpl implements ProfitCalculationService {
    private final MenuItemRepository menuItemRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final IngredientRepository ingredientRepository;
    private final ProfitCalculationRecordRepository profitCalculationRecordRepository;

    public ProfitCalculationServiceImpl(MenuItemRepository menuItemRepository,
                                       RecipeIngredientRepository recipeIngredientRepository,
                                       IngredientRepository ingredientRepository,
                                       ProfitCalculationRecordRepository profitCalculationRecordRepository) {
        this.menuItemRepository = menuItemRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.ingredientRepository = ingredientRepository;
        this.profitCalculationRecordRepository = profitCalculationRecordRepository;
    }

    @Override
    public ProfitCalculationRecord calculateProfit(Long menuItemId) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + menuItemId));

        List<RecipeIngredient> recipeIngredients = recipeIngredientRepository.findByMenuItemId(menuItemId);
        
        if (recipeIngredients.isEmpty()) {
            throw new BadRequestException("Cannot calculate profit for menu item without recipe ingredients");
        }

        BigDecimal totalCost = BigDecimal.ZERO;
        for (RecipeIngredient ri : recipeIngredients) {
            Ingredient ingredient = ri.getIngredient();
            BigDecimal ingredientCost = ingredient.getCostPerUnit()
                    .multiply(BigDecimal.valueOf(ri.getQuantityRequired()));
            totalCost = totalCost.add(ingredientCost);
        }

        BigDecimal sellingPrice = menuItem.getSellingPrice();
        if (sellingPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Invalid selling price for menu item");
        }

        BigDecimal profit = sellingPrice.subtract(totalCost);
        double profitMargin = profit.divide(sellingPrice, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();

        ProfitCalculationRecord record = new ProfitCalculationRecord();
        record.setMenuItem(menuItem);
        record.setTotalCost(totalCost);
        record.setProfitMargin(profitMargin);

        return profitCalculationRecordRepository.save(record);
    }

    @Override
    @Transactional(readOnly = true)
    public ProfitCalculationRecord getCalculationById(Long id) {
        return profitCalculationRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profit calculation record not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProfitCalculationRecord> getCalculationsForMenuItem(Long menuItemId) {
        if (!menuItemRepository.existsById(menuItemId)) {
            throw new ResourceNotFoundException("Menu item not found with id: " + menuItemId);
        }
        return profitCalculationRecordRepository.findByMenuItemId(menuItemId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProfitCalculationRecord> getAllCalculations() {
        return profitCalculationRecordRepository.findAll();
    }

   @Override
@Transactional(readOnly = true)
public List<ProfitCalculationRecord> findRecordsWithMarginBetween(Double min, Double max) {
    if (min == null || max == null || min > max) {
        throw new BadRequestException("Invalid margin range");
    }
    
    // Simple implementation - get all and filter
    List<ProfitCalculationRecord> allRecords = profitCalculationRecordRepository.findAll();
    return allRecords.stream()
            .filter(record -> record.getProfitMargin() >= min && record.getProfitMargin() <= max)
            .collect(Collectors.toList());
}