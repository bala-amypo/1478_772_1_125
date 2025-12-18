package com.example.demo.service;

import com.example.demo.entity.MenuItem;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MenuItemServiceImpl implements MenuItemService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Override
    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
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
    public List<MenuItem> getMenuItemsByCategory(String category) {
        return menuItemRepository.findByCategory(category);
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
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new BadRequestException("Minimum price cannot be greater than maximum price");
        }
        return menuItemRepository.findByPriceBetween(
            minPrice != null ? minPrice : 0.0,
            maxPrice != null ? maxPrice : Double.MAX_VALUE
        );
    }

    @Override
    public MenuItem createMenuItem(MenuItem menuItem) {
        // Validate that name doesn't already exist
        if (menuItemRepository.existsByName(menuItem.getName())) {
            throw new BadRequestException("Menu item with name '" + menuItem.getName() + "' already exists");
        }
        
        // Set default availability if not provided
        if (menuItem.getIsAvailable() == null) {
            menuItem.setIsAvailable(true);
        }
        
        return menuItemRepository.save(menuItem);
    }

    @Override
    public MenuItem updateMenuItem(Long id, MenuItem menuItemDetails) {
        MenuItem menuItem = getMenuItemById(id);
        
        // Check if name is being changed and if new name already exists
        if (!menuItem.getName().equals(menuItemDetails.getName()) && 
            menuItemRepository.existsByName(menuItemDetails.getName())) {
            throw new BadRequestException("Menu item with name '" + menuItemDetails.getName() + "' already exists");
        }
        
        // Update fields
        menuItem.setName(menuItemDetails.getName());
        menuItem.setDescription(menuItemDetails.getDescription());
        menuItem.setPrice(menuItemDetails.getPrice());
        menuItem.setCategory(menuItemDetails.getCategory());
        
        // Only update availability if provided
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
    public void deleteMenuItem(Long id) {
        MenuItem menuItem = getMenuItemById(id);
        menuItemRepository.delete(menuItem);
    }
}