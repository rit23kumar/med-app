package com.medapp.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class SellResponseDto {
    private Long id;
    private LocalDateTime invoiceDate;
    private LocalDateTime accountingDate;
    private Double totalAmount;
    private Double amountPaid;
    private String customer;
    private String modeOfPayment;
    private String utrNumber;
    private String createdBy;
    // private List<SellItemResponseDto> items;
    private List<Object> items;
    // Add other fields as needed
} 