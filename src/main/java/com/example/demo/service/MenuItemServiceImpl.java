package com.example.demo.service;

import com.example.demo.entity.MenuItem;
import com.example.demo.entity.Category;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.MenuItemRepository;
import com.example.demo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuItemServiceImpl implements MenuItemService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    @Override
    public List<MenuItem> getMenuItemsByCategory(String category) {
        return menuItemRepository.findByCategoriesName(category);
    }

    @Override
    public List<MenuItem> getAvailableMenuItems() {
        return menuItemRepository.findByIsAvailable(true);
    }

    @Override
    public List<MenuItem> searchMenuItemsByName(String name) {
        return menuItemRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<MenuItem> getMenuItemsByPriceRange(Double minPrice, Double maxPrice) {
        // Convert BigDecimal to Double for comparison if needed
        // This is a simplified version - you may need to adjust based on your entity
        List<MenuItem> allItems = menuItemRepository.findAll();
        
        return allItems.stream()
                .filter(item -> {
                    double price = item.getPrice().doubleValue();
                    boolean minCheck = minPrice == null || price >= minPrice;
                    boolean maxCheck = maxPrice == null || price <= maxPrice;
                    return minCheck && maxCheck;
                })
                .collect(Collectors.toList());
    }

    @Override
    public MenuItem getMenuItemById(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem", "id", id));
    }

    @Override
    public MenuItem getMenuItemByName(String name) {
        return menuItemRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem", "name", name));
    }

    @Override
    public MenuItem createMenuItem(MenuItem menuItem) {
        if (menuItemRepository.existsByName(menuItem.getName())) {
            throw new BadRequestException("Menu item with name '" + menuItem.getName() + "' already exists");
        }
        
        if (menuItem.getIsAvailable() == null) {
            menuItem.setIsAvailable(true);
        }
        
        return menuItemRepository.save(menuItem);
    }

    @Override
    public MenuItem updateMenuItem(Long id, MenuItem menuItemDetails) {
        MenuItem menuItem = getMenuItemById(id);
        
        if (!menuItem.getName().equals(menuItemDetails.getName()) && 
            menuItemRepository.existsByName(menuItemDetails.getName())) {
            throw new BadRequestException("Menu item with name '" + menuItemDetails.getName() + "' already exists");
        }
        
        menuItem.setName(menuItemDetails.getName());
        menuItem.setDescription(menuItemDetails.getDescription());
        menuItem.setPrice(menuItemDetails.getPrice());
        menuItem.setPreparationTime(menuItemDetails.getPreparationTime());
        
        if (menuItemDetails.getIsAvailable() != null) {
            menuItem.setIsAvailable(menuItemDetails.getIsAvailable());
        }
        
        return menuItemRepository.save(menuItem);
    }

    @Override
    public MenuItem updateMenuItemAvailability(Long id, Boolean isAvailable) {
        MenuItem menuItem = getMenuItemById(id);
        menuItem.setIsAvailable(isAvailable);
        return menuItemRepository.save(menuItem);
    }

    @Override
    public MenuItem addCategoryToMenuItem(Long menuItemId, Long categoryId) {
        MenuItem menuItem = getMenuItemById(menuItemId);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        
        // Check if category is already associated
        if (!menuItem.getCategories().contains(category)) {
            menuItem.getCategories().add(category);
        }
        
        return menuItemRepository.save(menuItem);
    }

    @Override
    public MenuItem removeCategoryFromMenuItem(Long menuItemId, Long categoryId) {
        MenuItem menuItem = getMenuItemById(menuItemId);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        
        menuItem.getCategories().remove(category);
        
        return menuItemRepository.save(menuItem);
    }

    @Override
    public List<String> getMenuItemCategories(Long menuItemId) {
        MenuItem menuItem = getMenuItemById(menuItemId);
        
        return menuItem.getCategories().stream()
                .map(Category::getName)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMenuItem(Long id) {
        MenuItem menuItem = getMenuItemById(id);
        menuItemRepository.delete(menuItem);
    }
}