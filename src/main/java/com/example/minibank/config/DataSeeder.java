package com.example.minibank.config;

import com.example.minibank.entity.User;
import com.example.minibank.enums.Role;
import com.example.minibank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner seedAdmin(
            UserRepository userRepository
    ) {

        return args -> {

            if(userRepository
                    .findByEmail("admin@bank.com")
                    .isEmpty()) {

                User admin = User.builder()
                        .fullName("Admin")
                        .email("admin@bank.com")
                        .password(
                                passwordEncoder.encode("Admin123")
                        )
                        .role(Role.ROLE_ADMIN)
                        .balance(BigDecimal.ZERO)
                        .createdAt(LocalDateTime.now())
                        .build();

                userRepository.save(admin);
            }
        };
    }
}
