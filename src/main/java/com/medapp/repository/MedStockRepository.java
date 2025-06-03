package com.medapp.repository;

import com.medapp.entity.MedStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface MedStockRepository extends JpaRepository<MedStock, Long> {
    List<MedStock> findByMedicineId(Long medicineId, Sort sort);
    List<MedStock> findByMedicineIdAndExpDate(Long medicineId, LocalDate expDate);
    List<MedStock> findByMedicineIdAndAvailableQuantityGreaterThan(Long medicineId, Integer minQuantity, Sort sort);
    List<MedStock> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
} 