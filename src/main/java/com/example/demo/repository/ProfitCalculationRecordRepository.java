package com.example.demo.repository;

import com.example.demo.entity.ProfitCalculationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfitCalculationRecordRepository extends JpaRepository<ProfitCalculationRecord, Long> {
    
    List<ProfitCalculationRecord> findByMenuItemId(Long menuItemId);
    List<ProfitCalculationRecord> findByProfitMarginGreaterThanEqual(Double margin);
    
    @Query("SELECT p FROM ProfitCalculationRecord p WHERE " +
           "(:min IS NULL OR p.profitMargin >= :min) AND " +
           "(:max IS NULL OR p.profitMargin <= :max)")
    List<ProfitCalculationRecord> findByProfitMarginBetween(
        @Param("min") Double min, 
        @Param("max") Double max);
}