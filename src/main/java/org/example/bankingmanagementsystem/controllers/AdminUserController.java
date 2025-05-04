package org.example.bankingmanagementsystem.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bankingmanagementsystem.dto.user.UpdateUserDto;
import org.example.bankingmanagementsystem.dto.user.UserRegistrationDto;
import org.example.bankingmanagementsystem.dto.user.UserResponseDto;
import org.example.bankingmanagementsystem.service.AdminUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.example.bankingmanagementsystem.config.ValidationConstants;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequiredArgsConstructor
public class AdminUserController {
    private final AdminUserService adminUserService;

    @GetMapping("/get-all")
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(
            @RequestParam(defaultValue = "0")
            @Min(value = ValidationConstants.PAGE_MIN_VALUE, message = ValidationConstants.PAGE_MIN_MESSAGE) int page,

            @RequestParam(defaultValue = "10")
            @Min(value = ValidationConstants.SIZE_MIN_VALUE, message = ValidationConstants.SIZE_MIN_MESSAGE)
            @Max(value = ValidationConstants.SIZE_MAX_VALUE, message = ValidationConstants.SIZE_MAX_MESSAGE) int size) {
        log.info("Admin requested all users");
        return ResponseEntity.ok(adminUserService.getAllUsers(page, size));
    }

    @GetMapping("/{id}")
    public UserResponseDto getUserById(
            @PathVariable
            @NotNull(message = ValidationConstants.ID_NOT_NULL_MESSAGE)
            @Positive(message = ValidationConstants.ID_POSITIVE_MESSAGE) Long id) {
        log.info("Admin requested user with id: {}", id);
        return adminUserService.getUserById(id);
    }

    @GetMapping("/by-email")
    public UserResponseDto getUserByEmail(
            @RequestParam
            @NotBlank(message = ValidationConstants.EMAIL_NOT_BLANK_MESSAGE)
            @Email(message = ValidationConstants.EMAIL_VALID_MESSAGE)
            @Size(max = ValidationConstants.EMAIL_MAX_SIZE, message = ValidationConstants.EMAIL_SIZE_MESSAGE)
            String email) {
        log.info("Admin requested user with email: {}", email);
        return adminUserService.getUserByEmail(email);
    }

    @GetMapping("/by-username")
    public UserResponseDto getUserByUsername(
            @RequestParam
            @NotBlank(message = ValidationConstants.USERNAME_NOT_BLANK_MESSAGE)
            @Size(min = ValidationConstants.USERNAME_MIN_SIZE,
                    max = ValidationConstants.USERNAME_MAX_SIZE,
                    message = ValidationConstants.USERNAME_SIZE_MESSAGE)
            String username) {
        log.info("Admin requested user with username: {}", username);
        return adminUserService.getUserByUsername(username);
    }

    @PostMapping("/create-admin")
    public ResponseEntity<UserResponseDto> createAdmin(@Valid @RequestBody UserRegistrationDto dto) {
        log.info("Admin creating new admin user");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(adminUserService.createAdmin(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable
            @NotNull(message = ValidationConstants.ID_NOT_NULL_MESSAGE)
            @Positive(message = ValidationConstants.ID_POSITIVE_MESSAGE) Long id,

            @Valid @RequestBody UpdateUserDto dto) {
        log.info("Admin updating user with id: {}", id);
        return ResponseEntity.ok(adminUserService.updateUser(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable
            @NotNull(message = ValidationConstants.ID_NOT_NULL_MESSAGE)
            @Positive(message = ValidationConstants.ID_POSITIVE_MESSAGE) Long id) {
        log.info("Admin deleting user with id: {}", id);
        adminUserService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}