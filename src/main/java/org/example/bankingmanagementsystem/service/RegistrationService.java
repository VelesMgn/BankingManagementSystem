package org.example.bankingmanagementsystem.service;

import org.example.bankingmanagementsystem.dto.user.RegistrationResponseDto;
import org.example.bankingmanagementsystem.dto.user.UserRegistrationDto;

public interface RegistrationService {
    RegistrationResponseDto registerNewUser(UserRegistrationDto dto);
}