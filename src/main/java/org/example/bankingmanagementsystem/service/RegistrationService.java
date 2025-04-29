package org.example.bankingmanagementsystem.service;

import org.example.bankingmanagementsystem.dto.RegistrationResponseDto;
import org.example.bankingmanagementsystem.dto.UserRegistrationDto;

public interface RegistrationService {
    RegistrationResponseDto registerNewUser(UserRegistrationDto dto);
}