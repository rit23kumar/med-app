package com.medapp.service;

import com.medapp.dto.CreateSaleRequest;
import com.medapp.entity.Sale;
import com.medapp.entity.SaleItem;
import com.medapp.entity.Medicine;
import com.medapp.repository.SaleRepository;
import com.medapp.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    @Transactional
    public Sale createSale(CreateSaleRequest request) {
        Sale sale = new Sale();
        sale.setDate(LocalDate.now());
        sale.setCustomer(request.getCustomer());
        
        List<SaleItem> items = new ArrayList<>();
        double totalAmount = 0.0;

        for (CreateSaleRequest.SaleItemRequest itemRequest : request.getItems()) {
            Medicine medicine = medicineRepository.findById(itemRequest.getMedicineId())
                .orElseThrow(() -> new RuntimeException("Medicine not found"));

            SaleItem item = new SaleItem();
            item.setSale(sale);
            item.setMedicine(medicine);
            item.setQuantity(itemRequest.getQuantity());
            item.setPrice(itemRequest.getPrice());
            item.setExpDate(LocalDate.parse(itemRequest.getExpDate()));
            
            items.add(item);
            totalAmount += itemRequest.getPrice() * itemRequest.getQuantity();
        }

        sale.setTotalAmount(totalAmount);
        sale = saleRepository.save(sale);
        return sale;
    }

    public Page<Sale> getAllSales(int page, int size) {
        return saleRepository.findAll(PageRequest.of(page, size));
    }

    public Sale getSaleById(Long id) {
        return saleRepository.findById(id).orElse(null);
    }
} 