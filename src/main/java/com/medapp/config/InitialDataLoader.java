package com.medapp.config;

import com.medapp.service.AuthService;
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
} 