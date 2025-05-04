package org.example.bankingmanagementsystem.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.bankingmanagementsystem.model.enums.Role;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role",
            columnDefinition = "ENUM('ROLE_USER', 'ROLE_ADMIN')",
            nullable = false)
    private Role role;

    @Column(name = "user_name", nullable = false)
    private String userName;
}