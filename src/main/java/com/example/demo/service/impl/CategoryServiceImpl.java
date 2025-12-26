package com.example.demo.service.impl;

import com.example.demo.entity.Category;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    
    private final CategoryRepository categoryRepository;
    
    @Override
    @Transactional
    public Category createCategory(Category category) {
        // Check for duplicate name
        categoryRepository.findByNameIgnoreCase(category.getName().trim())
            .ifPresent(existing -> {
                throw new BadRequestException("Category with name '" + category.getName() + "' already exists");
            });
        
        category.setActive(true);
        return categoryRepository.save(category);
    }
    
    @Override
    @Transactional
    public Category updateCategory(Long id, Category updatedCategory) {
        Category existing = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        
        // Check for duplicate name if name is being changed
        if (!existing.getName().equalsIgnoreCase(updatedCategory.getName().trim())) {
            categoryRepository.findByNameIgnoreCase(updatedCategory.getName().trim())
                .ifPresent(duplicate -> {
                    throw new BadRequestException("Category with name '" + updatedCategory.getName() + "' already exists");
                });
            existing.setName(updatedCategory.getName());
        }
        
        existing.setDescription(updatedCategory.getDescription());
        
        return categoryRepository.save(existing);
    }
    
    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }
    
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    
    @Override
    @Transactional
    public void deactivateCategory(Long id) {
        Category category = getCategoryById(id);
        category.setActive(false);
        categoryRepository.save(category);
    }
}