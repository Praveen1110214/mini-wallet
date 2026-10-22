package com.example.miniwallet.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferRequest {

    @NotNull(message = "Sender wallet ID is required")
    @Positive(message = "Sender wallet ID must be greater than zero")
    private Long fromWalletId;

    @NotNull(message = "Receiver wallet ID is required")
    @Positive(message = "Receiver wallet ID must be greater than zero")
    private Long toWalletId;

    @NotNull(message = "Transfer amount is required")
    @DecimalMin(
            value = "0.00",
            inclusive = false,
            message = "Transfer amount must be greater than zero"
    )
    @Digits(
            integer = 17,
            fraction = 2,
            message = "Transfer amount must have at most 17 integer digits and 2 decimal places"
    )
    private BigDecimal amount;
}