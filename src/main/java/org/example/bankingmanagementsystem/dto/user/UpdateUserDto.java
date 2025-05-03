package org.example.bankingmanagementsystem.dto.user;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.bankingmanagementsystem.model.enums.Role;

@Data
public class UpdateUserDto {
    @NotBlank(message = "Name cannot be empty")
    @Size(min = 2, max = 50, message = "User name should be between 2 and 50 characters")
    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;
}