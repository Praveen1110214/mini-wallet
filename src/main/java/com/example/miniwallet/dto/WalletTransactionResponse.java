package com.example.miniwallet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class WalletTransactionResponse {

    private Long transactionId;
    private String type;
    private BigDecimal amount;
    private String status;
    private LocalDateTime createdAt;
}