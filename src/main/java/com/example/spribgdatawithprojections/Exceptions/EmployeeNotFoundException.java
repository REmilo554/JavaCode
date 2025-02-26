package com.example.spribgdatawithprojections.Exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmployeeNotFoundException extends RuntimeException {
    private HttpStatus status;
    private String errorCode;

    public EmployeeNotFoundException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public EmployeeNotFoundException(String message) {
        super(message);
    }

    public EmployeeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmployeeNotFoundException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public EmployeeNotFoundException() {
    }
}
