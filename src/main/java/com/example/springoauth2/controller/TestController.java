package com.example.springoauth2.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/user/test")
    @PreAuthorize("hasRole('USER')")
    public String userTest() {
        return "Доступно для USER";
    }

    @GetMapping("/admin/test")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminTest() {
        return "Доступно для ADMIN";
    }
}
