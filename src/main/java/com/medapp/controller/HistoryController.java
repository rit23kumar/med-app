package com.medapp.controller;

import com.medapp.dto.DateRangeRequest;
import com.medapp.dto.StockHistoryResponse;
import com.medapp.entity.Sell;
import com.medapp.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@CrossOrigin(origins = "*")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @PostMapping("/sales")
    public ResponseEntity<List<Sell>> getSalesHistory(@RequestBody DateRangeRequest request) {
        return ResponseEntity.ok(historyService.getSalesHistory(request.getFromDate(), request.getToDate(), request.getDateType()));
    }

    @PostMapping("/purchases")
    public ResponseEntity<List<StockHistoryResponse>> getPurchaseHistory(@RequestBody DateRangeRequest request) {
        return ResponseEntity.ok(historyService.getPurchaseHistory(request.getFromDate(), request.getToDate()));
    }
} 