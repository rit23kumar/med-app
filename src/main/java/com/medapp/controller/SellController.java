package com.medapp.controller;

import com.medapp.dto.CreateSellRequest;
import com.medapp.entity.Sell;
import com.medapp.service.SellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@RestController
@RequestMapping("/api/sells")
public class SellController {

    @Autowired
    private SellService sellService;

    @PostMapping
    public ResponseEntity<Sell> createSell(@RequestBody CreateSellRequest request, Principal principal) {
        String username = principal != null ? principal.getName() : "anonymous";
        return ResponseEntity.ok(sellService.createSell(request, username));
    }

    @GetMapping
    public ResponseEntity<Page<Sell>> getAllSells(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(sellService.getAllSells(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sell> getSellById(@PathVariable Long id) {
        Sell sell = sellService.getSellById(id);
        if (sell != null) {
            return ResponseEntity.ok(sell);
        }
        return ResponseEntity.notFound().build();
    }
} 