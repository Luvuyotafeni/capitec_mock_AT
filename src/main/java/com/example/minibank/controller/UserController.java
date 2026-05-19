package com.example.minibank.controller;

import com.example.minibank.dto.TransactionRequest;
import com.example.minibank.entity.Transaction;
import com.example.minibank.entity.User;
import com.example.minibank.repository.TransactionRepository;
import com.example.minibank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    @GetMapping("/profile")
    public User getProfile(Authentication authentication) {

        return userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();
    }

    @PostMapping("/transactions")
    public Transaction makeTransaction(
            @RequestBody TransactionRequest request,
            Authentication authentication
    ) {

        User user = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .type(request.getType())
                .description(request.getDescription())
                .transactionDate(LocalDateTime.now())
                .user(user)
                .build();

        return transactionRepository.save(transaction);
    }

    @GetMapping("/transactions")
    public List<Transaction> getTransactions(
            Authentication authentication
    ) {

        User user = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        return transactionRepository
                .findByUserId(user.getId());
    }
}