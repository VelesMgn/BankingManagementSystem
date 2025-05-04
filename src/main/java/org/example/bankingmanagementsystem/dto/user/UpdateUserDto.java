package org.example.bankingmanagementsystem.dto.user;

import org.example.bankingmanagementsystem.config.ValidationConstants;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.bankingmanagementsystem.model.enums.Role;

@Data
public class UpdateUserDto {
    @NotBlank(message = ValidationConstants.USERNAME_NOT_BLANK_MESSAGE)
    @Size(min = ValidationConstants.USERNAME_MIN_SIZE, max = ValidationConstants.USERNAME_MAX_SIZE,
            message = ValidationConstants.USERNAME_SIZE_MESSAGE)
    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;
}