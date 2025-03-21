package com.vsu.events_spring.exception;

public class LoginExistsException extends RuntimeException {
    public LoginExistsException(String message) {
        super(message);
    }
}
