package org.example.bankingmanagementsystem.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.bankingmanagementsystem.dto.RegistrationResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RegistrationResponseDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));

        return ResponseEntity.badRequest()
                .body(new RegistrationResponseDto(false, errorMessage));
    }

    @ExceptionHandler({ValidationException.class, UserAlreadyExistsException.class})
    public ResponseEntity<RegistrationResponseDto> handleBusinessValidationExceptions(RuntimeException ex) {
        return ResponseEntity.badRequest().body(new RegistrationResponseDto(false, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RegistrationResponseDto> handleOtherExceptions(Exception ex) {
        log.error("Unexpected error", ex);
        return ResponseEntity.internalServerError()
                .body(new RegistrationResponseDto(false, "Internal server error"));
    }
}