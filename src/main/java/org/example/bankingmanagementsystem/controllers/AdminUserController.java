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

@Slf4j
@Validated
@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequiredArgsConstructor
public class AdminUserController {
    private final AdminUserService adminUserService;

    @GetMapping("/get-all")
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(@RequestParam(defaultValue = "0")
                                                             @Min(value = 0,
                                                                     message = "Page index must be 0 or greater")
                                                             int page,
                                                             @RequestParam(defaultValue = "10")
                                                             @Min(value = 1, message = "Size must be at least 1")
                                                             @Max(value = 100, message = "Size must be less than 100")
                                                             int size) {
        log.info("Admin requested all users");
        return ResponseEntity.ok(adminUserService.getAllUsers(page, size));
    }

    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable
                                           @Min(value = 1, message = "Id must be positive")
                                           @NotNull(message = "Id not be empty")
                                           Long id) {
        log.info("Admin requested user with id: {}", id);
        return adminUserService.getUserById(id);
    }

    @GetMapping("/by-email")
    public UserResponseDto getUserByEmail(@RequestParam @NotBlank(message = "Email cannot be empty")
                                          @Email(message = "Email should be valid") String email) {
        log.info("Admin requested user with email: {}", email);
        return adminUserService.getUserByEmail(email);
    }

    @GetMapping("/by-username")
    public UserResponseDto getUserByUsername(@RequestParam @NotBlank(message = "Username cannot be empty")
                                             @Size(min = 2, max = 50,
                                                     message = "Username must be between 2 and 50 characters")
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
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable
                                                          @Min(value = 1, message = "Id must be positive") Long id,
                                                      @Valid @RequestBody UpdateUserDto dto) {
        log.info("Admin updating user with id: {}", id);
        return ResponseEntity.ok(adminUserService.updateUser(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable @Min(value = 1, message = "Id must be positive") long id) {
        log.info("Admin deleting user with id: {}", id);
        adminUserService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}