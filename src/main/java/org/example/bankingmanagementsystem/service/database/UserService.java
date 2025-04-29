package org.example.bankingmanagementsystem.service.database;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bankingmanagementsystem.dto.UserRegistrationDto;
import org.example.bankingmanagementsystem.model.User;
import org.example.bankingmanagementsystem.model.enums.Role;
import org.example.bankingmanagementsystem.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        User user = findUserByEmail(mail).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User not found with mail: %s", mail)
        ));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name().replace("ROLE_", ""))
                .build();
    }

    public void saveUserInDb(UserRegistrationDto dto, PasswordEncoder passwordEncoder) {
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        User user = User.builder()
                .email(dto.getMail())
                .password(encodedPassword)
                .userName(dto.getName())
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

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}