package com.medapp.service;

import com.medapp.entity.MedStock;
import com.medapp.entity.Sell;
import java.time.LocalDate;
import java.util.List;

public interface HistoryService {
    List<Sell> getSalesHistory(LocalDate fromDate, LocalDate toDate);
    List<MedStock> getPurchaseHistory(LocalDate fromDate, LocalDate toDate);
} 