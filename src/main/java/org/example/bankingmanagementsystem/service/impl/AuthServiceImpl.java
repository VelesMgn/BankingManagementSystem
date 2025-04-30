package org.example.bankingmanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bankingmanagementsystem.dto.JwtRequestDto;
import org.example.bankingmanagementsystem.dto.JwtResponseDto;
import org.example.bankingmanagementsystem.repository.UserRepository;
import org.example.bankingmanagementsystem.service.AuthService;
import org.example.bankingmanagementsystem.service.database.UserDatabaseService;
import org.example.bankingmanagementsystem.utils.JwtTokenUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserDatabaseService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserRepository userRepository;

    @Override
    public JwtResponseDto authenticateUser(JwtRequestDto request) {
        if (!userRepository.existsByEmail(request.getMail())) {
            log.error("User not found with email: {}", request.getMail());
            throw new UsernameNotFoundException("The user was not found");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getMail(),
                            request.getPassword()
                    )
            );

            UserDetails userDetails = userService.loadUserByUsername(request.getMail());
            String token = jwtTokenUtils.generateToken(userDetails);

            return new JwtResponseDto(token);

        } catch (BadCredentialsException ex) {
            log.error("Auth failed for {}: {}", request.getMail(), ex.getMessage());
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}