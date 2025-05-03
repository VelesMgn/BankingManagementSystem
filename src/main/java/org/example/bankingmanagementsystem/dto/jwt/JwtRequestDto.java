package org.example.bankingmanagementsystem.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtRequestDto {
    private String mail;
    private String password;
}