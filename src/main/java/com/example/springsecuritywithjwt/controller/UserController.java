package com.example.springsecuritywithjwt.controller;

import com.example.springsecuritywithjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * http://localhost:8081/user/getinfo?username=user1
     */
    @GetMapping("/getinfo")
    public ResponseEntity<?> getMyInfo(@RequestParam String username) {
        return new ResponseEntity<>(userService.findByUsername(username), HttpStatus.OK);
    }
}
