package org.example.bankingmanagementsystem.service.database;

import lombok.RequiredArgsConstructor;
import org.example.bankingmanagementsystem.model.BankCard;
import org.example.bankingmanagementsystem.model.User;
import org.example.bankingmanagementsystem.model.enums.BankCardStatus;
import org.example.bankingmanagementsystem.repository.BankCardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CardDatabaseService {
    private final BankCardRepository cardRepository;

    public Page<BankCard> getAllCards(Pageable pageable) {
        return cardRepository.findAll(pageable);
    }

    public Page<BankCard> findByUserIdAndStatus(Long userId, BankCardStatus status, Pageable pageable) {
        return cardRepository.findByUserIdAndStatus(userId, status, pageable);
    }

    public Page<BankCard> findByUserId(Long userId, Pageable pageable) {
        return cardRepository.findByUserId(userId, pageable);
    }

    public List<BankCard> getAllBankCardsByUserId(Long userId) {
        return cardRepository.findByUserId(userId);
    }

    public Page<BankCard> findByStatus(BankCardStatus status, Pageable pageable) {
        return cardRepository.findByStatus(status, pageable);
    }

    public boolean existsByCardNumberEncrypted(String encryptedNumber) {
        return cardRepository.existsByCardNumberEncrypted(encryptedNumber);
    }

    @Transactional
    public BankCard createBankCard(User user, String cardNumber,
                                   LocalDate expiryDate, BankCardStatus status,
                                   BigDecimal balance) {
        return cardRepository.save(BankCard.builder()
                .cardNumberEncrypted(cardNumber)
                .user(user)
                .expiryDate(expiryDate)
                .status(status)
                .balance(balance)
                .build());
    }

    public Optional<BankCard> findById(Long cardId) {
        return cardRepository.findById(cardId);
    }

    @Transactional
    public BankCard save(BankCard card) {
        return cardRepository.save(card);
    }

    @Transactional
    public void delete(BankCard card) {
        cardRepository.delete(card);
    }

    public Optional<BankCard> findByIdAndUserId(Long userId, Long cardId) {
        return cardRepository.findByIdAndUserId(userId, cardId);
    }
}