package com.medapp.dto;

import lombok.Data;
import java.util.List;
import java.time.LocalDateTime;

@Data
public class CreateSellRequest {
    private String customer;
    private List<SellItemRequest> items;
    private String modeOfPayment;
    private String utrNumber;
    private Double amountPaid;
    private LocalDateTime invoiceDate;
    private String accountingDateOption; // 'today' or 'tomorrow'

    @Data
    public static class SellItemRequest {
        private Long medicineId;
        private Integer quantity;
        private Double price;
        private String expDate;
        private Double discount;
        private Long batchId;
    }
} 