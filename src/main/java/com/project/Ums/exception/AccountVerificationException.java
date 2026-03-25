package com.project.Ums.exception;

public class AccountVerificationException extends RuntimeException {
    public AccountVerificationException(String message) {
        super(message);
    }
    
    public AccountVerificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
