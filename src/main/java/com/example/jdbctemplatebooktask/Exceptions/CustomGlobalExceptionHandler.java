package com.example.jdbctemplatebooktask.Exceptions;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class CustomGlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(BookNotFoundException ex) {
        return ResponseEntity.status(ex.getStatus())
                .contentType(MediaType.TEXT_PLAIN)
                .body(ex.getMessage());
    }
}
