package org.example.bankingmanagementsystem.service;

import org.example.bankingmanagementsystem.dto.RegistrationResponseDto;
import org.example.bankingmanagementsystem.dto.UserRegistrationDto;
import org.example.bankingmanagementsystem.exception.UserAlreadyExistsException;
import org.example.bankingmanagementsystem.exception.ValidationException;
import org.example.bankingmanagementsystem.service.database.UserService;
import org.example.bankingmanagementsystem.service.impl.RegistrationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RegistrationServiceImplTest {

    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private RegistrationServiceImpl registrationService;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        passwordEncoder = mock(PasswordEncoder.class);
        registrationService = new RegistrationServiceImpl(userService, passwordEncoder);
    }

    @Test
    void registerNewUser_success() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setMail("user@test.com");
        dto.setPassword("validPassword");
        dto.setName("user1");

        when(userService.mailIsPresent(dto.getMail())).thenReturn(false);
        when(userService.nameIsPresent(dto.getName())).thenReturn(false);

        RegistrationResponseDto response = registrationService.registerNewUser(dto);

        assertTrue(response.isResult());
        assertTrue(response.getMessage().contains("created"));
        verify(userService).saveUserInDb(dto, passwordEncoder);
    }

    @Test
    void registerNewUser_duplicateEmail() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setMail("user@test.com");
        dto.setPassword("validPassword");
        dto.setName("user1");

        when(userService.mailIsPresent(dto.getMail())).thenReturn(true);

        UserAlreadyExistsException ex = assertThrows(UserAlreadyExistsException.class,
                () -> registrationService.registerNewUser(dto)
        );
        assertTrue(ex.getMessage().contains("already exists"));
        verify(userService, never()).saveUserInDb(any(), any());
    }

    @Test
    void registerNewUser_duplicateUserName() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setMail("user@test.com");
        dto.setPassword("validPassword");
        dto.setName("user1");

        when(userService.mailIsPresent(dto.getMail())).thenReturn(false);
        when(userService.nameIsPresent(dto.getName())).thenReturn(true);

        UserAlreadyExistsException ex = assertThrows(UserAlreadyExistsException.class,
                () -> registrationService.registerNewUser(dto)
        );
        assertTrue(ex.getMessage().contains("already exists"));
        verify(userService, never()).saveUserInDb(any(), any());
    }

    @Test
    void registerNewUser_passwordEqualsUserName() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setMail("user@test.com");
        dto.setPassword("user1");
        dto.setName("user1");

        when(userService.mailIsPresent(any())).thenReturn(false);
        when(userService.nameIsPresent(any())).thenReturn(false);

        ValidationException ex = assertThrows(ValidationException.class,
                () -> registrationService.registerNewUser(dto)
        );
        assertEquals("Password cannot be your username", ex.getMessage());
    }

    @Test
    void registerNewUser_passwordEqualsEmail() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setMail("user@test.com");
        dto.setPassword("user@test.com");
        dto.setName("user1");

        when(userService.mailIsPresent(any())).thenReturn(false);
        when(userService.nameIsPresent(any())).thenReturn(false);

        ValidationException ex = assertThrows(ValidationException.class,
                () -> registrationService.registerNewUser(dto)
        );
        assertEquals("The password cannot be your email address", ex.getMessage());
    }

    @Test
    void registerNewUser_passwordEqualsEmailPrefix() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setMail("user@test.com");
        dto.setPassword("user");
        dto.setName("name");

        when(userService.mailIsPresent(any())).thenReturn(false);
        when(userService.nameIsPresent(any())).thenReturn(false);

        ValidationException ex = assertThrows(ValidationException.class,
                () -> registrationService.registerNewUser(dto)
        );
        assertEquals("The password cannot be your email address", ex.getMessage());
    }

    @Test
    void registerNewUser_easyPassword() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setMail("user@test.com");
        dto.setPassword("qwerty");
        dto.setName("user1");

        when(userService.mailIsPresent(any())).thenReturn(false);
        when(userService.nameIsPresent(any())).thenReturn(false);

        ValidationException ex = assertThrows(ValidationException.class,
                () -> registrationService.registerNewUser(dto)
        );
        assertTrue(ex.getMessage().toLowerCase().contains("easy password"));
    }

    @Test
    void registerNewUser_usernameContainsAdmin() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setMail("user@test.com");
        dto.setPassword("goodPassword");
        dto.setName("superAdmin1337");

        when(userService.mailIsPresent(any())).thenReturn(false);
        when(userService.nameIsPresent(any())).thenReturn(false);

        ValidationException ex = assertThrows(ValidationException.class,
                () -> registrationService.registerNewUser(dto)
        );
        assertTrue(ex.getMessage().contains("Username cannot contain 'admin'"));
    }
}
