package com.example.springsecuritywithjwt.controller;

import com.example.springsecuritywithjwt.dto.AuthRequest;
import com.example.springsecuritywithjwt.dto.AuthenticationResponse;
import com.example.springsecuritywithjwt.dto.RegisterRequest;
import com.example.springsecuritywithjwt.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticatedController {

    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticatedController.class);

    @Autowired
    public AuthenticatedController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<RegisterRequest> signUp(@RequestBody RegisterRequest registerRequest) {
        logger.info("User {} signUp", registerRequest.getUsername());
        return new ResponseEntity<>(userService.registerUser(registerRequest), HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> signIn(@RequestBody AuthRequest authRequest) {
        logger.info("User {} signIn", authRequest.getUsername());
        return new ResponseEntity<>(userService.singIn(authRequest), HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody AuthenticationResponse authenticationResponse) {
        return new ResponseEntity<>(userService.refreshToken(authenticationResponse), HttpStatus.OK);
    }
}
