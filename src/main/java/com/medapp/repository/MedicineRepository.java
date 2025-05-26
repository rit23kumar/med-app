package com.medapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.medapp.entity.Medicine;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
} 