package org.example.bankingmanagementsystem.repository;

import org.example.bankingmanagementsystem.model.BankCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankCardRepository extends JpaRepository<BankCard, Long> {
}