package com.example.demo.service.impl;

import com.example.demo.entity.Ingredient;
import com.example.demo.entity.MenuItem;
import com.example.demo.entity.ProfitCalculationRecord;
import com.example.demo.entity.RecipeIngredient;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.service.ProfitCalculationService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfitCalculationServiceImpl implements ProfitCalculationService {
    
    private final MenuItemRepository menuItemRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final IngredientRepository ingredientRepository;
    private final ProfitCalculationRecordRepository profitCalculationRecordRepository;
    
    @Override
    @Transactional
    public ProfitCalculationRecord calculateProfit(Long menuItemId) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
            .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + menuItemId));
        
        List<RecipeIngredient> recipeIngredients = recipeIngredientRepository.findByMenuItemId(menuItemId);
        
        if (recipeIngredients.isEmpty()) {
            throw new BadRequestException("Cannot calculate profit: no ingredients in recipe");
        }
        
        BigDecimal totalCost = BigDecimal.ZERO;
        
        for (RecipeIngredient ri : recipeIngredients) {
            Ingredient ingredient = ri.getIngredient();
            if (!ingredient.isActive()) {
                throw new BadRequestException("Cannot calculate profit with inactive ingredient: " + ingredient.getName());
            }
            
            BigDecimal ingredientCost = ingredient.getCostPerUnit()
                .multiply(BigDecimal.valueOf(ri.getQuantityRequired()));
            totalCost = totalCost.add(ingredientCost);
        }
        
        BigDecimal sellingPrice = menuItem.getSellingPrice();
        BigDecimal profitAmount = sellingPrice.subtract(totalCost);
        double profitMargin = profitAmount.divide(sellingPrice, 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100))
            .doubleValue();
        
        ProfitCalculationRecord record = new ProfitCalculationRecord();
        record.setMenuItem(menuItem);
        record.setTotalCost(totalCost);
        record.setProfitMargin(profitMargin);
        
        return profitCalculationRecordRepository.save(record);
    }
    
    @Override
    public ProfitCalculationRecord getCalculationById(Long id) {
        return profitCalculationRecordRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Profit calculation record not found with id: " + id));
    }
    
    @Override
    public List<ProfitCalculationRecord> getCalculationsForMenuItem(Long menuItemId) {
        return profitCalculationRecordRepository.findByMenuItemId(menuItemId);
    }
    
    @Override
    public List<ProfitCalculationRecord> getAllCalculations() {
        return profitCalculationRecordRepository.findAll();
    }
    
    @Override
    public List<ProfitCalculationRecord> findRecordsWithMarginBetween(Double min, Double max) {
        return profitCalculationRecordRepository.findAll(new Specification<ProfitCalculationRecord>() {
            @Override
            public Predicate toPredicate(Root<ProfitCalculationRecord> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                
                if (min != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("profitMargin"), min));
                }
                if (max != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("profitMargin"), max));
                }
                
                return cb.and(predicates.toArray(new Predicate[0]));
            }
        });
    }
}