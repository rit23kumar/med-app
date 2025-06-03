package com.medapp.service.impl;

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

@Service
public class HistoryServiceImpl implements HistoryService {

    @Autowired
    private SellRepository sellRepository;

    @Autowired
    private MedStockRepository medStockRepository;

    @Override
    public List<Sell> getSalesHistory(LocalDate fromDate, LocalDate toDate) {
        LocalDateTime startDateTime = fromDate.atStartOfDay();
        LocalDateTime endDateTime = toDate.plusDays(1).atStartOfDay();
        return sellRepository.findByDateBetween(startDateTime, endDateTime);
    }

    @Override
    public List<MedStock> getPurchaseHistory(LocalDate fromDate, LocalDate toDate) {
        LocalDateTime startDateTime = fromDate.atStartOfDay();
        LocalDateTime endDateTime = toDate.plusDays(1).atStartOfDay();
        return medStockRepository.findByCreatedAtBetween(startDateTime, endDateTime);
    }
} 