package com.banking.system.exception;

public class AccountConflictException extends RuntimeException {
    public AccountConflictException(String message) {
        super(message);
    }
}