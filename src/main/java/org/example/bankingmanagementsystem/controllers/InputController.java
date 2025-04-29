package org.example.bankingmanagementsystem.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bankingmanagementsystem.dto.JwtRequestDto;
import org.example.bankingmanagementsystem.dto.RegistrationResponseDto;
import org.example.bankingmanagementsystem.dto.UserRegistrationDto;
import org.example.bankingmanagementsystem.service.AuthService;
import org.example.bankingmanagementsystem.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class InputController {
    private final RegistrationService registration;
    private final AuthService authService;

    @PostMapping("/registration")
    public ResponseEntity<RegistrationResponseDto> registration(@Valid @RequestBody UserRegistrationDto dto) {
        log.info("The controller \"InputController\" calls the RegistrationService.");
        RegistrationResponseDto response = registration.registerNewUser(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/authorization")
    public ResponseEntity<?> authorization(@RequestBody JwtRequestDto request) {
        log.info("The controller \"InputController\" calls the AuthorizationService.");
        return ResponseEntity.ok(authService.authenticateUser(request));
    }
}