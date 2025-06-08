package com.medapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Sell")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sell {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDateTime date;
    
    @Column(name = "Total_Amount")
    private Double totalAmount;
    
    @Column(nullable = true)
    private String customer;

    @Column(name = "mode_of_payment")
    private String modeOfPayment;

    @OneToMany(mappedBy = "sell", cascade = CascadeType.ALL)
    private List<SellItem> items;
} 