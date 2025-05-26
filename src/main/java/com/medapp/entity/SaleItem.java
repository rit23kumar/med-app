package com.medapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "Sale_Item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "Sale_ID", nullable = false)
    private Sale sale;
    
    @ManyToOne
    @JoinColumn(name = "Med_ID", nullable = false)
    private Medicine medicine;
    
    @Column(name = "Exp_date")
    private LocalDate expDate;
    
    private Integer quantity;
    private Double price;
} 