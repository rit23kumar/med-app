package com.medapp.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserRegistrationRequest {
    private String fullName;
    private String username;
    private String password;
    private Set<String> roles;
} 