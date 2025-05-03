package org.example.bankingmanagementsystem.service.database;

import lombok.RequiredArgsConstructor;
import org.example.bankingmanagementsystem.model.BankCard;
import org.example.bankingmanagementsystem.repository.BankCardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardDatabaseService {
    private final BankCardRepository cardRepository;

    public Page<BankCard> getAllCards(Pageable pageable) {
        return cardRepository.findAll(pageable);
    }
}
