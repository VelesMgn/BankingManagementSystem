package org.example.bankingmanagementsystem.service.utils;

import org.example.bankingmanagementsystem.dto.card.BankCardResponseDto;
import org.example.bankingmanagementsystem.model.BankCard;
import org.example.bankingmanagementsystem.utils.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractConverter {
    @Autowired
    private EncryptionService encryptionService;

    public BankCardResponseDto convertToDto(BankCard card) {
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

    public String getEncryptedNumber(String cardNumber) {
        return encryptionService.encrypt(cardNumber);
    }
}