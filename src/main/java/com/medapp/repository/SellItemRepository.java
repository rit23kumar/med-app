package com.medapp.repository;

import com.medapp.entity.SellItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellItemRepository extends JpaRepository<SellItem, Long> {
    boolean existsByMedicineId(Long medicineId);
    List<SellItem> findByMedicineId(Long medicineId);
} 