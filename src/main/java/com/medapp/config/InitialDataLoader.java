package com.medapp.config;

import com.medapp.service.AuthService;
import com.medapp.entity.MedCategory;
import com.medapp.repository.MedCategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitialDataLoader {

    @Bean
    public CommandLineRunner initData(AuthService authService) {
        return args -> {
            authService.createUsers();
        };
    }

    @Bean
    public CommandLineRunner loadMedCategories(MedCategoryRepository medCategoryRepository) {
        return args -> {
            if (medCategoryRepository.count() == 0) {
                String[] categories = {
                    "Tablet",
                    "Capsule",
                    "Syrup",
                    "Suspension",
                    "Injection",
                    "Ointment",
                    "Powder",
                    "Drops",
                    "Inhaler",
                    "Spray",
                    "Others"
                };
                for (String name : categories) {
                    MedCategory cat = new MedCategory();
                    cat.setName(name);
                    medCategoryRepository.save(cat);
                }
            }
        };
    }
} 