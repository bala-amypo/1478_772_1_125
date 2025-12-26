package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "profit_calculation_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfitCalculationRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;
    
    @Column(name = "total_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalCost;
    
    @Column(name = "profit_margin", nullable = false)
    private Double profitMargin;
    
    @Column(name = "calculated_at")
    private LocalDateTime calculatedAt;
    
    @PrePersist
    protected void onCreate() {
        calculatedAt = LocalDateTime.now();
    }
}