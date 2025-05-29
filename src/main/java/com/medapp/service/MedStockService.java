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
import java.util.List;
import java.util.stream.Collectors;

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

        stock = medStockRepository.save(stock);
        
        // Copy back the creation date to the DTO
        stockDto.setCreatedAt(stock.getCreatedAt());
        
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
            .map(stock -> {
                StockHistoryResponse response = new StockHistoryResponse();
                BeanUtils.copyProperties(stock, response);
                return response;
            })
            .collect(Collectors.toList());
    }
} 