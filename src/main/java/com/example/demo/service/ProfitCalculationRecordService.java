package com.example.demo.service;

import com.example.demo.entity.ProfitCalculationRecord;
import java.math.BigDecimal;
import java.util.List;

public interface ProfitCalculationRecordService {
    List<ProfitCalculationRecord> getAllRecords();
    ProfitCalculationRecord getRecordById(Long id);
    List<ProfitCalculationRecord> getRecordsByMenuItem(Long menuItemId);
    ProfitCalculationRecord createRecord(ProfitCalculationRecord record);
    void deleteRecord(Long id);
    ProfitCalculationRecord calculateProfitForMenuItem(Long menuItemId);
}