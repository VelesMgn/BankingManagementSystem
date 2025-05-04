package org.example.bankingmanagementsystem.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtTokenUtilsTest {

    @InjectMocks
    private JwtTokenUtils jwtTokenUtils;

    @Mock
    private UserDetails userDetails;

    private final String secret = "testsecret123456789012345678901234567890123456789012345678901234567890";
    private final Duration expirationTime = Duration.ofHours(1);

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtTokenUtils, "jwtSecret", secret);
        ReflectionTestUtils.setField(jwtTokenUtils, "expirationTime", expirationTime);
    }

    @Test
    void generateToken_ShouldReturnValidToken() {
        when(userDetails.getUsername()).thenReturn("test@example.com");
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());

        String token = jwtTokenUtils.generateToken(userDetails);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void getUsernameFromToken_ShouldReturnUsername() {
        UserDetails user = new User("test@example.com", "password", Collections.emptyList());
        String token = jwtTokenUtils.generateToken(user);
        String username = jwtTokenUtils.getUsernameFromToken(token);

        assertEquals("test@example.com", username);
    }

    @Test
    void validateToken_ShouldReturnTrueForValidToken() {
        UserDetails user = new User("test@example.com", "password", Collections.emptyList());
        String token = jwtTokenUtils.generateToken(user);

        boolean isValid = jwtTokenUtils.validateToken(token);

        assertTrue(isValid);
    }

    @Test
    void validateToken_ShouldReturnFalseForExpiredToken() {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        String expiredToken = Jwts.builder()
                .setSubject("test@example.com")
                .setIssuedAt(new Date(System.currentTimeMillis() - 10000))
                .setExpiration(new Date(System.currentTimeMillis() - 5000))
                .signWith(key)
                .compact();

        boolean isValid = jwtTokenUtils.validateToken(expiredToken);

        assertFalse(isValid);
    }

    @Test
    void validateToken_ShouldReturnFalseForInvalidToken() {
        boolean isValid = jwtTokenUtils.validateToken("invalid.token.here");

        assertFalse(isValid);
    }
}
