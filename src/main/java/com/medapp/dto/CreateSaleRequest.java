package com.medapp.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateSaleRequest {
    private String customer;
    private List<SaleItemRequest> items;

    @Data
    public static class SaleItemRequest {
        private Long medicineId;
        private Integer quantity;
        private Double price;
        private String expDate;
    }
} 