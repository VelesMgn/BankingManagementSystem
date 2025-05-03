package org.example.bankingmanagementsystem.dto.card;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.bankingmanagementsystem.model.enums.BankCardStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankCardResponseDto {
    private Long id;
    private String maskedCardNumber;
    private Long userId;
    private LocalDate expiryDate;
    private BankCardStatus status;
    private BigDecimal balance;
}
