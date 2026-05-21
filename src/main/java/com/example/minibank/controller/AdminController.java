package com.example.minibank.controller;

import com.example.minibank.entity.Transaction;
import com.example.minibank.entity.User;
import com.example.minibank.repository.TransactionRepository;
import com.example.minibank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {

        if (!userRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with ID " + id + " not found");
        }

        userRepository.deleteById(id);

        return ResponseEntity.ok(
                "User " + id + " deleted successfully"
        );
    }

    @DeleteMapping("/users/delete-all-users")
    public ResponseEntity<String> deleteAllNormalUsers() {

        List<User> users = userRepository.findAll();

        List<User> normalUsers = users.stream()
                .filter(user -> "ROLE_USER".equals(user.getRole()))
                .toList();

        if (normalUsers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No ROLE_USER accounts found");
        }

        for (User user : normalUsers) {

            List<Transaction> transactions =
                    transactionRepository.findByUserId(user.getId());

            transactionRepository.deleteAll(transactions);

            userRepository.delete(user);
        }

        return ResponseEntity.ok(
                "All ROLE_USER accounts deleted successfully"
        );
    }

    @GetMapping("/transactions")
    public List<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }
}