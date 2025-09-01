package com.capstone.cargo.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message, AuthenticationException e) {
        super(message, e);
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }

}
