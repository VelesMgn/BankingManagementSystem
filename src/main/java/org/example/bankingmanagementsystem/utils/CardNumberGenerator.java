package org.example.bankingmanagementsystem.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bankingmanagementsystem.service.database.CardDatabaseService;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardNumberGenerator {
    private static final String BIN = "4";
    private static final int LENGTH = 16;
    private static final int MAX_ATTEMPTS = 5;

    private final CardDatabaseService cardDatabaseService;
    private final EncryptionService encryptionService;

    public String generateUniqueCardNumber() {
        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            String cardNumber = generateCardNumber();
            if (!isCardNumberExists(cardNumber)) {
                return cardNumber;
            }
            log.warn("Generated card number already exists, retrying...");
        }
        throw new IllegalStateException("Failed to generate unique card number after " + MAX_ATTEMPTS + " attempts");
    }

    private boolean isCardNumberExists(String cardNumber) {
        String encryptedNumber = encryptionService.encrypt(cardNumber);
        return cardDatabaseService.existsByCardNumberEncrypted(encryptedNumber);
    }

    private String generateCardNumber() {
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder(BIN);

        for (int i = 1; i < LENGTH - 1; i++) {
            cardNumber.append(random.nextInt(10));
        }

        cardNumber.append(calculateLuhnCheckDigit(cardNumber.toString()));
        return cardNumber.toString();
    }

    private int calculateLuhnCheckDigit(String number) {
        int sum = 0;
        boolean alternate = false;
        for (int i = number.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(number.charAt(i));
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit = (digit % 10) + 1;
                }
            }
            sum += digit;
            alternate = !alternate;
        }
        return (10 - (sum % 10)) % 10;
    }
}