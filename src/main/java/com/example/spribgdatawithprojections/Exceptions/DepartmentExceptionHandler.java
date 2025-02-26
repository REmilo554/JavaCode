package com.example.spribgdatawithprojections.Exceptions;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class DepartmentExceptionHandler {

    @ResponseBody
    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<?> handleDepartmentNotFoundException(DepartmentNotFoundException ex) {
        return ResponseEntity.status(ex.getStatus())
                .contentType(MediaType.TEXT_PLAIN)
                .body(ex.getMessage());
    }
}
