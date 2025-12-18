package com.example.demo.service;

import com.example.demo.entity.MenuItem;
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
    public MenuItem createMenuItem(MenuItem menuItem) {
        // Check if menu item with same name already exists
        if (menuItemRepository.existsByName(menuItem.getName())) {
            throw new RuntimeException("Menu item with name '" + menuItem.getName() + "' already exists");
        }
        return menuItemRepository.save(menuItem);
    }

    @Override
    public MenuItem updateMenuItem(Long id, MenuItem menuItemDetails) {
        MenuItem menuItem = getMenuItemById(id);
        
        // Check if name is being changed and if new name already exists
        if (!menuItem.getName().equals(menuItemDetails.getName()) && 
            menuItemRepository.existsByName(menuItemDetails.getName())) {
            throw new RuntimeException("Menu item with name '" + menuItemDetails.getName() + "' already exists");
        }
        
        menuItem.setName(menuItemDetails.getName());
        menuItem.setDescription(menuItemDetails.getDescription());
        menuItem.setPrice(menuItemDetails.getPrice());
        menuItem.setCategory(menuItemDetails.getCategory());
        
        return menuItemRepository.save(menuItem);
    }

    @Override
    public void deleteMenuItem(Long id) {
        MenuItem menuItem = getMenuItemById(id);
        menuItemRepository.delete(menuItem);
    }
}