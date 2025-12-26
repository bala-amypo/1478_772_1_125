package com.example.demo.service.impl;

import com.example.demo.entity.Category;
import com.example.demo.entity.MenuItem;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.MenuItemRepository;
import com.example.demo.repository.RecipeIngredientRepository;
import com.example.demo.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {
    
    private final MenuItemRepository menuItemRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final CategoryRepository categoryRepository;
    
    @Override
    @Transactional
    public MenuItem createMenuItem(MenuItem item) {
        // Validate price > 0
        if (item.getSellingPrice() == null || item.getSellingPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Selling price must be greater than 0");
        }
        
        // Check for duplicate name
        menuItemRepository.findByNameIgnoreCase(item.getName().trim())
            .ifPresent(existing -> {
                throw new BadRequestException("Menu item with name '" + item.getName() + "' already exists");
            });
        
        // Process categories if provided
        if (item.getCategories() != null && !item.getCategories().isEmpty()) {
            Set<Category> validCategories = new HashSet<>();
            for (Category category : item.getCategories()) {
                Category existingCategory = categoryRepository.findById(category.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + category.getId()));
                
                if (!existingCategory.isActive()) {
                    throw new BadRequestException("Cannot assign inactive category");
                }
                
                validCategories.add(existingCategory);
            }
            item.setCategories(validCategories);
        } else {
            item.setCategories(new HashSet<>());
        }
        
        // New menu items are inactive by default until they have recipe ingredients
        item.setActive(false);
        return menuItemRepository.save(item);
    }
    
    @Override
    @Transactional
    public MenuItem updateMenuItem(Long id, MenuItem updatedItem) {
        MenuItem existing = menuItemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));
        
        // Validate price if being updated
        if (updatedItem.getSellingPrice() != null) {
            if (updatedItem.getSellingPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                throw new BadRequestException("Selling price must be greater than 0");
            }
            existing.setSellingPrice(updatedItem.getSellingPrice());
        }
        
        // Check for duplicate name if name is being changed
        if (updatedItem.getName() != null && !updatedItem.getName().equalsIgnoreCase(existing.getName())) {
            menuItemRepository.findByNameIgnoreCase(updatedItem.getName().trim())
                .ifPresent(duplicate -> {
                    throw new BadRequestException("Menu item with name '" + updatedItem.getName() + "' already exists");
                });
            existing.setName(updatedItem.getName());
        }
        
        if (updatedItem.getDescription() != null) {
            existing.setDescription(updatedItem.getDescription());
        }
        
        // Handle activation - cannot activate without recipe ingredients
        if (updatedItem.isActive() && !existing.isActive()) {
            if (!recipeIngredientRepository.existsByMenuItemId(id)) {
                throw new BadRequestException("Cannot activate menu item without recipe ingredients");
            }
            existing.setActive(true);
        } else if (!updatedItem.isActive() && existing.isActive()) {
            existing.setActive(false);
        }
        
        // Update categories if provided
        if (updatedItem.getCategories() != null) {
            Set<Category> validCategories = new HashSet<>();
            for (Category category : updatedItem.getCategories()) {
                Category existingCategory = categoryRepository.findById(category.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + category.getId()));
                
                if (!existingCategory.isActive()) {
                    throw new BadRequestException("Cannot assign inactive category");
                }
                
                validCategories.add(existingCategory);
            }
            existing.setCategories(validCategories);
        }
        
        return menuItemRepository.save(existing);
    }
    
    @Override
    public MenuItem getMenuItemById(Long id) {
        return menuItemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));
    }
    
    @Override
    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }
    
    @Override
    @Transactional
    public void deactivateMenuItem(Long id) {
        MenuItem menuItem = getMenuItemById(id);
        menuItem.setActive(false);
        menuItemRepository.save(menuItem);
    }
}