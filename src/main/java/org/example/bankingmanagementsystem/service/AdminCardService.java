package org.example.bankingmanagementsystem.service;

import org.example.bankingmanagementsystem.dto.card.BankCardResponseDto;
import org.example.bankingmanagementsystem.model.enums.BankCardStatus;
import org.springframework.data.domain.Page;

public interface AdminCardService {
    Page<BankCardResponseDto> getAllCards(int page, int size, Long userId, BankCardStatus status);
    BankCardResponseDto createCard(Long id);
    BankCardResponseDto changeCardStatus(Long cardId, BankCardStatus newStatus);
    void deleteCard(Long cardId);
}