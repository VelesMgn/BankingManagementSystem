package org.example.bankingmanagementsystem.service;

import org.example.bankingmanagementsystem.dto.jwt.JwtRequestDto;
import org.example.bankingmanagementsystem.dto.jwt.JwtResponseDto;

public interface AuthService {
    JwtResponseDto authenticateUser(JwtRequestDto request);
}