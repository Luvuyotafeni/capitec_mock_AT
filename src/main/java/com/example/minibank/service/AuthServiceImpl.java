package com.example.minibank.service;

import com.example.minibank.dto.AuthResponse;
import com.example.minibank.dto.LoginRequest;
import com.example.minibank.dto.RegisterRequest;
import com.example.minibank.entity.User;
import com.example.minibank.enums.Role;
import com.example.minibank.repository.UserRepository;
import com.example.minibank.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.
        AuthenticationManager;
import org.springframework.security.authentication.
        UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.
        PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public void register(RegisterRequest request) {

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                )
                .role(Role.ROLE_USER)
                .balance(BigDecimal.ZERO)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        String token = jwtService.generateToken(
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        java.util.List.of()
                )
        );

        return new AuthResponse(token);
    }
}