package com.example.springsecuritywithjwt;

import com.example.springsecuritywithjwt.controller.AuthenticatedController;
import com.example.springsecuritywithjwt.dto.AuthRequest;
import com.example.springsecuritywithjwt.dto.AuthenticationResponse;
import com.example.springsecuritywithjwt.dto.RegisterRequest;
import com.example.springsecuritywithjwt.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthenticatedController authenticatedController;

    @Test
    void signUp_ShouldReturnCreatedAndRegisterRequest() {
        RegisterRequest registerRequest = new RegisterRequest("testUser", "password", "test@example.com");
        when(userService.registerUser(registerRequest)).thenReturn(registerRequest);

        ResponseEntity<RegisterRequest> response = authenticatedController.signUp(registerRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(registerRequest, response.getBody());
        verify(userService, times(1)).registerUser(registerRequest);
    }

    @Test
    void signIn_ShouldReturnOkAndAuthenticationResponse() {
        AuthRequest authRequest = new AuthRequest("testUser", "password");
        AuthenticationResponse authenticationResponse = new AuthenticationResponse("testToken", "testRefreshToken");
        when(userService.singIn(authRequest)).thenReturn(authenticationResponse);

        ResponseEntity<AuthenticationResponse> response = authenticatedController.signIn(authRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authenticationResponse, response.getBody());
        verify(userService, times(1)).singIn(authRequest);
    }

    @Test
    void refresh_ShouldReturnOkAndRefreshTokenResponse() {
        AuthenticationResponse authenticationResponse = new AuthenticationResponse("testToken", "testRefreshToken");
        when(userService.refreshToken(authenticationResponse)).thenReturn(authenticationResponse);

        ResponseEntity<?> response = authenticatedController.refresh(authenticationResponse);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authenticationResponse, response.getBody()); //  Проверяем, что тело ответа соответствует ожидаемому
        verify(userService, times(1)).refreshToken(authenticationResponse);
    }
}
