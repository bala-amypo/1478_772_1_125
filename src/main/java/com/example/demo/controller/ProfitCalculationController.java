package com.example.demo.controller;

import com.example.demo.entity.ProfitCalculationRecord;
import com.example.demo.service.ProfitCalculationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profit")
public class ProfitCalculationController {
    private final ProfitCalculationService profitCalculationService;

    public ProfitCalculationController(ProfitCalculationService profitCalculationService) {
        this.profitCalculationService = profitCalculationService;
    }

    @PostMapping("/calculate/{menuItemId}")
    public ResponseEntity<ProfitCalculationRecord> calculateProfit(@PathVariable Long menuItemId) {
        ProfitCalculationRecord record = profitCalculationService.calculateProfit(menuItemId);
        return new ResponseEntity<>(record, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfitCalculationRecord> getCalculationById(@PathVariable Long id) {
        ProfitCalculationRecord record = profitCalculationService.getCalculationById(id);
        return ResponseEntity.ok(record);
    }

    @GetMapping("/menu-item/{menuItemId}")
    public ResponseEntity<List<ProfitCalculationRecord>> getCalculationsForMenuItem(@PathVariable Long menuItemId) {
        List<ProfitCalculationRecord> records = profitCalculationService.getCalculationsForMenuItem(menuItemId);
        return ResponseEntity.ok(records);
    }

    @GetMapping
    public ResponseEntity<List<ProfitCalculationRecord>> getAllCalculations() {
        List<ProfitCalculationRecord> records = profitCalculationService.getAllCalculations();
        return ResponseEntity.ok(records);
    }

    @GetMapping("/margin-between")
    public ResponseEntity<List<ProfitCalculationRecord>> findRecordsWithMarginBetween(
            @RequestParam Double min, 
            @RequestParam Double max) {
        List<ProfitCalculationRecord> records = profitCalculationService.findRecordsWithMarginBetween(min, max);
        return ResponseEntity.ok(records);
    }
}