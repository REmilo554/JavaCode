package com.example.springsecuritywithjwt.controller;

import com.example.springsecuritywithjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/superadmin")
public class SuperAdminController {

    private final UserService userService;

    @Autowired
    public SuperAdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getadmin")
    public ResponseEntity<?> getAdmin(@RequestBody String username) {
        return new ResponseEntity<>(userService.getAdminByName(username), HttpStatus.OK);
    }

    @GetMapping("/getalladmin")
    public ResponseEntity<?> getAllAdmin() {
        return new ResponseEntity<>(userService.getAllAdmin(), HttpStatus.OK);
    }
}
