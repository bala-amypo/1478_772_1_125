package com.example.demo.controller;

import com.example.demo.entity.MenuItem;
import com.example.demo.service.MenuItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/menu-items")
public class MenuItemController {

    @Autowired
    private MenuItemService menuItemService;

    // GET all menu items
    @GetMapping
    public ResponseEntity<List<MenuItem>> getAllMenuItems(
            @RequestParam(required = false) Boolean available,
            @RequestParam(required = false) String category) {
        
        if (available != null && available) {
            return ResponseEntity.ok(menuItemService.getAvailableMenuItems());
        }
        if (category != null && !category.isEmpty()) {
            return ResponseEntity.ok(menuItemService.getMenuItemsByCategory(category));
        }
        return ResponseEntity.ok(menuItemService.getAllMenuItems());
    }

    // GET menu item by ID
    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable Long id) {
        MenuItem menuItem = menuItemService.getMenuItemById(id);
        return ResponseEntity.ok(menuItem);
    }

    // GET menu item by name
    @GetMapping("/name/{name}")
    public ResponseEntity<MenuItem> getMenuItemByName(@PathVariable String name) {
        MenuItem menuItem = menuItemService.getMenuItemByName(name);
        return ResponseEntity.ok(menuItem);
    }

    // GET menu items by category
    @GetMapping("/category/{category}")
    public ResponseEntity<List<MenuItem>> getMenuItemsByCategory(@PathVariable String category) {
        List<MenuItem> menuItems = menuItemService.getMenuItemsByCategory(category);
        return ResponseEntity.ok(menuItems);
    }

    // GET menu items by price range
    @GetMapping("/price-range")
    public ResponseEntity<List<MenuItem>> getMenuItemsByPriceRange(
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        List<MenuItem> menuItems = menuItemService.getMenuItemsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(menuItems);
    }

    // SEARCH menu items by name
    @GetMapping("/search")
    public ResponseEntity<List<MenuItem>> searchMenuItemsByName(@RequestParam String name) {
        List<MenuItem> menuItems = menuItemService.searchMenuItemsByName(name);
        return ResponseEntity.ok(menuItems);
    }

    // POST create new menu item
    @PostMapping
    public ResponseEntity<MenuItem> createMenuItem(@Valid @RequestBody MenuItem menuItem) {
        MenuItem createdMenuItem = menuItemService.createMenuItem(menuItem);
        return new ResponseEntity<>(createdMenuItem, HttpStatus.CREATED);
    }

    // PUT update menu item
    @PutMapping("/{id}")
    public ResponseEntity<MenuItem> updateMenuItem(@PathVariable Long id, 
                                                   @Valid @RequestBody MenuItem menuItem) {
        MenuItem updatedMenuItem = menuItemService.updateMenuItem(id, menuItem);
        return ResponseEntity.ok(updatedMenuItem);
    }

    // PATCH update availability only
    @PatchMapping("/{id}/availability")
    public ResponseEntity<MenuItem> updateMenuItemAvailability(@PathVariable Long id, 
                                                                @RequestBody Map<String, Boolean> request) {
        Boolean isAvailable = request.get("isAvailable");
        if (isAvailable == null) {
            return ResponseEntity.badRequest().build();
        }
        MenuItem updatedMenuItem = menuItemService.updateMenuItemAvailability(id, isAvailable);
        return ResponseEntity.ok(updatedMenuItem);
    }

    // PATCH add category to menu item
    @PatchMapping("/{id}/categories/add")
    public ResponseEntity<MenuItem> addCategoryToMenuItem(@PathVariable Long id, 
                                                           @RequestBody Map<String, Long> request) {
        Long categoryId = request.get("categoryId");
        if (categoryId == null) {
            return ResponseEntity.badRequest().build();
        }
        MenuItem updatedMenuItem = menuItemService.addCategoryToMenuItem(id, categoryId);
        return ResponseEntity.ok(updatedMenuItem);
    }

    // PATCH remove category from menu item
    @PatchMapping("/{id}/categories/remove")
    public ResponseEntity<MenuItem> removeCategoryFromMenuItem(@PathVariable Long id, 
                                                                @RequestBody Map<String, Long> request) {
        Long categoryId = request.get("categoryId");
        if (categoryId == null) {
            return ResponseEntity.badRequest().build();
        }
        MenuItem updatedMenuItem = menuItemService.removeCategoryFromMenuItem(id, categoryId);
        return ResponseEntity.ok(updatedMenuItem);
    }

    // GET categories for a menu item
    @GetMapping("/{id}/categories")
    public ResponseEntity<List<String>> getMenuItemCategories(@PathVariable Long id) {
        List<String> categories = menuItemService.getMenuItemCategories(id);
        return ResponseEntity.ok(categories);
    }

    // DELETE menu item
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.noContent().build();
    }
}