package com.medapp.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateSellRequest {
    private String customer;
    private List<SellItemRequest> items;

    @Data
    public static class SellItemRequest {
        private Long medicineId;
        private Integer quantity;
        private Double price;
        private String expDate;
    }
} 