package com.medapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/public/hello")
    public String publicEndpoint() {
        return "Hello! This is a public endpoint.";
    }

    @GetMapping("/api/private/hello")
    public String privateEndpoint() {
        return "Hello! This is a private endpoint.";
    }
} 