package org.example.bankingmanagementsystem.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bankingmanagementsystem.model.enums.Role;
import org.example.bankingmanagementsystem.service.database.UserDatabaseService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {
    private final UserDatabaseService userDatabaseService;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.mail}")
    private String mail;
    @Value("${admin.password}")
    private String password;
    @Value("${admin.name}")
    private String name;

    @Override
    public void run(String... args) {
        if (userDatabaseService.findUserByEmail(mail).isEmpty()) {
            userDatabaseService.saveUserInDb(mail, name, password, Role.ROLE_ADMIN, passwordEncoder);
            log.info("Admin user created successfully");
        }
    }
}