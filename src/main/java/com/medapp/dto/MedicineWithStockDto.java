package com.medapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineWithStockDto {
    private MedicineDto medicine;
    private StockDto stock;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockDto {
        private LocalDate expDate;
        private Integer quantity;
        private Integer availableQuantity;
        private Double price;
        private LocalDateTime createdAt;
    }
} 