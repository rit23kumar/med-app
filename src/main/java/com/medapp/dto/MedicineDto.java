package com.medapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineDto {
    private Long id;
    private String name;
    private boolean enabled;
    private Integer categoryId;
} 