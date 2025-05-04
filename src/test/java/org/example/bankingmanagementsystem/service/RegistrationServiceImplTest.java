package org.example.bankingmanagementsystem.service;

import org.example.bankingmanagementsystem.dto.user.RegistrationResponseDto;
import org.example.bankingmanagementsystem.dto.user.UserRegistrationDto;
import org.example.bankingmanagementsystem.exception.UserAlreadyExistsException;
import org.example.bankingmanagementsystem.exception.ValidationException;
import org.example.bankingmanagementsystem.model.enums.Role;
import org.example.bankingmanagementsystem.service.database.UserDatabaseService;
import org.example.bankingmanagementsystem.service.impl.RegistrationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplTest {

    @Mock
    private UserDatabaseService userService;

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        ReflectionTestUtils.setField(registrationService, "passwordEncoder", passwordEncoder);
    }

    @Test
    void registerNewUser_ShouldSuccessfullyRegisterUser() {
        UserRegistrationDto dto = new UserRegistrationDto(
                "test@example.com", "password123", "Test User");

        when(userService.mailIsPresent(dto.getMail())).thenReturn(false);
        when(userService.nameIsPresent(dto.getName())).thenReturn(false);

        RegistrationResponseDto response = registrationService.registerNewUser(dto);

        assertTrue(response.isResult());
        assertTrue(response.getMessage().contains(dto.getMail()));
        verify(userService).saveUserInDb(
                eq(dto.getMail()),
                eq(dto.getName()),
                anyString(),
                eq(Role.ROLE_USER),
                eq(passwordEncoder));
    }

    @Test
    void registerNewUser_ShouldThrowWhenEmailExists() {
        UserRegistrationDto dto = new UserRegistrationDto(
                "existing@example.com", "password123", "Test User");

        when(userService.mailIsPresent(dto.getMail())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class,
                () -> registrationService.registerNewUser(dto));
    }

    @Test
    void registerNewUser_ShouldThrowWhenUsernameExists() {
        UserRegistrationDto dto = new UserRegistrationDto(
                "test@example.com", "password123", "Existing User");

        when(userService.mailIsPresent(dto.getMail())).thenReturn(false);
        when(userService.nameIsPresent(dto.getName())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class,
                () -> registrationService.registerNewUser(dto));
    }

    @Test
    void registerNewUser_ShouldThrowWhenPasswordEqualsUsername() {
        UserRegistrationDto dto = new UserRegistrationDto(
                "test@example.com", "Test User", "Test User");

        when(userService.mailIsPresent(dto.getMail())).thenReturn(false);
        when(userService.nameIsPresent(dto.getName())).thenReturn(false);

        assertThrows(ValidationException.class,
                () -> registrationService.registerNewUser(dto));
    }

    @Test
    void registerNewUser_ShouldThrowWhenPasswordEqualsEmail() {
        UserRegistrationDto dto = new UserRegistrationDto(
                "test@example.com", "test@example.com", "Test User");

        when(userService.mailIsPresent(dto.getMail())).thenReturn(false);
        when(userService.nameIsPresent(dto.getName())).thenReturn(false);

        assertThrows(ValidationException.class,
                () -> registrationService.registerNewUser(dto));
    }

    @Test
    void registerNewUser_ShouldThrowWhenEasyPassword() {
        UserRegistrationDto dto = new UserRegistrationDto(
                "test@example.com", "123456", "Test User");

        when(userService.mailIsPresent(dto.getMail())).thenReturn(false);
        when(userService.nameIsPresent(dto.getName())).thenReturn(false);

        assertThrows(ValidationException.class,
                () -> registrationService.registerNewUser(dto));
    }

    @Test
    void registerNewUser_ShouldThrowWhenUsernameContainsAdmin() {
        UserRegistrationDto dto = new UserRegistrationDto(
                "test@example.com", "password123", "Test Admin");

        when(userService.mailIsPresent(dto.getMail())).thenReturn(false);
        when(userService.nameIsPresent(dto.getName())).thenReturn(false);

        assertThrows(ValidationException.class,
                () -> registrationService.registerNewUser(dto));
    }
}