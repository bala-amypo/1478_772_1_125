package com.example.demo.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ingredients")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private Double quantity;
    private String unit;

    // Relationship with RecipeIngredient
    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // To prevent infinite recursion in JSON serialization
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();

    // Constructors
    public Ingredient() {}

    public Ingredient(String name, Double quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public List<RecipeIngredient> getRecipeIngredients() { 
        return recipeIngredients; 
    }
    
    public void setRecipeIngredients(List<RecipeIngredient> recipeIngredients) { 
        this.recipeIngredients = recipeIngredients; 
    }
    
    // Helper method to add RecipeIngredient
    public void addRecipeIngredient(RecipeIngredient recipeIngredient) {
        recipeIngredients.add(recipeIngredient);
        recipeIngredient.setIngredient(this);
    }
    
    // Helper method to remove RecipeIngredient
    public void removeRecipeIngredient(RecipeIngredient recipeIngredient) {
        recipeIngredients.remove(recipeIngredient);
        recipeIngredient.setIngredient(null);
    }
}