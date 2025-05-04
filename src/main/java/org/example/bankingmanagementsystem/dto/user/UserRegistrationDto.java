package org.example.bankingmanagementsystem.dto.user;

import org.example.bankingmanagementsystem.config.ValidationConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationDto {
    @NotBlank(message = ValidationConstants.EMAIL_NOT_BLANK_MESSAGE)
    @Email(message = ValidationConstants.EMAIL_VALID_MESSAGE)
    private String mail;

    @NotBlank(message = ValidationConstants.PASSWORD_NOT_BLANK_MESSAGE)
    @Size(min = ValidationConstants.PASSWORD_MIN_SIZE, max = ValidationConstants.PASSWORD_MAX_SIZE,
            message = ValidationConstants.PASSWORD_SIZE_MESSAGE)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank(message = ValidationConstants.USERNAME_NOT_BLANK_MESSAGE)
    @Size(min = ValidationConstants.USERNAME_MIN_SIZE, max = ValidationConstants.USERNAME_MAX_SIZE,
            message = ValidationConstants.USERNAME_SIZE_MESSAGE)
    private String name;
}