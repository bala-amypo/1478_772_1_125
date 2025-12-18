package com.example.demo.service;

import com.example.demo.entity.MenuItem;
import java.util.List;

public interface MenuItemService {
    
    // Basic CRUD operations
    List<MenuItem> getAllMenuItems();
    MenuItem getMenuItemById(Long id);
    MenuItem createMenuItem(MenuItem menuItem);
    MenuItem updateMenuItem(Long id, MenuItem menuItem);
    void deleteMenuItem(Long id);
    
    // Additional business operations
    MenuItem getMenuItemByName(String name);
    List<MenuItem> getMenuItemsByCategory(String category);
    List<MenuItem> getAvailableMenuItems();
    List<MenuItem> searchMenuItemsByName(String name);
    MenuItem updateMenuItemAvailability(Long id, Boolean isAvailable);
    List<MenuItem> getMenuItemsByPriceRange(Double minPrice, Double maxPrice);
    
    // Category management methods
    MenuItem addCategoryToMenuItem(Long menuItemId, Long categoryId);
    MenuItem removeCategoryFromMenuItem(Long menuItemId, Long categoryId);
    List<String> getMenuItemCategories(Long menuItemId);
}