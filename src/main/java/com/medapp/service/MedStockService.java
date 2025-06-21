package com.medapp.service;

import com.medapp.dto.MedicineWithStockDto;
import com.medapp.dto.StockHistoryResponse;
import com.medapp.entity.Medicine;
import com.medapp.entity.MedStock;
import com.medapp.repository.MedicineRepository;
import com.medapp.repository.MedStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.time.LocalDate;

@Service
public class MedStockService {

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private MedStockRepository medStockRepository;

    @Transactional
    public MedStock addStock(Long medicineId, MedicineWithStockDto.StockDto stockDto) {
        Medicine medicine = medicineRepository.findById(medicineId)
            .orElseThrow(() -> new EntityNotFoundException("Medicine not found with id: " + medicineId));

        MedStock stock = new MedStock();
        stock.setMedicine(medicine);
        stock.setExpDate(stockDto.getExpDate());
        stock.setQuantity(stockDto.getQuantity());
        stock.setAvailableQuantity(stockDto.getAvailableQuantity() != null ? stockDto.getAvailableQuantity() : stockDto.getQuantity());
        stock.setPrice(stockDto.getPrice());
        stock.setCreatedAt(LocalDateTime.now());

        stock = medStockRepository.save(stock);        
        
        return stock;
    }

    public List<StockHistoryResponse> getMedicineStockHistory(Long medicineId, boolean includeFinished) {
        // Verify medicine exists
        if (!medicineRepository.existsById(medicineId)) {
            throw new EntityNotFoundException("Medicine not found with id: " + medicineId);
        }

        // Get stock entries based on includeFinished parameter
        List<MedStock> stockEntries;
        if (includeFinished) {
            stockEntries = medStockRepository.findByMedicineId(
                medicineId, 
                Sort.by(Sort.Direction.DESC, "createdAt")
            );
        } else {
            stockEntries = medStockRepository.findByMedicineIdAndAvailableQuantityGreaterThan(
                medicineId,
                0,
                Sort.by(Sort.Direction.DESC, "createdAt")
            );
        }

        // Convert to DTOs
        return stockEntries.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStockBatch(Long batchId) {
        MedStock stock = medStockRepository.findById(batchId)
                .orElseThrow(() -> new EntityNotFoundException("Stock batch not found with id: " + batchId));
        
        if (stock.getQuantity() > stock.getAvailableQuantity()) {
            throw new IllegalArgumentException("Cannot delete batch: Items have already been sold from this stock.");
        }

        medStockRepository.delete(stock);
    }

    @Transactional
    public StockHistoryResponse updateStockBatch(Long batchId, Map<String, Object> updates) {
        MedStock stock = medStockRepository.findById(batchId)
                .orElseThrow(() -> new EntityNotFoundException("Stock batch not found with id: " + batchId));

        updates.forEach((key, value) -> {
            switch (key) {
                case "quantity":
                    stock.setQuantity((Integer) value);
                    break;
                case "availableQuantity":
                    stock.setAvailableQuantity((Integer) value);
                    break;
                case "price":
                    stock.setPrice(Double.valueOf(String.valueOf(value)));
                    break;
                default:
                    // Ignore other fields
                    break;
            }
        });

        MedStock updatedStock = medStockRepository.save(stock);
        return convertToResponse(updatedStock);
    }

    @Transactional(readOnly = true)
    public List<StockHistoryResponse> getExpiringStock(int withinDays) {
        LocalDate today = LocalDate.now();
        LocalDate expiryLimit = today.plusDays(withinDays);
        
        List<MedStock> expiringStock = medStockRepository.findExpiringStock(today, expiryLimit);
        
        return expiringStock.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StockHistoryResponse> getExpiredStock() {
        LocalDate today = LocalDate.now();
        
        List<MedStock> expiredStock = medStockRepository.findExpiredStock(today);
        
        return expiredStock.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private StockHistoryResponse convertToResponse(MedStock medStock) {
        StockHistoryResponse response = new StockHistoryResponse();
        BeanUtils.copyProperties(medStock, response);
        if (medStock.getMedicine() != null) {
            response.setMedicineName(medStock.getMedicine().getName());
        }
        return response;
    }
} 