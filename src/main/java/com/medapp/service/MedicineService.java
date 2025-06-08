package com.medapp.service;

import com.medapp.dto.MedicineDto;
import com.medapp.dto.MedicineWithStockDto;
import com.medapp.dto.BatchMedicineResponse;
import com.medapp.entity.Medicine;
import com.medapp.repository.MedicineRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class MedicineService {

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private MedStockService medStockService;

    public MedicineDto addMedicine(MedicineDto medicineDto) {
        // Check if medicine with same name already exists
        if (medicineRepository.existsByNameIgnoreCase(medicineDto.getName())) {
            throw new IllegalArgumentException("A medicine with this name already exists");
        }

        try {
            Medicine medicine = new Medicine();
            BeanUtils.copyProperties(medicineDto, medicine);
            medicine = medicineRepository.save(medicine);
            BeanUtils.copyProperties(medicine, medicineDto);
            return medicineDto;
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Failed to save medicine. Please try again.");
        }
    }

    @Transactional
    public BatchMedicineResponse addMedicines(List<MedicineDto> medicineDtos) {
        BatchMedicineResponse response = new BatchMedicineResponse();
        List<MedicineDto> successfulMedicines = new ArrayList<>();
        List<BatchMedicineResponse.FailedMedicine> failedMedicines = new ArrayList<>();

        for (MedicineDto medicineDto : medicineDtos) {
            try {
                // Check if medicine with same name already exists
                if (medicineRepository.existsByNameIgnoreCase(medicineDto.getName())) {
                    failedMedicines.add(new BatchMedicineResponse.FailedMedicine(
                        medicineDto.getName(),
                        "Medicine with this name already exists"
                    ));
                    continue;
                }

                Medicine medicine = new Medicine();
                BeanUtils.copyProperties(medicineDto, medicine);
                medicine = medicineRepository.save(medicine);
                
                MedicineDto savedDto = new MedicineDto();
                BeanUtils.copyProperties(medicine, savedDto);
                successfulMedicines.add(savedDto);
            } catch (Exception e) {
                failedMedicines.add(new BatchMedicineResponse.FailedMedicine(
                    medicineDto.getName(),
                    "Failed to save medicine: " + e.getMessage()
                ));
            }
        }

        response.setSuccessfulMedicines(successfulMedicines);
        response.setFailedMedicines(failedMedicines);
        response.setTotalSuccess(successfulMedicines.size());
        response.setTotalFailed(failedMedicines.size());

        return response;
    }

    @Transactional
    public MedicineWithStockDto addMedicineWithStock(MedicineWithStockDto dto) {
        Medicine medicine;
        if (dto.getMedicine().getId() != null) {
            // If medicine ID exists, this is an update to existing medicine's stock
            medicine = medicineRepository.findById(dto.getMedicine().getId())
                .orElseThrow(() -> new EntityNotFoundException("Medicine not found with id: " + dto.getMedicine().getId()));
        } else {
            // If no medicine ID, this is a new medicine with stock
            MedicineDto savedMedicine = addMedicine(dto.getMedicine());
            medicine = new Medicine();
            BeanUtils.copyProperties(savedMedicine, medicine);
        }
        
        // Add or update the stock
        medStockService.addStock(medicine.getId(), dto.getStock());
        
        return dto;
    }

    public Page<Medicine> getAllMedicines(int page, int size) {
        return medicineRepository.findAll(PageRequest.of(page, size, org.springframework.data.domain.Sort.by("name").ascending()));
    }

    public MedicineDto getMedicineById(Long id) {
        Optional<Medicine> medicineOpt = medicineRepository.findById(id);
        if (medicineOpt.isPresent()) {
            MedicineDto dto = new MedicineDto();
            BeanUtils.copyProperties(medicineOpt.get(), dto);
            return dto;
        }
        return null;
    }

    public List<Medicine> searchMedicinesByName(String name, String searchType) {
        if ("startsWith".equals(searchType)) {
            return medicineRepository.findByNameStartsWithIgnoreCase(name);
        }
        return medicineRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Medicine> getAllMedicinesUnpaged() {
        return medicineRepository.findAll();
    }
} 