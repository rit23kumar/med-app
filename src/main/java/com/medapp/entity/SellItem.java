package com.medapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;

@Entity
@Table(name = "Sell_Item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "Sell_ID", nullable = false)
    @JsonIgnore
    private Sell sell;
    
    @ManyToOne
    @JoinColumn(name = "Med_ID", nullable = false)
    private Medicine medicine;
    
    @Column(name = "Exp_date")
    private LocalDate expDate;
    
    private Integer quantity;
    private Double price;
    
    @Column(name = "discount")
    private Double discount;

    @Column(name = "batch_id")
    private Long batchId;
} 