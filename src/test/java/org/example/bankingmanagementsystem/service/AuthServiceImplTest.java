package org.example.bankingmanagementsystem.service;

import org.example.bankingmanagementsystem.dto.jwt.JwtRequestDto;
import org.example.bankingmanagementsystem.dto.jwt.JwtResponseDto;
import org.example.bankingmanagementsystem.repository.UserRepository;
import org.example.bankingmanagementsystem.service.database.UserDatabaseService;
import org.example.bankingmanagementsystem.service.impl.AuthServiceImpl;
import org.example.bankingmanagementsystem.utils.JwtTokenUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDatabaseService userService;

    @Mock
    private JwtTokenUtils jwtTokenUtils;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    private final String testEmail = "test@example.com";
    private final String testPassword = "password123";

    @Test
    void authenticateUser_ShouldReturnTokenForValidCredentials() {
        when(userRepository.existsByEmail(testEmail)).thenReturn(true); //<--- добавьте сюда

        JwtRequestDto request = new JwtRequestDto(testEmail, testPassword);
        UserDetails userDetails = User.builder()
                .username(testEmail)
                .password(testPassword)
                .roles("USER")
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));
        when(userService.loadUserByUsername(testEmail)).thenReturn(userDetails);
        when(jwtTokenUtils.generateToken(userDetails)).thenReturn("generated.token.here");

        JwtResponseDto response = authService.authenticateUser(request);

        assertNotNull(response);
        assertEquals("generated.token.here", response.getToken());
        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(testEmail, testPassword));
    }

    @Test
    void authenticateUser_ShouldThrowWhenUserNotFound() {
        JwtRequestDto request = new JwtRequestDto("nonexistent@example.com", testPassword);
        when(userRepository.existsByEmail("nonexistent@example.com")).thenReturn(false);

        assertThrows(UsernameNotFoundException.class,
                () -> authService.authenticateUser(request));
    }

    @Test
    void authenticateUser_ShouldThrowForInvalidPassword() {
        when(userRepository.existsByEmail(testEmail)).thenReturn(true); //<--- добавьте сюда

        JwtRequestDto request = new JwtRequestDto(testEmail, "wrongpassword");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class,
                () -> authService.authenticateUser(request));
    }
}