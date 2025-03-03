package com.example.springsecuritywithjwt.service;

import com.example.springsecuritywithjwt.dto.AuthRequest;
import com.example.springsecuritywithjwt.dto.AuthenticationResponse;
import com.example.springsecuritywithjwt.dto.RegisterRequest;
import com.example.springsecuritywithjwt.entity.User;
import com.example.springsecuritywithjwt.repository.UserRepository;
import com.example.springsecuritywithjwt.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, @Lazy AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Transactional
    public RegisterRequest registerUser(RegisterRequest registerRequest) {
        RegisterRequest newRegisterRequest = new RegisterRequest();
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(registerRequest.getRole());
        user.setIsAccountNonLocked(true);
        newRegisterRequest.setUsername(registerRequest.getUsername());
        newRegisterRequest.setPassword(registerRequest.getPassword());
        newRegisterRequest.setRole(registerRequest.getRole());
        userRepository.save(user);
        logger.info("User {} registered", user.getUsername());
        return newRegisterRequest;
    }

    public AuthenticationResponse singIn(AuthRequest registerRequest) {
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();

        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        registerRequest.getUsername(), registerRequest.getPassword()));
        User user = userRepository.findByUsername(registerRequest.getUsername()).orElseThrow();
        String token = jwtTokenUtil.generateToken(user);
        String refreshToken = jwtTokenUtil.generateRefreshToken(new HashMap<>(), user);
        authenticationResponse.setToken(token);
        authenticationResponse.setRefreshToken(refreshToken);
        logger.info("User {} logged in", user.getUsername());
        return authenticationResponse;
    }

    public AuthenticationResponse refreshToken(AuthenticationResponse authenticationResponse) {
        AuthenticationResponse newAuthenticationResponse = new AuthenticationResponse();
        String usernameFromToken = jwtTokenUtil.getUsernameFromToken(authenticationResponse.getToken());
        User user = userRepository.findByUsername(usernameFromToken).orElseThrow();
        if (jwtTokenUtil.validateToken(authenticationResponse.getToken(), user)) {
            String token = jwtTokenUtil.generateToken(user);
            String refreshToken = jwtTokenUtil.generateRefreshToken(new HashMap<>(), user);
            newAuthenticationResponse.setToken(token);
            newAuthenticationResponse.setRefreshToken(refreshToken);
        }
        logger.info("User {} start getting refresh token", user.getUsername());
        return newAuthenticationResponse;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }

    public User getAdminByName(String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }

    public List<User> getAllAdmin() {
        return userRepository.getAllAdmin();
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Transactional
    public String unlockAccount(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return "User not found";
        }
        User user = userOptional.get();
        if (user.isAccountNonLocked()) {
            return "Account is already unlocked";
        }
        user.setIsAccountNonLocked(true);
        user.setFailedLoginAttempts(0);
        userRepository.save(user);
        logger.info("User {} unlocked", user.getUsername());
        return "User account unlocked";
    }
}

