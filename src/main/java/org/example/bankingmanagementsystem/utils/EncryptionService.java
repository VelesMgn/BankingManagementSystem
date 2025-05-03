package org.example.bankingmanagementsystem.utils;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {
    private final BasicTextEncryptor textEncryptor;

    public EncryptionService(@Value("${encryption.key}") String encryptionKey) {
        this.textEncryptor = new BasicTextEncryptor();
        this.textEncryptor.setPassword(encryptionKey);
    }

    public String encrypt(String plainText) {
        return textEncryptor.encrypt(plainText);
    }

    public String decrypt(String encryptedText) {
        return textEncryptor.decrypt(encryptedText);
    }

    public String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return cardNumber;
        }
        String lastFour = cardNumber.substring(cardNumber.length() - 4);
        return "**** **** **** " + lastFour;
    }
}