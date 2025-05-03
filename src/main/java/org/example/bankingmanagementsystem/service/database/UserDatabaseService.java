package org.example.bankingmanagementsystem.service.database;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bankingmanagementsystem.model.User;
import org.example.bankingmanagementsystem.model.enums.Role;
import org.example.bankingmanagementsystem.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class UserDatabaseService implements UserDetailsService {
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

    public User saveUserInDb(String email, String name,
                             String password, Role role,
                             PasswordEncoder passwordEncoder) {
        String encodedPassword = passwordEncoder.encode(password);

        User user = User.builder()
                .email(email)
                .password(encodedPassword)
                .userName(name)
                .role(role)
                .build();

        userRepository.save(user);

        return user;
    }

    public boolean mailIsPresent(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean nameIsPresent(String name) {
        return userRepository.findByUserName(name).isPresent();
    }

    public boolean idIsPresent(Long id) {
        return userRepository.findById(id).isPresent();
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findUserByName(String name) {
        return userRepository.findByUserName(name);
    }

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Optional<User> getUserById(long id) {
        return userRepository.findById(id);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }
}