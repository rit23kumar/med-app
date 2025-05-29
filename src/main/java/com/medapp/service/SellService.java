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

@Service
public class SellService {

    @Autowired
    private SellRepository sellRepository;

    @Autowired
    private MedicineRepository medicineRepository;
    
    @Autowired
    private MedStockRepository medStockRepository;

    @Transactional
    public Sell createSell(CreateSellRequest request) {
        Sell sell = new Sell();
        sell.setDate(LocalDateTime.now());
        sell.setCustomer(request.getCustomer());
        
        List<SellItem> items = new ArrayList<>();
        double totalAmount = 0.0;

        for (SellItemRequest itemRequest : request.getItems()) {
            Medicine medicine = medicineRepository.findById(itemRequest.getMedicineId())
                .orElseThrow(() -> new RuntimeException("Medicine not found"));

            // Find the stock entry with matching expiry date
            List<MedStock> stockEntries = medStockRepository.findByMedicineIdAndExpDate(
                medicine.getId(), 
                LocalDate.parse(itemRequest.getExpDate())
            );
            
            if (stockEntries.isEmpty()) {
                throw new RuntimeException("No stock found for medicine with given expiry date");
            }
            
            MedStock stockEntry = stockEntries.get(0);
            
            // Check if enough quantity is available
            if (stockEntry.getAvailableQuantity() < itemRequest.getQuantity()) {
                throw new RuntimeException("Insufficient stock available");
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
} 