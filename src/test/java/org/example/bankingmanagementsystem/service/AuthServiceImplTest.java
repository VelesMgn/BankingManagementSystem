package org.example.bankingmanagementsystem.service;

import org.example.bankingmanagementsystem.dto.JwtRequestDto;
import org.example.bankingmanagementsystem.dto.JwtResponseDto;
import org.example.bankingmanagementsystem.service.database.UserService;
import org.example.bankingmanagementsystem.service.impl.AuthServiceImpl;
import org.example.bankingmanagementsystem.utils.JwtTokenUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenUtils jwtTokenUtils;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void authenticateUser_Success() {
        JwtRequestDto request = new JwtRequestDto("test@mail.com", "password");
        UserDetails userDetails = new User(
                "test@mail.com",
                "password",
                Collections.singletonList(() -> "ROLE_USER")
        );

        when(userService.loadUserByUsername("test@mail.com")).thenReturn(userDetails);
        when(jwtTokenUtils.generateToken(userDetails)).thenReturn("mocked.jwt.token");

        JwtResponseDto response = authService.authenticateUser(request);

        assertNotNull(response);
        assertEquals("mocked.jwt.token", response.getToken());
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void authenticateUser_Failure_BadCredentials() {
        JwtRequestDto request = new JwtRequestDto("wrong@mail.com", "wrongpass");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(BadCredentialsException.class, () -> authService.authenticateUser(request));
    }
}