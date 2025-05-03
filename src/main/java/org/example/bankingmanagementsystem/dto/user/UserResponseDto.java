package org.example.bankingmanagementsystem.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.bankingmanagementsystem.dto.card.BankCardResponseDto;
import org.example.bankingmanagementsystem.model.enums.Role;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String email;
    private String userName;
    private List<BankCardResponseDto> bankCards;
    private Role role;
}