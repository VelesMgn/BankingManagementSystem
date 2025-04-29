package org.example.bankingmanagementsystem.service.database;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bankingmanagementsystem.dto.UserRegistrationDto;
import org.example.bankingmanagementsystem.model.User;
import org.example.bankingmanagementsystem.model.enums.Role;
import org.example.bankingmanagementsystem.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void saveUserInDb(UserRegistrationDto dto) {
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        User user = User.builder()
                .email(dto.getEmail())
                .password(encodedPassword)
                .userName(dto.getUserName())
                .role(Role.ROLE_USER)
                .build();
        userRepository.save(user);
    }

    public boolean mailIsPresent(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean nameIsPresent(String name) {
        return userRepository.findByUserName(name).isPresent();
    }
}
