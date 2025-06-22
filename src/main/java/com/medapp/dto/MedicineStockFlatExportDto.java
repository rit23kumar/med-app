package com.medapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineStockFlatExportDto {
    private String name;
    private boolean enabled;
    private String expDate;
    private int availableQty;
    private double price;
} 