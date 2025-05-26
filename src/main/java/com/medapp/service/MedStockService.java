package com.medapp.service;

import com.medapp.dto.MedicineWithStockDto;
import com.medapp.entity.Medicine;
import com.medapp.entity.MedStock;
import com.medapp.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;

@Service
public class MedStockService {

    @Autowired
    private MedicineRepository medicineRepository;

    @Transactional
    public MedStock addStock(Long medicineId, MedicineWithStockDto.StockDto stockDto) {
        Medicine medicine = medicineRepository.findById(medicineId)
            .orElseThrow(() -> new EntityNotFoundException("Medicine not found with id: " + medicineId));

        MedStock stock = new MedStock();
        stock.setMedicine(medicine);
        stock.setExpDate(stockDto.getExpDate());
        stock.setQuantity(stockDto.getQuantity());
        stock.setPrice(stockDto.getPrice());

        return stock;
    }
} 