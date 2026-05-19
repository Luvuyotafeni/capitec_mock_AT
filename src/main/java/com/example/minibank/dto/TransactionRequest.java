package com.example.minibank.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequest {

    private BigDecimal amount;
    private String type;
    private String description;
}
