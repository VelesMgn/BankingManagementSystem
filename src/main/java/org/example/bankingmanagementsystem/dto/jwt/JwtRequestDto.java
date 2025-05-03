package org.example.bankingmanagementsystem.dto.jwt;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtRequestDto {
    @NotBlank(message = "Mail cannot be empty")
    @Email(message = "Mail should be valid")
    private String mail;

    @NotBlank(message = "Password cannot be empty")
    private String password;
}