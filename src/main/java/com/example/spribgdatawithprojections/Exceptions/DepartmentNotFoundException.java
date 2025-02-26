package com.example.spribgdatawithprojections.Exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class DepartmentNotFoundException extends RuntimeException {
    private HttpStatus status;
    private String errorCode;

    public DepartmentNotFoundException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public DepartmentNotFoundException(String message) {
        super(message);
    }

    public DepartmentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DepartmentNotFoundException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public DepartmentNotFoundException() {
    }
}
