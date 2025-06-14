package com.medapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Med_Stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedStock {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "Med_ID", nullable = false)
    private Medicine medicine;
    
    @Column(name = "Exp_Date")
    private LocalDate expDate;
    
    private Integer quantity;
    
    @Column(name = "Available_Quantity")
    private Integer availableQuantity;
    
    private Double price;

    @Column(name = "Created_At", nullable = false, updatable = false)
    private LocalDateTime createdAt;
} 