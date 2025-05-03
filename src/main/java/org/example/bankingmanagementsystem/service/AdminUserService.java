package org.example.bankingmanagementsystem.service;

import org.example.bankingmanagementsystem.dto.user.UpdateUserDto;
import org.example.bankingmanagementsystem.dto.user.UserRegistrationDto;
import org.example.bankingmanagementsystem.dto.user.UserResponseDto;
import org.springframework.data.domain.Page;

public interface AdminUserService {
    Page<UserResponseDto> getAllUsers(int page, int size);
    UserResponseDto getUserById(long id);
    UserResponseDto getUserByEmail(String email);
    UserResponseDto getUserByUsername(String username);
    UserResponseDto createAdmin(UserRegistrationDto dto);
    UserResponseDto updateUser(Long id, UpdateUserDto dto);
    void deleteUser(Long id);
}
