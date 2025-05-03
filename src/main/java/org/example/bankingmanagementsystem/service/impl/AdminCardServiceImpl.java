package org.example.bankingmanagementsystem.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bankingmanagementsystem.dto.card.BankCardResponseDto;
import org.example.bankingmanagementsystem.model.BankCard;
import org.example.bankingmanagementsystem.model.User;
import org.example.bankingmanagementsystem.model.enums.BankCardStatus;
import org.example.bankingmanagementsystem.service.AdminCardService;
import org.example.bankingmanagementsystem.service.database.CardDatabaseService;
import org.example.bankingmanagementsystem.service.database.UserDatabaseService;
import org.example.bankingmanagementsystem.utils.CardNumberGenerator;
import org.example.bankingmanagementsystem.utils.EncryptionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminCardServiceImpl implements AdminCardService {
    private final CardNumberGenerator cardNumberGenerator;
    private final EncryptionService encryptionService;
    private final CardDatabaseService cardService;
    private final UserDatabaseService userService;


    @Override
    public Page<BankCardResponseDto> getAllCards(int page, int size,
                                                 Long userId, BankCardStatus status) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BankCard> cardsPage;

        if (userId != null && status != null) {
            cardsPage = cardService.findByUserIdAndStatus(userId, status, pageable);
        } else if (userId != null) {
            cardsPage = cardService.findByUserId(userId, pageable);
        } else if (status != null) {
            cardsPage = cardService.findByStatus(status, pageable);
        } else {
            cardsPage = cardService.getAllCards(pageable);
        }

        return cardsPage.map(this::convertToDto);
    }

    @Override
    public BankCardResponseDto createCard(Long id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        LocalDate expiryDate = LocalDate.now().plusYears(10);
        String cardNumber = cardNumberGenerator.generateUniqueCardNumber();
        String encryptedCardNumber = encryptionService.encrypt(cardNumber);
        BigDecimal balance = BigDecimal.ZERO;

        BankCard card = cardService.createBankCard(user, encryptedCardNumber,
                expiryDate, BankCardStatus.ACTIVE, balance);

        log.info("Created new card {} for user {}", encryptedCardNumber, user.getId());
        return convertToDto(card);
    }

    @Override
    public BankCardResponseDto changeCardStatus(Long cardId, BankCardStatus newStatus) {
        BankCard card = cardService.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Card not found"));

        if (card.getStatus() == BankCardStatus.EXPIRED) {
            throw new IllegalStateException("Cannot change status of expired card");
        }

        card.setStatus(newStatus);
        BankCard updatedCard = cardService.save(card);

        return convertToDto(updatedCard);
    }

    @Override
    public void deleteCard(Long cardId) {
        BankCard card = cardService.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Card not found"));

        if (card.getStatus() == BankCardStatus.ACTIVE) {
            throw new IllegalStateException("Cannot delete active card. Block it first.");
        }

        cardService.delete(card);
    }

    public List<BankCardResponseDto> getBankCards(Long userId) {
        List<BankCard> bankCards = cardService.getAllBankCardsByUserId(userId);
        return bankCards.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private BankCardResponseDto convertToDto(BankCard card) {
        String decryptedCardNumber = encryptionService.decrypt(card.getCardNumberEncrypted());
        String maskedCardNumber = encryptionService.maskCardNumber(decryptedCardNumber);

        return BankCardResponseDto.builder()
                .id(card.getId())
                .maskedCardNumber(maskedCardNumber)
                .expiryDate(card.getExpiryDate())
                .status(card.getStatus())
                .balance(card.getBalance())
                .userId(card.getUser().getId())
                .build();
    }
}