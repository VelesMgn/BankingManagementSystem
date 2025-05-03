package org.example.bankingmanagementsystem.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.bankingmanagementsystem.model.enums.BankCardStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
@Entity
@Table(name = "bank_card")
public class BankCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cardNumberEncrypted;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status",
            columnDefinition = "ENUM('ACTIVE', 'BLOCKED', 'EXPIRED')",
            nullable = false)
    private BankCardStatus status;

    @Column(nullable = false)
    private BigDecimal balance;
}