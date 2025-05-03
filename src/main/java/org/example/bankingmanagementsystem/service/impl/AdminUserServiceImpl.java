package org.example.bankingmanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bankingmanagementsystem.dto.user.UpdateUserDto;
import org.example.bankingmanagementsystem.dto.user.UserRegistrationDto;
import org.example.bankingmanagementsystem.dto.user.UserResponseDto;
import org.example.bankingmanagementsystem.exception.UserAlreadyExistsException;
import org.example.bankingmanagementsystem.model.User;
import org.example.bankingmanagementsystem.model.enums.Role;
import org.example.bankingmanagementsystem.service.AdminUserService;
import org.example.bankingmanagementsystem.service.database.UserDatabaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {
    private final AdminCardServiceImpl adminCardService;
    private final UserDatabaseService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<UserResponseDto> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> usersPage = userService.getAllUsers(pageable);
        return usersPage.map(this::convertToDto);
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
        return convertToDto(user);
    }

    @Override
    public UserResponseDto getUserByEmail(String email) {
        User user = userService.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return convertToDto(user);
    }

    @Override
    public UserResponseDto getUserByUsername(String username) {
        User user = userService.findUserByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return convertToDto(user);
    }

    @Override
    public UserResponseDto createAdmin(UserRegistrationDto dto) {
        if (userService.mailIsPresent(dto.getMail())) {
            throw new UserAlreadyExistsException(dto.getMail());
        }

        if (userService.nameIsPresent(dto.getName())) {
            throw new UserAlreadyExistsException(dto.getName());
        }

        String password = dto.getPassword();
        String email = dto.getMail();
        String name = dto.getName();

        User user = userService.saveUserInDb(email, name, password, Role.ROLE_ADMIN, passwordEncoder);

        return convertToDto(user);
    }

    @Override
    public UserResponseDto updateUser(Long id, UpdateUserDto dto) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

        if (dto.getName() != null) user.setUserName(dto.getName());
        if (dto.getRole() != null) user.setRole(dto.getRole());

        User updatedUser = userService.updateUser(user);

        return convertToDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userService.idIsPresent(id)) {
            throw new UsernameNotFoundException("User not found with id: " + id);
        }

        userService.deleteById(id);
    }

    private UserResponseDto convertToDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .userName(user.getUserName())
                .bankCards(adminCardService.getBankCards(user.getId()))
                .role(user.getRole())
                .build();
    }
}