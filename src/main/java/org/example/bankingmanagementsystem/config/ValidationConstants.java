package org.example.bankingmanagementsystem.config;

public class ValidationConstants {
    public static final String AMOUNT_MIN_VALUE = "0.01";
    public static final String AMOUNT_POSITIVE_MESSAGE = "Amount must be positive";
    public static final String AMOUNT_MIN_MESSAGE = "Amount must be at least " + AMOUNT_MIN_VALUE;

    public static final int PAGE_MIN_VALUE = 0;
    public static final int SIZE_MIN_VALUE = 1;
    public static final int SIZE_MAX_VALUE = 100;
    public static final String PAGE_MIN_MESSAGE = "Page index must be 0 or greater";
    public static final String SIZE_MIN_MESSAGE = "Size must be at least " + SIZE_MIN_VALUE;
    public static final String SIZE_MAX_MESSAGE = "Size must be less than " + SIZE_MAX_VALUE;

    public static final String ID_NOT_NULL_MESSAGE = "Id cannot be null";
    public static final String ID_POSITIVE_MESSAGE = "Id must be positive";

    public static final String EMAIL_NOT_BLANK_MESSAGE = "Email cannot be empty";
    public static final String EMAIL_VALID_MESSAGE = "Email should be valid";
    public static final int EMAIL_MAX_SIZE = 255;
    public static final String EMAIL_SIZE_MESSAGE = "Email must be less than " + EMAIL_MAX_SIZE + " characters";

    public static final String USERNAME_NOT_BLANK_MESSAGE = "Username cannot be empty";
    public static final int USERNAME_MIN_SIZE = 2;
    public static final int USERNAME_MAX_SIZE = 50;
    public static final String USERNAME_SIZE_MESSAGE = "Username must be between " + USERNAME_MIN_SIZE
            + " and " + USERNAME_MAX_SIZE + " characters";

    public static final String PASSWORD_NOT_BLANK_MESSAGE = "Password cannot be empty";
    public static final int PASSWORD_MIN_SIZE = 6;
    public static final int PASSWORD_MAX_SIZE = 50;
    public static final String PASSWORD_SIZE_MESSAGE = "Password should be between " + PASSWORD_MIN_SIZE
            + " and " + PASSWORD_MAX_SIZE + " characters";
}