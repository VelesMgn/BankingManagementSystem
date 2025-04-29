package org.example.bankingmanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bankingmanagementsystem.dto.RegistrationResponseDto;
import org.example.bankingmanagementsystem.dto.UserRegistrationDto;
import org.example.bankingmanagementsystem.exception.UserAlreadyExistsException;
import org.example.bankingmanagementsystem.exception.ValidationException;
import org.example.bankingmanagementsystem.service.RegistrationService;
import org.example.bankingmanagementsystem.service.database.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final UserService userService;

    public RegistrationResponseDto registerNewUser(UserRegistrationDto dto) {
        validateBusinessRules(dto);
        validatePassword(dto);

        userService.saveUserInDb(dto);

        String message = String.format("User with email: %s created", dto.getEmail());
        log.info(message);
        return new RegistrationResponseDto(true, message);
    }

    private void validatePassword(UserRegistrationDto dto) { // Упрощенная версия
        String password = dto.getPassword();

        if (password.equalsIgnoreCase(dto.getUserName())) {
            String message = "Password cannot be your username";
            log.warn(message);
            throw new ValidationException(message);
        }

        if (password.equalsIgnoreCase(dto.getEmail()) ||
                password.equalsIgnoreCase(dto.getEmail().split("@")[0])) {
            String message = "The password cannot be your email address";
            log.warn(message);
            throw new ValidationException(message);
        }

        if (isEasyPassword(password)) {
            String message = "It's an easy password: " + password;
            log.warn(message);
            throw new ValidationException(message);
        }

    }

    private boolean isEasyPassword(String password) {
        List<String> forbiddenPatterns = List.of(
                "qwerty", "123456", "password",
                "abcdef", "654321", "111111"
        );
        return forbiddenPatterns.contains(password.toLowerCase());
    }

    private void validateBusinessRules(UserRegistrationDto dto) { // Упрощенная версия
        if (userService.mailIsPresent(dto.getEmail())) {
            log.warn("User already exists with email: {}", dto.getEmail());
            throw new UserAlreadyExistsException(dto.getEmail());
        }

        if (userService.nameIsPresent(dto.getUserName())) {
            log.warn("User already exists with name: {}", dto.getUserName());
            throw new UserAlreadyExistsException(dto.getUserName());
        }

        if (dto.getUserName().toLowerCase().contains("admin")) {
            String message = "Username cannot contain 'admin'";
            log.warn(message);
            throw new ValidationException(message);
        }
    }
}
