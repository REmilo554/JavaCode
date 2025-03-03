package com.example.springsecuritywithjwt;

import com.example.springsecuritywithjwt.entity.User;
import com.example.springsecuritywithjwt.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class JwtTokenUtilTest {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        userDetails = User.builder()
                .username("testuser")
                .password("password")
                .role("USER")
                .isAccountNonLocked(true)
                .build();
    }

    @Test
    void generateTokenTest() {
        String token = jwtTokenUtil.generateToken(userDetails);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void getUsernameFromTokenTest() {
        String token = jwtTokenUtil.generateToken(userDetails);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        assertEquals("testuser", username);
    }

    @Test
    void validateTokenTest() {
        String token = jwtTokenUtil.generateToken(userDetails);
        assertTrue(jwtTokenUtil.validateToken(token, userDetails));
    }

    @Test
    void isTokenExpiredTest() throws InterruptedException {
        String token = jwtTokenUtil.generateToken(userDetails);
        Thread.sleep(5000);
        assertTrue(jwtTokenUtil.validateToken(token, userDetails));
    }

    @Test
    void getRoleFromTokenTest() {
        String token = jwtTokenUtil.generateToken(userDetails);
        assertNotNull(token);

        Claims claims = jwtTokenUtil.getClaimsFromToken(token);
        assertNotNull(claims);

        Map<String, Object> expectedClaims = Map.of("role", userDetails.getAuthorities().stream().findFirst().get().getAuthority());
        assertEquals(expectedClaims.get("role"), claims.get("role"));

        System.out.println(claims.get("role"));
    }
}
