package org.example.bankingmanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bankingmanagementsystem.dto.card.BankCardResponseDto;
import org.example.bankingmanagementsystem.model.BankCard;
import org.example.bankingmanagementsystem.service.AdminCardService;
import org.example.bankingmanagementsystem.service.database.CardDatabaseService;
import org.example.bankingmanagementsystem.service.database.UserDatabaseService;
import org.example.bankingmanagementsystem.utils.EncryptionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminCardServiceImpl implements AdminCardService {
    private final CardDatabaseService cardService;
    private final UserDatabaseService userService;
    private final EncryptionService encryptionService;

    @Override
    public Page<BankCardResponseDto> getAllCards(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BankCard> cardsPage = cardService.getAllCards(pageable);
        return cardsPage.map(this::convertToDto);
    }

    private BankCardResponseDto convertToDto(BankCard card) {
        String decryptedCardNumber = encryptionService.decrypt(card.getCardNumberEncrypted());
        String maskedCardNumber = maskCardNumber(decryptedCardNumber);

        return BankCardResponseDto.builder()
                .id(card.getId())
                .maskedCardNumber(maskedCardNumber)
                .expiryDate(card.getExpiryDate())
                .status(card.getStatus())
                .balance(card.getBalance())
                .userId(card.getUser().getId())
                .build();
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return cardNumber;
        }
        String lastFour = cardNumber.substring(cardNumber.length() - 4);
        return "**** **** **** " + lastFour;
    }
}
