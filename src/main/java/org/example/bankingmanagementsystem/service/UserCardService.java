package org.example.bankingmanagementsystem.service;

import org.example.bankingmanagementsystem.dto.card.BankCardResponseDto;
import org.example.bankingmanagementsystem.dto.card.CardOperationRequestDto;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

public interface UserCardService {
    BankCardResponseDto processOperation(CardOperationRequestDto request);
    Page<BankCardResponseDto> getCardsBalance(Long userId, int page, int size);
    BankCardResponseDto blockCard(Long userId, Long cardId);
    BankCardResponseDto transferBetweenCards(Long userId, Long fromCardId, Long toCardId, BigDecimal amount);
}