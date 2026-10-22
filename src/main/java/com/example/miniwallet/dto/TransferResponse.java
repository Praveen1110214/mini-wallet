package com.example.miniwallet.dto;

import com.example.miniwallet.constant.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class TransferResponse {

    private Long transactionId;
    private Long fromWalletId;
    private Long toWalletId;
    private BigDecimal amount;
    private String status;
}