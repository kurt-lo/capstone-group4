package com.capstone.cargo.exception;

public class ContainerNotFoundException extends RuntimeException {
    public ContainerNotFoundException(String message) {
        super(message);
    }
}
