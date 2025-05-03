package org.example.bankingmanagementsystem.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegistrationResponseDto {
    private boolean result;
    private String message;
}