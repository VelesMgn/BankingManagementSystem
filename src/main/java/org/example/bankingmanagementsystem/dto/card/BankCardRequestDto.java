package org.example.bankingmanagementsystem.dto.card;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BankCardRequestDto {
    @Future(message = "Expiry date must be in the future")
    private LocalDate expiryDate;

    @NotNull(message = "Balance cannot be null")
    @DecimalMin(value = "0.0", message = "Balance must be positive")
    private BigDecimal balance;

    @NotNull(message = "User ID cannot be null")
    @Min(value = 1, message = "User ID must be positive")
    private Long userId;
}