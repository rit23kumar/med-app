package com.medapp.service;

import com.medapp.dto.MedicineDto;
import com.medapp.dto.MedicineWithStockDto;
import com.medapp.entity.Medicine;
import com.medapp.repository.MedicineRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MedicineService {

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private MedStockService medStockService;

    public MedicineDto addMedicine(MedicineDto medicineDto) {
        Medicine medicine = new Medicine();
        BeanUtils.copyProperties(medicineDto, medicine);
        medicine = medicineRepository.save(medicine);
        BeanUtils.copyProperties(medicine, medicineDto);
        return medicineDto;
    }

    @Transactional
    public List<MedicineDto> addMedicines(List<MedicineDto> medicineDtos) {
        return medicineDtos.stream()
            .map(this::addMedicine)
            .toList();
    }

    @Transactional
    public MedicineWithStockDto addMedicineWithStock(MedicineWithStockDto dto) {
        // First save the medicine
        MedicineDto savedMedicine = addMedicine(dto.getMedicine());
        
        // Then add the stock
        medStockService.addStock(savedMedicine.getId(), dto.getStock());
        
        // Update the DTO with saved data
        dto.getMedicine().setId(savedMedicine.getId());
        return dto;
    }

    public Page<Medicine> getAllMedicines(int page, int size) {
        return medicineRepository.findAll(PageRequest.of(page, size));
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
} 