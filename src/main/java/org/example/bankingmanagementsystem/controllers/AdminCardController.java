package org.example.bankingmanagementsystem.controllers;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bankingmanagementsystem.dto.card.BankCardResponseDto;
import org.example.bankingmanagementsystem.model.enums.BankCardStatus;
import org.example.bankingmanagementsystem.service.AdminCardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.example.bankingmanagementsystem.config.ValidationConstants;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/cards/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequiredArgsConstructor
public class AdminCardController {
    private final AdminCardService cardService;

    @GetMapping("/get-all")
    public ResponseEntity<Page<BankCardResponseDto>> getAllCards(
            @RequestParam(defaultValue = "0")
            @Min(value = ValidationConstants.PAGE_MIN_VALUE, message = ValidationConstants.PAGE_MIN_MESSAGE) int page,

            @RequestParam(defaultValue = "10")
            @Min(value = ValidationConstants.SIZE_MIN_VALUE, message = ValidationConstants.SIZE_MIN_MESSAGE)
            @Max(value = ValidationConstants.SIZE_MAX_VALUE, message = ValidationConstants.SIZE_MAX_MESSAGE) int size,

            @RequestParam(required = false) @Positive(message = ValidationConstants.ID_POSITIVE_MESSAGE) Long userId,

            @RequestParam(required = false) BankCardStatus status) {
        log.info("Admin requested all cards, page {}, size {}, userId {}, status {}",
                page, size, userId, status);
        return ResponseEntity.ok(cardService.getAllCards(page, size, userId, status));
    }

    @PostMapping("/create/{userId}")
    public ResponseEntity<BankCardResponseDto> createCard(
            @PathVariable @NotNull(message = ValidationConstants.ID_NOT_NULL_MESSAGE)
            @Positive(message = ValidationConstants.ID_POSITIVE_MESSAGE) Long userId) {
        log.info("Admin creating new card for user {}", userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(cardService.createCard(userId));
    }

    @PatchMapping("/{cardId}/block")
    public ResponseEntity<BankCardResponseDto> blockCard(
            @PathVariable @NotNull(message = ValidationConstants.ID_NOT_NULL_MESSAGE)
            @Positive(message = ValidationConstants.ID_POSITIVE_MESSAGE) Long cardId) {
        log.info("Admin blocking card {}", cardId);
        return ResponseEntity.ok(cardService.changeCardStatus(cardId, BankCardStatus.BLOCKED));
    }

    @PatchMapping("/{cardId}/activate")
    public ResponseEntity<BankCardResponseDto> activateCard(
                @PathVariable @NotNull(message = ValidationConstants.ID_NOT_NULL_MESSAGE)
                @Positive(message = ValidationConstants.ID_POSITIVE_MESSAGE) Long cardId) {
        log.info("Admin activating card {}", cardId);
        return ResponseEntity.ok(cardService.changeCardStatus(cardId, BankCardStatus.ACTIVE));
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(
            @PathVariable @NotNull(message = ValidationConstants.ID_NOT_NULL_MESSAGE)
            @Positive(message = ValidationConstants.ID_POSITIVE_MESSAGE) Long cardId) {
        log.info("Admin deleting card {}", cardId);
        cardService.deleteCard(cardId);
        return ResponseEntity.noContent().build();
    }
}