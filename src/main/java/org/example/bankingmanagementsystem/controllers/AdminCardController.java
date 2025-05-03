package org.example.bankingmanagementsystem.controllers;

import jakarta.validation.constraints.Min;
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
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) BankCardStatus status) {
        log.info("Admin requested all cards, page {}, size {}, userId {}, status {}",
                page, size, userId, status);
        return ResponseEntity.ok(cardService.getAllCards(page, size, userId, status));
    }

    @PostMapping("/create/{id}")
    public ResponseEntity<BankCardResponseDto> createCard(@PathVariable
                                                              @Min(value = 1, message = "Id must be positive")
                                                              Long id) {
        log.info("Admin creating new card for user {}", id);
        return ResponseEntity.status(HttpStatus.CREATED).body(cardService.createCard(id));
    }

    @PatchMapping("/{cardId}/block")
    public ResponseEntity<BankCardResponseDto> blockCard(@PathVariable Long cardId) {
        log.info("Admin blocking card {}", cardId);
        return ResponseEntity.ok(cardService.changeCardStatus(cardId, BankCardStatus.BLOCKED));
    }

        @PatchMapping("/{cardId}/activate")
    public ResponseEntity<BankCardResponseDto> activateCard(@PathVariable Long cardId) {
        log.info("Admin activating card {}", cardId);
        return ResponseEntity.ok(cardService.changeCardStatus(cardId, BankCardStatus.ACTIVE));
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long cardId) {
        log.info("Admin deleting card {}", cardId);
        cardService.deleteCard(cardId);
        return ResponseEntity.noContent().build();
    }
}