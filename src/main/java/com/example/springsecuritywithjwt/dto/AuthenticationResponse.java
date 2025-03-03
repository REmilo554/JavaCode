package com.example.springsecuritywithjwt.dto;

import lombok.Data;

@Data
public class AuthenticationResponse {

    private String token;
    private String refreshToken;

    public AuthenticationResponse(String token) {
        this.token = token;
    }

    public AuthenticationResponse(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public AuthenticationResponse() {
    }
}
