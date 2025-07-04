package com.medapp.service.impl;

import com.medapp.dto.StockHistoryResponse;
import com.medapp.entity.MedStock;
import com.medapp.entity.Sell;
import com.medapp.repository.MedStockRepository;
import com.medapp.repository.SellRepository;
import com.medapp.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoryServiceImpl implements HistoryService {

    @Autowired
    private SellRepository sellRepository;

    @Autowired
    private MedStockRepository medStockRepository;

    @Override
    public List<Sell> getSalesHistory(LocalDate fromDate, LocalDate toDate, String dateType) {
        LocalDateTime startDateTime = fromDate.atStartOfDay();
        LocalDateTime endDateTime = toDate.plusDays(1).atStartOfDay();
        if ("accountingDate".equalsIgnoreCase(dateType)) {
            return sellRepository.findByAccountingDateBetween(startDateTime, endDateTime);
        } else {
            return sellRepository.findByInvoiceDateBetween(startDateTime, endDateTime);
        }
    }

    @Override
    public List<StockHistoryResponse> getPurchaseHistory(LocalDate fromDate, LocalDate toDate) {
        LocalDateTime startDateTime = fromDate.atStartOfDay();
        LocalDateTime endDateTime = toDate.plusDays(1).atStartOfDay();
        List<MedStock> stocks = medStockRepository.findByCreatedAtBetween(startDateTime, endDateTime);
        
        return stocks.stream()
            .map(stock -> {
                StockHistoryResponse response = new StockHistoryResponse();
                response.setId(stock.getId());
                response.setMedicineName(stock.getMedicine().getName());
                response.setExpDate(stock.getExpDate());
                response.setQuantity(stock.getQuantity());
                response.setAvailableQuantity(stock.getAvailableQuantity());
                response.setPrice(stock.getPrice());
                response.setCreatedAt(stock.getCreatedAt());
                return response;
            })
            .collect(Collectors.toList());
    }
} 