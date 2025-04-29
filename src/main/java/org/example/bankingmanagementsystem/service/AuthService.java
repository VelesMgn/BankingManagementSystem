package org.example.bankingmanagementsystem.service;

import org.example.bankingmanagementsystem.dto.JwtRequestDto;
import org.example.bankingmanagementsystem.dto.JwtResponseDto;

public interface AuthService {
    JwtResponseDto authenticateUser(JwtRequestDto request);
}