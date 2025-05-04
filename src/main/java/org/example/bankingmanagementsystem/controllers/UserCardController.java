package org.example.bankingmanagementsystem.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bankingmanagementsystem.dto.card.CardOperationRequestDto;
import org.example.bankingmanagementsystem.dto.card.BankCardResponseDto;
import org.example.bankingmanagementsystem.service.UserCardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.example.bankingmanagementsystem.config.ValidationConstants;

import java.math.BigDecimal;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/cards/user")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
@RequiredArgsConstructor
public class UserCardController {
    private final UserCardService userCardService;

    @PostMapping("/operations")
    public ResponseEntity<BankCardResponseDto> processOperation(@RequestBody @Valid CardOperationRequestDto request) {
        log.info("UserCardService calls processOperation");
        return ResponseEntity.ok(userCardService.processOperation(request));
    }

    @GetMapping("/get-balance")
    public ResponseEntity<Page<BankCardResponseDto>> getCardsBalance(
            @RequestParam(defaultValue = "0")
            @Min(value = ValidationConstants.PAGE_MIN_VALUE, message = ValidationConstants.PAGE_MIN_MESSAGE) int page,

            @RequestParam(defaultValue = "10")
            @Min(value = ValidationConstants.SIZE_MIN_VALUE, message = ValidationConstants.SIZE_MIN_MESSAGE)
            @Max(value = ValidationConstants.SIZE_MAX_VALUE, message = ValidationConstants.SIZE_MAX_MESSAGE) int size,

            @RequestParam
            @NotNull(message = ValidationConstants.ID_NOT_NULL_MESSAGE)
            @Positive(message = ValidationConstants.ID_POSITIVE_MESSAGE) Long userId) {
        log.info("UserCardController calls getCardsBalance with id {}", userId);
        return ResponseEntity.ok(userCardService.getCardsBalance(userId, page, size));
    }

    @PostMapping("/block")
    public ResponseEntity<BankCardResponseDto> blockCard(
            @RequestParam @NotNull(message = ValidationConstants.ID_NOT_NULL_MESSAGE)
            @Positive(message = ValidationConstants.ID_POSITIVE_MESSAGE) Long userId,

            @RequestParam @NotNull(message = ValidationConstants.ID_NOT_NULL_MESSAGE)
            @Positive(message = ValidationConstants.ID_POSITIVE_MESSAGE) Long cardId) {
        log.info("UserCardController calls blockCard with user id {} and card id {}", userId, cardId);
        return ResponseEntity.ok(userCardService.blockCard(userId, cardId));
    }

    @PostMapping("/transfer")
    public ResponseEntity<BankCardResponseDto> transferBetweenCards(
            @RequestParam @NotNull(message = ValidationConstants.ID_NOT_NULL_MESSAGE) Long userId,
            @RequestParam @NotNull(message = "Source card ID cannot be null") Long fromCardId,
            @RequestParam @NotNull(message = "Target card ID cannot be null") Long toCardId,
            @NotNull(message = ValidationConstants.AMOUNT_POSITIVE_MESSAGE)
            @Positive(message = ValidationConstants.AMOUNT_POSITIVE_MESSAGE)
            @DecimalMin(value = ValidationConstants.AMOUNT_MIN_VALUE, message = ValidationConstants.AMOUNT_MIN_MESSAGE)
            @RequestParam BigDecimal amount) {
        log.info("UserCardController calls transferBetweenCards with user id {}", userId);
        return ResponseEntity.ok(userCardService.transferBetweenCards(userId, fromCardId, toCardId, amount));
    }
}