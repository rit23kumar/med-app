package com.medapp.controller;

import com.medapp.dto.MedicineDto;
import com.medapp.dto.MedicineWithStockDto;
import com.medapp.entity.Medicine;
import com.medapp.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    @PostMapping
    public ResponseEntity<MedicineDto> addMedicine(@RequestBody MedicineDto medicineDto) {
        return ResponseEntity.ok(medicineService.addMedicine(medicineDto));
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
} 