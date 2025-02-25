package com.example.jdbctemplatebooktask.Exceptions;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
public class BookNotFoundException extends RuntimeException {

    private HttpStatus status;
    private String errorCode;

    public BookNotFoundException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public BookNotFoundException(String message) {
        super(message);
    }

    public BookNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookNotFoundException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BookNotFoundException() {
    }

}
