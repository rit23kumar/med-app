package com.medapp.service;

import com.medapp.dto.CreateSellRequest;
import com.medapp.dto.CreateSellRequest.SellItemRequest;
import com.medapp.entity.Sell;
import com.medapp.entity.SellItem;
import com.medapp.entity.Medicine;
import com.medapp.entity.MedStock;
import com.medapp.repository.SellRepository;
import com.medapp.repository.MedicineRepository;
import com.medapp.repository.MedStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.EntityNotFoundException;

@Service
public class SellService {

    @Autowired
    private SellRepository sellRepository;

    @Autowired
    private MedicineRepository medicineRepository;
    
    @Autowired
    private MedStockRepository medStockRepository;

    @Transactional
    public Sell createSell(CreateSellRequest request, String createdBy) {
        Sell sell = new Sell();
        sell.setDate(LocalDateTime.now());
        sell.setCustomer(request.getCustomer() == null || request.getCustomer().trim().isEmpty() ? "ANONYMOUS" : request.getCustomer());
        sell.setModeOfPayment(request.getModeOfPayment());
        sell.setUtrNumber(request.getUtrNumber());
        sell.setCreatedBy(createdBy);
        
        List<SellItem> items = new ArrayList<>();
        double totalAmount = 0.0;

        for (SellItemRequest itemRequest : request.getItems()) {
            Medicine medicine = medicineRepository.findById(itemRequest.getMedicineId())
                .orElseThrow(() -> new EntityNotFoundException("Medicine not found with id: " + itemRequest.getMedicineId()));

            MedStock stockEntry = medStockRepository.findById(itemRequest.getBatchId())
                .orElseThrow(() -> new EntityNotFoundException("Stock batch not found with id: " + itemRequest.getBatchId()));

            // Validate that the medicine and expiry date of the stock entry match the request
            if (!stockEntry.getMedicine().getId().equals(itemRequest.getMedicineId())) {
                throw new IllegalArgumentException("Batch ID " + itemRequest.getBatchId() + " does not belong to medicine ID " + itemRequest.getMedicineId());
            }
            if (!stockEntry.getExpDate().isEqual(LocalDate.parse(itemRequest.getExpDate()))) {
                throw new IllegalArgumentException("Expiry date mismatch for batch ID " + itemRequest.getBatchId());
            }
            
            // Check if enough quantity is available
            if (stockEntry.getAvailableQuantity() < itemRequest.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock available for batch ID " + itemRequest.getBatchId() + ". Available: " + stockEntry.getAvailableQuantity() + ", Requested: " + itemRequest.getQuantity());
            }
            
            // Update available quantity
            stockEntry.setAvailableQuantity(stockEntry.getAvailableQuantity() - itemRequest.getQuantity());
            medStockRepository.save(stockEntry);

            SellItem item = new SellItem();
            item.setSell(sell);
            item.setMedicine(medicine);
            item.setQuantity(itemRequest.getQuantity());
            item.setPrice(itemRequest.getPrice());
            item.setExpDate(LocalDate.parse(itemRequest.getExpDate()));
            item.setDiscount(itemRequest.getDiscount());
            item.setBatchId(itemRequest.getBatchId());
            
            items.add(item);
            totalAmount += itemRequest.getPrice() * itemRequest.getQuantity();
        }

        sell.setTotalAmount(totalAmount);
        sell.setItems(items);
        sell = sellRepository.save(sell);
        return sell;
    }

    public Page<Sell> getAllSells(int page, int size) {
        return sellRepository.findAll(PageRequest.of(page, size));
    }

    public Sell getSellById(Long id) {
        return sellRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteSell(Long sellId) {
        Sell sell = sellRepository.findById(sellId)
            .orElseThrow(() -> new EntityNotFoundException("Sale not found with id: " + sellId));

        for (SellItem item : sell.getItems()) {
            if (item.getBatchId() != null) {
                MedStock stockEntry = medStockRepository.findById(item.getBatchId())
                    .orElseThrow(() -> new EntityNotFoundException("Stock batch not found with id: " + item.getBatchId()));
                
                stockEntry.setAvailableQuantity(stockEntry.getAvailableQuantity() + item.getQuantity());
                medStockRepository.save(stockEntry);
            }
        }

        sellRepository.delete(sell);
    }
} 