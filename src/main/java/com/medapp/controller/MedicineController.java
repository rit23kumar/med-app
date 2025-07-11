package com.medapp.controller;

import com.medapp.dto.MedicineDto;
import com.medapp.dto.MedicineWithStockDto;
import com.medapp.dto.BatchMedicineResponse;
import com.medapp.dto.StockHistoryResponse;
import com.medapp.entity.Medicine;
import com.medapp.service.MedicineService;
import com.medapp.service.MedStockService;
import com.medapp.repository.MedCategoryRepository;
import com.medapp.entity.MedCategory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    private final MedCategoryRepository medCategoryRepository;

    @Autowired
    private MedicineService medicineService;

    @Autowired
    private MedStockService medStockService;

    public MedicineController(MedicineService medicineService, MedCategoryRepository medCategoryRepository) {
        this.medicineService = medicineService;
        this.medCategoryRepository = medCategoryRepository;
    }

    @PostMapping
    public ResponseEntity<MedicineDto> addMedicine(@RequestBody MedicineDto medicineDto) {
        return ResponseEntity.ok(medicineService.addMedicine(medicineDto));
    }

    @PostMapping("/batch")
    public ResponseEntity<BatchMedicineResponse> addMedicines(@RequestBody List<MedicineDto> medicines) {
        return ResponseEntity.ok(medicineService.addMedicines(medicines));
    }

    @PostMapping("/with-stock")
    public ResponseEntity<MedicineWithStockDto> addMedicineWithStock(
            @RequestBody MedicineWithStockDto medicineWithStockDto) {
        return ResponseEntity.ok(medicineService.addMedicineWithStock(medicineWithStockDto));
    }

    @GetMapping
    public ResponseEntity<Page<Medicine>> getAllMedicines(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(medicineService.getAllMedicines(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicineDto> getMedicineById(@PathVariable Long id) {
        MedicineDto medicine = medicineService.getMedicineById(id);
        if (medicine != null) {
            return ResponseEntity.ok(medicine);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Medicine>> searchMedicines(
            @RequestParam String name,
            @RequestParam(defaultValue = "contains") String searchType) {
        List<Medicine> medicines = medicineService.searchMedicinesByName(name, searchType);
        return ResponseEntity.ok(medicines);
    }

    @GetMapping("/{id}/stock-history")
    public ResponseEntity<List<StockHistoryResponse>> getMedicineStockHistory(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean includeFinished) {
        return ResponseEntity.ok(medStockService.getMedicineStockHistory(id, includeFinished));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Medicine>> getAllMedicinesUnpaged(@RequestParam(value = "includeDisabled", required = false) Boolean includeDisabled) {
        return ResponseEntity.ok(medicineService.getAllMedicinesUnpaged(includeDisabled));
    }

    @GetMapping("/categories")
    public List<MedCategory> getAllCategories() {
        return medCategoryRepository.findAll();
    }

    @DeleteMapping("/stock/{id}")
    public ResponseEntity<Void> deleteStockBatch(@PathVariable Long id) {
        medStockService.deleteStockBatch(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/stock/{id}")
    public ResponseEntity<StockHistoryResponse> updateStockBatch(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        StockHistoryResponse updatedBatch = medStockService.updateStockBatch(id, updates);
        return ResponseEntity.ok(updatedBatch);
    }

    @GetMapping("/expiring")
    public ResponseEntity<List<StockHistoryResponse>> getExpiringStock(@RequestParam(defaultValue = "90") int days) {
        List<StockHistoryResponse> expiringStock = medStockService.getExpiringStock(days);
        return ResponseEntity.ok(expiringStock);
    }

    @GetMapping("/expired")
    public ResponseEntity<List<StockHistoryResponse>> getExpiredStock() {
        List<StockHistoryResponse> expiredStock = medStockService.getExpiredStock();
        return ResponseEntity.ok(expiredStock);
    }

    @PutMapping("/enabled/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MedicineDto> updateMedicineEnabled(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        Boolean enabled = body.get("enabled");
        if (enabled == null) {
            return ResponseEntity.badRequest().build();
        }
        MedicineDto updated = medicineService.updateMedicineEnabled(id, enabled);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MedicineDto> updateMedicine(@PathVariable Long id, @RequestBody MedicineDto medicineDto) {
        MedicineDto updated = medicineService.updateMedicine(id, medicineDto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/stock/grand-total")
    public ResponseEntity<Double> getGrandTotalStockValue() {
        double total = medStockService.getGrandTotalStockValue();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/flat-export")
    public ResponseEntity<List<com.medapp.dto.MedicineStockFlatExportDto>> getAllMedicineStockFlatExport() {
        return ResponseEntity.ok(medStockService.getAllMedicineStockFlatExport());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMedicine(@PathVariable Long id) {
        try {
            medicineService.deleteMedicine(id);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(Map.of("message", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
} 