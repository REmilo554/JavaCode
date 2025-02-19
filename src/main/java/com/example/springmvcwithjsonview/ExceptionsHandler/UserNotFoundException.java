package com.example.springmvcwithjsonview.ExceptionsHandler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserNotFoundException extends RuntimeException {

    private HttpStatus status;
    private String errorCode;

    public UserNotFoundException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public UserNotFoundException() {
    }
}
