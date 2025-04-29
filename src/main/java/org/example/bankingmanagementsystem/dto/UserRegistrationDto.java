package org.example.bankingmanagementsystem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationDto {
    @NotBlank(message = "Mail cannot be empty")
    @Email(message = "Mail should be valid")
    private String mail;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, max = 50, message = "Password should be between 6 and 50 characters")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank(message = "User name cannot be empty")
    @Size(min = 2, max = 50, message = "User name should be between 2 and 50 characters")
    private String name;
}