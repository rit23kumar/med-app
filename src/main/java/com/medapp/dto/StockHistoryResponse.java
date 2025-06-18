package com.medapp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockHistoryResponse {
    private Long id;
    private String medicineName;
    private LocalDate expDate;
    private Integer quantity;
    private Integer availableQuantity;
    private Double price;
    private LocalDateTime createdAt;
} 