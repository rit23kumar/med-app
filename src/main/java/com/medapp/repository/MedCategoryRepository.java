package com.medapp.repository;

import com.medapp.entity.MedCategory;
import org.springframework.data.jpa.repository.JpaRepository;
 
public interface MedCategoryRepository extends JpaRepository<MedCategory, Integer> {
} 