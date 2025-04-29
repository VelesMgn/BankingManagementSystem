package org.example.bankingmanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bankingmanagementsystem.dto.JwtRequestDto;
import org.example.bankingmanagementsystem.dto.JwtResponseDto;
import org.example.bankingmanagementsystem.service.AuthService;
import org.example.bankingmanagementsystem.service.database.UserService;
import org.example.bankingmanagementsystem.utils.JwtTokenUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    public JwtResponseDto authenticateUser(JwtRequestDto request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getMail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            log.error("Auth failed for {}: {}", request.getMail(), ex.getMessage());
            throw new BadCredentialsException("Invalid username or password");
        }

        UserDetails userDetails = userService.loadUserByUsername(request.getMail());
        String token = jwtTokenUtils.generateToken(userDetails);

        return new JwtResponseDto(token);
    }
}