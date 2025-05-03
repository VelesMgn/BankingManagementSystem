package org.example.bankingmanagementsystem.repository;

import org.example.bankingmanagementsystem.model.BankCard;
import org.example.bankingmanagementsystem.model.enums.BankCardStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankCardRepository extends JpaRepository<BankCard, Long> {
    Page<BankCard> findByUserId(Long userId, Pageable pageable);
    List<BankCard> findByUserId(Long userId);
    Page<BankCard> findByStatus(BankCardStatus status, Pageable pageable);
    Page<BankCard> findByUserIdAndStatus(Long userId, BankCardStatus status, Pageable pageable);

    boolean existsByCardNumberEncrypted(String cardNumberEncrypted);
}