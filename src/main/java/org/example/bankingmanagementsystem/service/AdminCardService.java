package org.example.bankingmanagementsystem.service;

import org.example.bankingmanagementsystem.dto.card.BankCardResponseDto;
import org.springframework.data.domain.Page;

public interface AdminCardService {
    Page<BankCardResponseDto> getAllCards(int page, int size);
}
