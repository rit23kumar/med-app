package com.medapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

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
    private Double price;
} 