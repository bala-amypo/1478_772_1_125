package com.example.demo.controller;

import com.example.demo.entity.ProfitCalculationRecord;
import com.example.demo.service.ProfitCalculationRecordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profit-records")
public class ProfitCalculationRecordController {

    @Autowired
    private ProfitCalculationRecordService profitRecordService;

    @GetMapping
    public ResponseEntity<List<ProfitCalculationRecord>> getAllRecords() {
        List<ProfitCalculationRecord> records = profitRecordService.getAllRecords();
        return ResponseEntity.ok(records);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfitCalculationRecord> getRecordById(@PathVariable Long id) {
        ProfitCalculationRecord record = profitRecordService.getRecordById(id);
        return ResponseEntity.ok(record);
    }

    @GetMapping("/menu-item/{menuItemId}")
    public ResponseEntity<List<ProfitCalculationRecord>> getRecordsByMenuItem(@PathVariable Long menuItemId) {
        List<ProfitCalculationRecord> records = profitRecordService.getRecordsByMenuItem(menuItemId);
        return ResponseEntity.ok(records);
    }

    @PostMapping
    public ResponseEntity<ProfitCalculationRecord> createRecord(@Valid @RequestBody ProfitCalculationRecord record) {
        ProfitCalculationRecord createdRecord = profitRecordService.createRecord(record);
        return new ResponseEntity<>(createdRecord, HttpStatus.CREATED);
    }

    @PostMapping("/calculate/{menuItemId}")
    public ResponseEntity<ProfitCalculationRecord> calculateProfit(@PathVariable Long menuItemId) {
        ProfitCalculationRecord record = profitRecordService.calculateProfitForMenuItem(menuItemId);
        return new ResponseEntity<>(record, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        profitRecordService.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }
}