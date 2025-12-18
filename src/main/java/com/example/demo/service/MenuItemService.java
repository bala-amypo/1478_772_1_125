package com.example.demo.service;

import com.example.demo.entity.MenuItem;
import java.util.List;

public interface MenuItemService {
    List<MenuItem> getAllMenuItems();
    MenuItem getMenuItemById(Long id);
    MenuItem getMenuItemByName(String name);
    List<MenuItem> getMenuItemsByCategory(String category);
    MenuItem createMenuItem(MenuItem menuItem);
    MenuItem updateMenuItem(Long id, MenuItem menuItem);
    void deleteMenuItem(Long id);
}