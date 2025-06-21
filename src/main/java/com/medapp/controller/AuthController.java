package com.medapp.controller;

import com.medapp.dto.ErrorResponse;
import com.medapp.dto.LoginRequest;
import com.medapp.dto.LoginResponse;
import com.medapp.dto.UserRegistrationRequest;
import com.medapp.dto.UserResponseDto;
import com.medapp.entity.User;
import com.medapp.exception.UserDisabledException;
import com.medapp.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {
    "http://localhost:3000", "http://med-app.uk"
})
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (UserDisabledException e) {
            return ResponseEntity.ok(new ErrorResponse(e.getMessage(), "USER_DISABLED"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Invalid username or password", "INVALID_CREDENTIALS"));
        }
    }

    @GetMapping("/current-user")
    public ResponseEntity<LoginResponse> getCurrentUser(Authentication authentication) {
        try {
            LoginResponse response = authService.getCurrentUser(authentication);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserRegistrationRequest request) {
        try {
            User newUser = authService.registerUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Return null body for bad requests
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(authService.getAllUsers());
    }

    @PutMapping("/users/{id}/status")
    public ResponseEntity<User> updateUserStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> statusUpdate) {
        try {
            Boolean active = statusUpdate.get("active");
            if (active == null) {
                return ResponseEntity.badRequest().body(null);
            }
            User updatedUser = authService.updateUserStatus(id, active);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/users/{id}/password")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<User> changeUserPassword(@PathVariable Long id, @RequestBody Map<String, String> passwordUpdate) {
        try {
            String newPassword = passwordUpdate.get("newPassword");
            if (newPassword == null || newPassword.isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }
            User updatedUser = authService.changeUserPassword(id, newPassword);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        authService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
} 