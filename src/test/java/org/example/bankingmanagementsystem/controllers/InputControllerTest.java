package org.example.bankingmanagementsystem.controllers;

import org.example.bankingmanagementsystem.dto.jwt.JwtRequestDto;
import org.example.bankingmanagementsystem.dto.jwt.JwtResponseDto;
import org.example.bankingmanagementsystem.dto.user.RegistrationResponseDto;
import org.example.bankingmanagementsystem.dto.user.UserRegistrationDto;
import org.example.bankingmanagementsystem.exception.GlobalExceptionHandler;
import org.example.bankingmanagementsystem.exception.UserAlreadyExistsException;
import org.example.bankingmanagementsystem.exception.ValidationException;
import org.example.bankingmanagementsystem.service.AuthService;
import org.example.bankingmanagementsystem.service.RegistrationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InputControllerTest {

    @Mock
    private RegistrationService registrationService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private InputController inputController;

    @Test
    void registration_SuccessfulRegistration_ReturnsCreated() {
        UserRegistrationDto request = createValidRequest();
        RegistrationResponseDto serviceResponse = new RegistrationResponseDto(true, "User created");

        when(registrationService.registerNewUser(any(UserRegistrationDto.class)))
                .thenReturn(serviceResponse);

        ResponseEntity<RegistrationResponseDto> response =
                inputController.registration(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(serviceResponse, response.getBody());
        verify(registrationService).registerNewUser(request);
    }

    @Test
    void registration_UserAlreadyExists_ThrowsUserAlreadyExistsException() {
        UserRegistrationDto request = createValidRequest();
        String errorMessage = "User with 'test@example.com' already exists";

        when(registrationService.registerNewUser(any(UserRegistrationDto.class)))
                .thenThrow(new UserAlreadyExistsException(request.getMail()));

        UserAlreadyExistsException exception = assertThrows(
                UserAlreadyExistsException.class,
                () -> inputController.registration(request)
        );

        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void registration_InvalidPassword_ThrowsValidationException() {
        UserRegistrationDto request = createValidRequest();
        request.setPassword("123456");

        when(registrationService.registerNewUser(any(UserRegistrationDto.class)))
                .thenThrow(new ValidationException("Password too simple"));

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> inputController.registration(request)
        );

        assertEquals("Password too simple", exception.getMessage());
    }

    @Test
    void handleValidationException_ReturnsBadRequestWithMessages() {
        MethodArgumentNotValidException ex = createValidationException(
                "Email should be valid",
                "Password cannot be empty"
        );

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<RegistrationResponseDto> response =
                handler.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isResult());
        assertEquals(
                "Email should be valid; Password cannot be empty",
                response.getBody().getMessage()
        );
    }

    @Test
    void handleUserAlreadyExistsException_ReturnsBadRequest() {
        UserAlreadyExistsException ex = new UserAlreadyExistsException("test@example.com");
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<RegistrationResponseDto> response =
                handler.handleBusinessValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isResult());
        assertTrue(response.getBody().getMessage().contains("already exists"));
    }

    @Test
    void handleGenericException_ReturnsInternalServerError() {
        Exception ex = new RuntimeException("Unexpected error");
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<RegistrationResponseDto> response =
                handler.handleOtherExceptions(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertFalse(response.getBody().isResult());
        assertEquals("Internal server error", response.getBody().getMessage());
    }

    private UserRegistrationDto createValidRequest() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setMail("test@example.com");
        dto.setPassword("SecurePass123!");
        dto.setName("test_user");
        return dto;
    }

    private MethodArgumentNotValidException createValidationException(String... messages) {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(
                List.of(
                        new FieldError("object", "email", messages[0]),
                        new FieldError("object", "password", messages[1])
                )
        );
        return new MethodArgumentNotValidException((MethodParameter) null, bindingResult);
    }

    @Test
    void authorization_Success() {
        JwtRequestDto request = new JwtRequestDto("test@mail.com", "password");
        JwtResponseDto mockResponse = new JwtResponseDto("mocked.jwt.token");

        when(authService.authenticateUser(request)).thenReturn(mockResponse);

        ResponseEntity<?> response = inputController.authorization(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void authorization_Failure_BadCredentials() {
        JwtRequestDto request = new JwtRequestDto("wrong@mail.com", "wrongpass");

        when(authService.authenticateUser(request))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(BadCredentialsException.class, () -> inputController.authorization(request));
    }
}