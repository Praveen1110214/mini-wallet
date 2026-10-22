package com.example.miniwallet.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateWalletRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotNull(message = "Initial balance is required")
    @DecimalMin(value = "0.00", message = "Initial balance cannot be negative")
    @Digits(integer = 17, fraction = 2,
            message = "Initial balance must have at most 17 integer digits and 2 decimal places")
    private BigDecimal initialBalance;
}