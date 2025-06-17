package com.medapp.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserResponseDto {
    private Long id;
    private String fullName;
    private String username;
    private Set<String> roles;
    private boolean active;
} 