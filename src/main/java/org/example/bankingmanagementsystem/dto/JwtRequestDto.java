package org.example.bankingmanagementsystem.dto;

import lombok.Data;

@Data
public class JwtRequestDto {
    private String mail;
    private String password;
}