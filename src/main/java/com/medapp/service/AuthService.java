package com.medapp.service;

import com.medapp.dto.LoginRequest;
import com.medapp.dto.LoginResponse;
import com.medapp.dto.UserRegistrationRequest;
import com.medapp.dto.UserResponseDto;
import com.medapp.entity.User;
import com.medapp.exception.UserDisabledException;
import com.medapp.repository.UserRepository;
import com.medapp.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public LoginResponse login(LoginRequest request) {
        // First check if user exists and is active
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isActive()) {
            throw new UserDisabledException("User is disabled");
        }

        // Then authenticate
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        String token = jwtService.generateToken(user.getUsername(), user.getRoles());
        
        return new LoginResponse(
                token,
                user.getUsername(),
                user.getFullName(),
                user.getRoles()
        );
    }

    public User registerUser(UserRegistrationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setFullName(request.getFullName());
        // Ensure roles are uppercase for consistency with Spring Security's ROLE_ prefix logic
        newUser.setRoles(request.getRoles().stream().map(String::toUpperCase).collect(Collectors.toSet()));
        newUser.setActive(true); // New users are active by default

        return userRepository.save(newUser);
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> {
                    UserResponseDto dto = new UserResponseDto();
                    dto.setId(user.getId());
                    dto.setFullName(user.getFullName());
                    dto.setUsername(user.getUsername());
                    dto.setRoles(user.getRoles());
                    dto.setActive(user.isActive());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public User updateUserStatus(Long userId, boolean active) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setActive(active);
        return userRepository.save(user);
    }

    public User changeUserPassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    public void createUsers() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("Tindal@321"));
            admin.setFullName("Administrator");
            Set<String> roles = new HashSet<>();
            roles.add("ADMIN");
            admin.setRoles(roles);
            userRepository.save(admin);
        }

        // Create a default app user if not exists
        if (!userRepository.existsByUsername("user1")) {
            User appUser = new User();
            appUser.setUsername("user1");
            appUser.setPassword(passwordEncoder.encode("User1@234")); // Choose a strong password
            appUser.setFullName("App User");
            Set<String> appRoles = new HashSet<>();
            appRoles.add("USER");
            appUser.setRoles(appRoles);
            userRepository.save(appUser);
        }
    }

    public void createAdminUser() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("Administrator");
            Set<String> roles = new HashSet<>();
            roles.add("ADMIN");
            admin.setRoles(roles);
            userRepository.save(admin);
        }

        // Create a default app user if not exists
        if (!userRepository.existsByUsername("appuser")) {
            User appUser = new User();
            appUser.setUsername("appuser");
            appUser.setPassword(passwordEncoder.encode("appuser123")); // Choose a strong password
            appUser.setFullName("App User");
            Set<String> appRoles = new HashSet<>();
            appRoles.add("USER");
            appUser.setRoles(appRoles);
            userRepository.save(appUser);
        }
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
} 