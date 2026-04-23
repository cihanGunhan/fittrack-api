package com.cihangunhan.fittrackapi.controller;

import com.cihangunhan.fittrackapi.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(
            @Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.register(
                        request.getFullName(),
                        request.getEmail(),
                        request.getPassword()));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(
                authService.login(
                        request.getEmail(),
                        request.getPassword()));
    }

    @Getter
    @Setter
    static class RegisterRequest {
        @NotBlank(message = "Ad soyad boş olamaz")
        private String fullName;

        @Email(message = "Geçerli bir email girin")
        @NotBlank(message = "Email boş olamaz")
        private String email;

        @Size(min = 6, message = "Şifre en az 6 karakter olmalı")
        @NotBlank(message = "Şifre boş olamaz")
        private String password;
    }

    @Getter
    @Setter
    static class LoginRequest {
        @Email
        @NotBlank
        private String email;

        @NotBlank
        private String password;
    }
}