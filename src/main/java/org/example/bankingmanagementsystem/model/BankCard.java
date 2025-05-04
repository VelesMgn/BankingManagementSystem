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
@Table(name = "bank_card",
        indexes = {
                @Index(name = "idx_bank_card_user_id", columnList = "user_id"),
                @Index(name = "idx_bank_card_status", columnList = "status")
        })
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
    @Column(columnDefinition = "ENUM('ACTIVE', 'BLOCKED', 'EXPIRED')")
    private BankCardStatus status;

    @Column(nullable = false)
    private BigDecimal balance;
}