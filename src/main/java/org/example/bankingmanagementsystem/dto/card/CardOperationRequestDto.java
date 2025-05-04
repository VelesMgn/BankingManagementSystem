package org.example.bankingmanagementsystem.dto.card;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.example.bankingmanagementsystem.model.enums.OperationType;
import org.example.bankingmanagementsystem.config.ValidationConstants;

import java.math.BigDecimal;

@Getter
public class CardOperationRequestDto {
    @NotNull(message = ValidationConstants.ID_NOT_NULL_MESSAGE)
    private Long userId;

    @NotNull(message = ValidationConstants.ID_NOT_NULL_MESSAGE)
    private Long cardId;

    @NotNull(message = "Operation type cannot be null")
    private OperationType operationType;

    @NotNull(message = ValidationConstants.AMOUNT_POSITIVE_MESSAGE)
    @DecimalMin(value = ValidationConstants.AMOUNT_MIN_VALUE, message = ValidationConstants.AMOUNT_MIN_MESSAGE)
    private BigDecimal amount;
}
