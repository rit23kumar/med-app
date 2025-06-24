package com.medapp.service;

import com.medapp.dto.StockHistoryResponse;
import com.medapp.entity.Sell;
import java.time.LocalDate;
import java.util.List;

public interface HistoryService {
    List<Sell> getSalesHistory(LocalDate fromDate, LocalDate toDate);
    List<StockHistoryResponse> getPurchaseHistory(LocalDate fromDate, LocalDate toDate);
} 