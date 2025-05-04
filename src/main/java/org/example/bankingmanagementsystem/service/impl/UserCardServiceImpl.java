package org.example.bankingmanagementsystem.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.bankingmanagementsystem.dto.card.BankCardResponseDto;
import org.example.bankingmanagementsystem.dto.card.CardOperationRequestDto;
import org.example.bankingmanagementsystem.exception.InsufficientFundsException;
import org.example.bankingmanagementsystem.model.BankCard;
import org.example.bankingmanagementsystem.model.enums.BankCardStatus;
import org.example.bankingmanagementsystem.service.UserCardService;
import org.example.bankingmanagementsystem.service.database.CardDatabaseService;
import org.example.bankingmanagementsystem.service.utils.AbstractConverter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserCardServiceImpl extends AbstractConverter implements UserCardService {
    private final CardDatabaseService cardService;

    @Override
    @Transactional
    public BankCardResponseDto processOperation(CardOperationRequestDto request) {
        BankCard card = getCardIfBelongsToUser(request.getUserId(), request.getCardId());

        if (card.getStatus() == BankCardStatus.EXPIRED || card.getStatus() == BankCardStatus.BLOCKED) {
            throw new IllegalStateException("Bank card is not available");
        }

        BigDecimal newBalance = changeBalance(request, card);
        card.setBalance(newBalance);

        BankCard updatedCard = cardService.save(card);

        return convertToDto(updatedCard);
    }

    @Override
    public Page<BankCardResponseDto> getCardsBalance(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BankCard> cardsPage;

        cardsPage = cardService.findByUserId(userId, pageable);
        if (cardsPage.isEmpty()) throw new EntityNotFoundException("Card not found");

        return cardsPage.map(this::convertToDto);
    }

    @Override
    @Transactional
    public BankCardResponseDto blockCard(Long userId, Long cardId) {
        BankCard card = getCardIfBelongsToUser(userId, cardId);

        if (card.getStatus() == BankCardStatus.BLOCKED) {
            throw new IllegalStateException("Card is already blocked");
        }

        card.setStatus(BankCardStatus.BLOCKED);
        BankCard savedCard = cardService.save(card);

        return convertToDto(savedCard);
    }

    @Override
    @Transactional
    public BankCardResponseDto transferBetweenCards(Long userId, Long fromCardId, Long toCardId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        BankCard fromCard = getCardIfBelongsToUser(userId, fromCardId);
        BankCard toCard = getCardIfBelongsToUser(userId, toCardId);

        if (fromCard.getId().equals(toCard.getId())) {
            throw new IllegalArgumentException("Cannot transfer to the same card");
        }

        if (fromCard.getStatus() != BankCardStatus.ACTIVE || toCard.getStatus() != BankCardStatus.ACTIVE) {
            throw new IllegalStateException("Both cards must be active for transfer");
        }

        if (fromCard.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds");
        }

        fromCard.setBalance(fromCard.getBalance().subtract(amount));
        toCard.setBalance(toCard.getBalance().add(amount));

        cardService.save(fromCard);
        cardService.save(toCard);

        return convertToDto(fromCard);
    }

    private BankCard getCardIfBelongsToUser(Long userId, Long cardId) {
        return cardService.findByIdAndUserId(cardId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Card not found or doesn't belong to user"));
    }

    private BigDecimal changeBalance(CardOperationRequestDto request, BankCard card) {
        return switch (request.getOperationType()) {
            case DEPOSIT -> card.getBalance().add(request.getAmount());
            case WITHDRAW -> {
                if (card.getBalance().compareTo(request.getAmount()) < 0) {
                    throw new InsufficientFundsException(request.getCardId());
                }
                yield card.getBalance().subtract(request.getAmount());
            }
        };
    }
}