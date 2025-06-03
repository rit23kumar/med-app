package com.medapp.repository;

import com.medapp.entity.Sell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SellRepository extends JpaRepository<Sell, Long> {
    List<Sell> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
} 