package com.medapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;

@Entity
@Table(name = "Sell")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sell {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "invoice_date")
    private LocalDateTime invoiceDate;
    
    @Column(name = "accounting_date")
    private LocalDateTime accountingDate;
    
    @Column(name = "Total_Amount", precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "amount_paid", precision = 15, scale = 2)
    private BigDecimal amountPaid;
    
    @Column(nullable = true)
    private String customer;

    @Column(name = "mode_of_payment")
    private String modeOfPayment;

    @Column(name = "utr_number", length = 12)
    private String utrNumber;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @OneToMany(mappedBy = "sell", cascade = CascadeType.ALL)
    private List<SellItem> items;
} 