package org.example.bankingmanagementsystem.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(Long cardId) {
        super("Insufficient funds in wallet: " + cardId);
    }
}