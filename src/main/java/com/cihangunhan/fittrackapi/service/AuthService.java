package com.cihangunhan.fittrackapi.service;

import com.cihangunhan.fittrackapi.config.JwtService;
import com.cihangunhan.fittrackapi.entity.Role;
import com.cihangunhan.fittrackapi.entity.User;
import com.cihangunhan.fittrackapi.exception.DuplicateResourceException;
import com.cihangunhan.fittrackapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public Map<String, String> register(String fullName,
                                        String email,
                                        String password) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException(
                    "Bu email zaten kayıtlı: " + email);
        }

        User user = User.builder()
                .fullName(fullName)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("email", email);
        response.put("fullName", fullName);
        return response;
    }

    public Map<String, String> login(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        String token = jwtService.generateToken(user);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("email", email);
        response.put("fullName", user.getFullName());
        return response;
    }
}