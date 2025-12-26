package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(unique = true)
    private String name;
    
    private String description;
    
    private boolean active = true;
    
    @ManyToMany(mappedBy = "categories")
    private Set<MenuItem> menuItems = new HashSet<>();
    
    @PrePersist
    protected void onCreate() {
        if (menuItems == null) {
            menuItems = new HashSet<>();
        }
    }
}