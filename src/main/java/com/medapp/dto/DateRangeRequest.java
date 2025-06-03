package com.medapp.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class DateRangeRequest {
    private LocalDate fromDate;
    private LocalDate toDate;
} 