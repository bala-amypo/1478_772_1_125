package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "profit_calculation_records")
public class ProfitCalculationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Menu item is required")
    @ManyToOne
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    @NotNull(message = "Total cost is required")
    @Column(name = "total_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalCost;

    @NotNull(message = "Profit margin is required")
    @Column(name = "profit_margin", nullable = false, precision = 5, scale = 2)
    private BigDecimal profitMargin; // percentage

    @Column(name = "calculated_at", nullable = false)
    private LocalDateTime calculatedAt;

    // Auto-set calculatedAt before persisting
    @PrePersist
    protected void onCreate() {
        calculatedAt = LocalDateTime.now();
    }

    // Constructors
    public ProfitCalculationRecord() {}

    public ProfitCalculationRecord(MenuItem menuItem, BigDecimal totalCost, BigDecimal profitMargin) {
        this.menuItem = menuItem;
        this.totalCost = totalCost;
        this.profitMargin = profitMargin;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public MenuItem getMenuItem() { return menuItem; }
    public void setMenuItem(MenuItem menuItem) { this.menuItem = menuItem; }

    public BigDecimal getTotalCost() { return totalCost; }
    public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }

    public BigDecimal getProfitMargin() { return profitMargin; }
    public void setProfitMargin(BigDecimal profitMargin) { this.profitMargin = profitMargin; }

    public LocalDateTime getCalculatedAt() { return calculatedAt; }
    public void setCalculatedAt(LocalDateTime calculatedAt) { this.calculatedAt = calculatedAt; }
}