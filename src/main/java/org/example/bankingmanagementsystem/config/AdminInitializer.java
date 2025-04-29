package org.example.bankingmanagementsystem.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bankingmanagementsystem.model.User;
import org.example.bankingmanagementsystem.model.enums.Role;
import org.example.bankingmanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.mail}")
    private String mail;
    @Value("${admin.password}")
    private String password;
    @Value("${admin.name}")
    private String name;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail(mail).isEmpty()) {
            User admin = User.builder()
                    .email(mail)
                    .password(passwordEncoder.encode(password))
                    .role(Role.ROLE_ADMIN)
                    .userName(name)
                    .build();
            userRepository.save(admin);
            log.info("Admin user created successfully");
        }
    }
}
