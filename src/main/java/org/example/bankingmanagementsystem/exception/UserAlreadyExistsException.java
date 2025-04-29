package org.example.bankingmanagementsystem.exception;

public class UserAlreadyExistsException extends ValidationException {
    public UserAlreadyExistsException(String data) {
        super("User with '" + data + "' already exists");
    }
}