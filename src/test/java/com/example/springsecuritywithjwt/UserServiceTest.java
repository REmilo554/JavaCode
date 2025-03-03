package com.example.springsecuritywithjwt;

import com.example.springsecuritywithjwt.dto.AuthRequest;
import com.example.springsecuritywithjwt.dto.AuthenticationResponse;
import com.example.springsecuritywithjwt.dto.RegisterRequest;
import com.example.springsecuritywithjwt.entity.User;
import com.example.springsecuritywithjwt.repository.UserRepository;
import com.example.springsecuritywithjwt.service.UserDetailsServiceImpl;
import com.example.springsecuritywithjwt.service.UserService;
import com.example.springsecuritywithjwt.util.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetails userDetails;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserService userService;

    private RegisterRequest registerRequest;
    private User user;
    private AuthRequest authRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("testUser");
        registerRequest.setPassword("password");
        registerRequest.setRole("USER");

        user = new User();
        user.setUsername("testUser");
        user.setPassword("encodedPassword");
        user.setRole("USER");
        user.setIsAccountNonLocked(true);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken("testToken");
        authenticationResponse.setRefreshToken("testRefreshToken");

        authRequest = new AuthRequest();
        authRequest.setUsername("testUser");
        authRequest.setPassword("password");

    }


    @Test
    void registerUser_ShouldReturnRegisterRequest() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        RegisterRequest result = userService.registerUser(registerRequest);

        assertNotNull(result);
        assertEquals(registerRequest.getUsername(), result.getUsername());
        assertEquals(registerRequest.getPassword(), result.getPassword());
        assertEquals(registerRequest.getRole(), result.getRole());
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode(anyString());
    }

    @Test
    void singIn_ShouldReturnAuthenticationResponse() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(jwtTokenUtil.generateToken(user)).thenReturn("testToken");
        when(jwtTokenUtil.generateRefreshToken(ArgumentMatchers.argThat(map -> map instanceof Map<?, ?>), any(User.class))).thenReturn("testRefreshToken");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        AuthenticationResponse result = userService.singIn(authRequest);

        assertNotNull(result);
        assertEquals("testToken", result.getToken());
        assertEquals("testRefreshToken", result.getRefreshToken());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByUsername("testUser");
        verify(jwtTokenUtil, times(1)).generateToken(user);
        verify(jwtTokenUtil, times(1)).generateRefreshToken(ArgumentMatchers.argThat(map -> map instanceof Map<?, ?>), any(User.class));
    }

    @Test
    void refreshToken_ShouldReturnNewAuthenticationResponse_WhenTokenIsValid() {
        String token = "validToken";
        when(jwtTokenUtil.getUsernameFromToken(token)).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(jwtTokenUtil.validateToken(token, user)).thenReturn(true);
        when(jwtTokenUtil.generateToken(user)).thenReturn("newToken");
        when(jwtTokenUtil.generateRefreshToken(ArgumentMatchers.argThat(map -> map instanceof Map<?, ?>), any(User.class))).thenReturn("newRefreshToken");

        AuthenticationResponse authResponse = new AuthenticationResponse();
        authResponse.setToken(token);

        AuthenticationResponse result = userService.refreshToken(authResponse);

        assertNotNull(result);
        assertEquals("newToken", result.getToken());
        assertEquals("newRefreshToken", result.getRefreshToken());
        verify(jwtTokenUtil).getUsernameFromToken(token);
        verify(userRepository).findByUsername("testUser");
        verify(jwtTokenUtil).validateToken(token, user);
        verify(jwtTokenUtil).generateToken(user);
        verify(jwtTokenUtil).generateRefreshToken(ArgumentMatchers.argThat(map -> map instanceof Map<?, ?>), any(User.class));
    }


    @Test
    void refreshToken_ShouldNotRefreshToken_WhenTokenIsInvalid() {
        String token = "invalidToken";
        when(jwtTokenUtil.getUsernameFromToken(token)).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(jwtTokenUtil.validateToken(token, user)).thenReturn(false);

        AuthenticationResponse authResponse = new AuthenticationResponse();
        authResponse.setToken(token);

        AuthenticationResponse result = userService.refreshToken(authResponse);

        assertNotNull(result);
        assertNull(result.getToken());
        assertNull(result.getRefreshToken());
        verify(jwtTokenUtil).getUsernameFromToken(token);
        verify(userRepository).findByUsername("testUser");
        verify(jwtTokenUtil).validateToken(token, user);
        verify(jwtTokenUtil, never()).generateToken(user);
        verify(jwtTokenUtil, never()).generateRefreshToken(ArgumentMatchers.argThat(map -> map instanceof Map<?, ?>), any(User.class));
    }
}
