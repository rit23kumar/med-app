package com.medapp.repository;

import com.medapp.entity.MedStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;
import java.time.LocalDate;
import java.util.List;

public interface MedStockRepository extends JpaRepository<MedStock, Long> {
    List<MedStock> findByMedicineId(Long medicineId, Sort sort);
    List<MedStock> findByMedicineIdAndExpDate(Long medicineId, LocalDate expDate);
    List<MedStock> findByMedicineIdAndAvailableQuantityGreaterThan(Long medicineId, Integer minQuantity, Sort sort);
} 