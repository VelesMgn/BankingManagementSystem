package org.example.bankingmanagementsystem.controllers;

import io.jsonwebtoken.Jwts;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bankingmanagementsystem.dto.JwtRequestDto;
import org.example.bankingmanagementsystem.dto.JwtResponseDto;
import org.example.bankingmanagementsystem.dto.RegistrationResponseDto;
import org.example.bankingmanagementsystem.dto.UserRegistrationDto;
import org.example.bankingmanagementsystem.exception.AppError;
import org.example.bankingmanagementsystem.service.RegistrationService;
import org.example.bankingmanagementsystem.service.database.UserService;
import org.example.bankingmanagementsystem.utils.JwtTokenUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class InputController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;
    private final RegistrationService registration;
    private final UserService userService;

    @PostMapping("/registration")
    public ResponseEntity<RegistrationResponseDto> registration(@Valid @RequestBody UserRegistrationDto dto) {
        log.info("The controller \"InputController\" calls the RegistrationService.");
        RegistrationResponseDto response = registration.registerNewUser(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/authorization")
    public ResponseEntity<?> authorization(@RequestBody JwtRequestDto request) {
        log.info("The controller \"InputController\" calls the AuthorizationService.");

            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getMail(),
                                request.getPassword()
                        )
                );
            } catch (BadCredentialsException ex) {
                log.error("Auth failed for {}: {}", request.getMail(), ex.getMessage());
                return new ResponseEntity<>
                        (new AppError(HttpStatus.UNAUTHORIZED.value(),
                        "Invalid username or password"), HttpStatus.UNAUTHORIZED);
            }

        UserDetails userDetails = userService.loadUserByUsername(request.getMail());
        String token = jwtTokenUtils.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponseDto(token));
    }
}
