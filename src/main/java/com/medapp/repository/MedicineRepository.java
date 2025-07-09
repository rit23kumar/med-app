package com.medapp.repository;

import com.medapp.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    @Query("SELECT m FROM Medicine m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY m.name ASC")
    List<Medicine> findByNameContainingIgnoreCase(@Param("name") String name);

    @Query("SELECT m FROM Medicine m WHERE LOWER(m.name) LIKE LOWER(CONCAT(:name, '%')) ORDER BY m.name ASC")
    List<Medicine> findByNameStartsWithIgnoreCase(@Param("name") String name);

    boolean existsByNameIgnoreCase(String name);

    List<Medicine> findByEnabledTrueOrderByNameAsc();

    List<Medicine> findAllByOrderByNameAsc();
} 