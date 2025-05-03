package org.example.bankingmanagementsystem.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.bankingmanagementsystem.model.BankCard;
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
    private List<BankCard> cards;
    private Role role;
}