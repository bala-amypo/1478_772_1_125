package com.example.demo.service.impl;

import com.example.demo.entity.Category;
import com.example.demo.entity.MenuItem;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.MenuItemRepository;
import com.example.demo.repository.RecipeIngredientRepository;
import com.example.demo.service.MenuItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class MenuItemServiceImpl implements MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final CategoryRepository categoryRepository;

    public MenuItemServiceImpl(MenuItemRepository menuItemRepository,
                              RecipeIngredientRepository recipeIngredientRepository,
                              CategoryRepository categoryRepository) {
        this.menuItemRepository = menuItemRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public MenuItem createMenuItem(MenuItem item) {
        menuItemRepository.findByNameIgnoreCase(item.getName())
                .ifPresent(existing -> {
                    throw new BadRequestException("Menu item with name '" + item.getName() + "' already exists");
                });

        if (item.getSellingPrice() == null || item.getSellingPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Selling price must be greater than 0");
        }

        if (item.getCategories() != null && !item.getCategories().isEmpty()) {
            Set<Category> managedCategories = new HashSet<>();
            for (Category category : item.getCategories()) {
                Category managedCategory = categoryRepository.findById(category.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + category.getId()));
                
                if (!managedCategory.getActive()) {
                    throw new BadRequestException("Cannot assign inactive category: " + managedCategory.getName());
                }
                managedCategories.add(managedCategory);
            }
            item.setCategories(managedCategories);
        }

        item.setActive(true);
        return menuItemRepository.save(item);
    }

    @Override
    public MenuItem updateMenuItem(Long id, MenuItem item) {
        MenuItem existing = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));

        if (!existing.getName().equalsIgnoreCase(item.getName())) {
            menuItemRepository.findByNameIgnoreCase(item.getName())
                    .ifPresent(duplicate -> {
                        throw new BadRequestException("Menu item with name '" + item.getName() + "' already exists");
                    });
        }

        if (item.getSellingPrice() != null && item.getSellingPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Selling price must be greater than 0");
        }

        if (Boolean.TRUE.equals(item.getActive()) && !Boolean.TRUE.equals(existing.getActive())) {
            if (!recipeIngredientRepository.existsByMenuItemId(id)) {
                throw new BadRequestException("Cannot activate menu item without recipe ingredients");
            }
        }

        existing.setName(item.getName());
        existing.setDescription(item.getDescription());
        existing.setSellingPrice(item.getSellingPrice());
        existing.setActive(item.getActive());

        if (item.getCategories() != null) {
            Set<Category> managedCategories = new HashSet<>();
            for (Category category : item.getCategories()) {
                Category managedCategory = categoryRepository.findById(category.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + category.getId()));
                
                if (!managedCategory.getActive()) {
                    throw new BadRequestException("Cannot assign inactive category: " + managedCategory.getName());
                }
                managedCategories.add(managedCategory);
            }
            existing.setCategories(managedCategories);
        }

        return menuItemRepository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public MenuItem getMenuItemById(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    @Override
    public void deactivateMenuItem(Long id) {
        MenuItem menuItem = getMenuItemById(id);
        menuItem.setActive(false);
        menuItemRepository.save(menuItem);
    }
}