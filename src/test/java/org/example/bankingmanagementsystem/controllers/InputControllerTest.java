package org.example.bankingmanagementsystem.controllers;

import org.example.bankingmanagementsystem.dto.jwt.JwtRequestDto;
import org.example.bankingmanagementsystem.dto.jwt.JwtResponseDto;
import org.example.bankingmanagementsystem.dto.user.RegistrationResponseDto;
import org.example.bankingmanagementsystem.dto.user.UserRegistrationDto;
import org.example.bankingmanagementsystem.service.AuthService;
import org.example.bankingmanagementsystem.service.RegistrationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class InputControllerTest {

    @Mock
    private RegistrationService registrationService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private InputController inputController;

    @Test
    void registration_ShouldReturnCreatedStatus() {
        UserRegistrationDto dto = new UserRegistrationDto(
                "test@example.com", "password123", "Test User");
        RegistrationResponseDto responseDto = new RegistrationResponseDto(true, "User created");

        when(registrationService.registerNewUser(dto)).thenReturn(responseDto);

        ResponseEntity<RegistrationResponseDto> response = inputController.registration(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
        verify(registrationService).registerNewUser(dto);
    }

    @Test
    void authorization_ShouldReturnOkStatus() {
        JwtRequestDto request = new JwtRequestDto("test@example.com", "password123");
        JwtResponseDto responseDto = new JwtResponseDto("generated.token.here");

        when(authService.authenticateUser(request)).thenReturn(responseDto);

        ResponseEntity<?> response = inputController.authorization(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
        verify(authService).authenticateUser(request);
    }
}
