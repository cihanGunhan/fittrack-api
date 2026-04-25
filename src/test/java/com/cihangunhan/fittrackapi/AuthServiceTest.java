package com.cihangunhan.fittrackapi;

import com.cihangunhan.fittrackapi.config.JwtService;
import com.cihangunhan.fittrackapi.entity.Role;
import com.cihangunhan.fittrackapi.entity.User;
import com.cihangunhan.fittrackapi.exception.DuplicateResourceException;
import com.cihangunhan.fittrackapi.repository.UserRepository;
import com.cihangunhan.fittrackapi.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .fullName("Cihan Günhan")
                .email("cihan@fittrack.com")
                .password("encodedPassword")
                .role(Role.USER)
                .build();
    }

    @Test
    @DisplayName("Yeni kullanıcı başarıyla kaydedilir")
    void register_WhenEmailNotExists_ShouldRegisterUser() {
        when(userRepository.existsByEmail(anyString()))
                .thenReturn(false);
        when(passwordEncoder.encode(anyString()))
                .thenReturn("encodedPassword");
        when(userRepository.save(any(User.class)))
                .thenReturn(testUser);
        when(jwtService.generateToken(any(User.class)))
                .thenReturn("mock-jwt-token");

        Map<String, String> result = authService.register(
                "Cihan Günhan",
                "cihan@fittrack.com",
                "fittrack123");

        assertThat(result).containsKey("token");
        assertThat(result.get("token")).isEqualTo("mock-jwt-token");
        assertThat(result.get("email")).isEqualTo("cihan@fittrack.com");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Kayıtlı email ile kayıt olunca hata fırlatılır")
    void register_WhenEmailExists_ShouldThrowException() {
        when(userRepository.existsByEmail(anyString()))
                .thenReturn(true);

        assertThatThrownBy(() -> authService.register(
                "Cihan Günhan",
                "cihan@fittrack.com",
                "fittrack123"))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("email");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Doğru bilgilerle giriş yapılır ve token döner")
    void login_WithValidCredentials_ShouldReturnToken() {
        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(testUser));
        when(jwtService.generateToken(any(User.class)))
                .thenReturn("mock-jwt-token");

        Map<String, String> result = authService.login(
                "cihan@fittrack.com", "fittrack123");

        assertThat(result.get("token")).isEqualTo("mock-jwt-token");
        assertThat(result.get("email"))
                .isEqualTo("cihan@fittrack.com");
        verify(jwtService, times(1)).generateToken(any(User.class));
    }
}