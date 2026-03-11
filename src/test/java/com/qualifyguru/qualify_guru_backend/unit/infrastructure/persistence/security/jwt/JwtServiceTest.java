package com.qualifyguru.qualify_guru_backend.unit.infrastructure.persistence.security.jwt;

import com.qualifyguru.qualify_guru_backend.infrastructure.security.service.JwtService;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {
    private JwtService jwtService;
    private UserDetails mockUser;

    private final String TEST_SECRET = "VGhpcy1Jcy1BLVRlc3QtU2VjcmV0LUtleS1UaGF0LU11c3QtQmUtQXQtTGVhc3QtMjU2LUJpdHM=";

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        ReflectionTestUtils.setField(jwtService, "secretKey", TEST_SECRET);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 1000 * 60 * 60);

        mockUser = new User("usermock@teste.com", "password",
                List.of(new SimpleGrantedAuthority("CLIENT")));
    }

    @Test
    @DisplayName("Should generate a valid token and allow username extraction")
    void shouldGenerateValidTokenAndAllowUsernameExtraction() {
        // Arrange & Act
        String token = jwtService.generateToken(mockUser);
        String extractedUsername = jwtService.extractUsername(token);

        // Assert
        assertNotNull(token);
        assertEquals(mockUser.getUsername(), extractedUsername);
        assertTrue(jwtService.isTokenValid(token));
    }

    @Test
    @DisplayName("Should extract user roles from token")
    void shouldExtractUserRolesFromToken() {
        // Arrange
        String token = jwtService.generateToken(mockUser);

        // Act
        List<SimpleGrantedAuthority> authorities = jwtService.extractAuthorities(token);

        // Assert
        assertEquals(1, authorities.size());
        assertEquals("CLIENT", authorities.get(0).getAuthority());
    }

    @Test
    @DisplayName("Should throw SignatureException when token is tampered")
    void shouldThrowSignatureExceptionWhenTokenIsTampered() {
        // Arrange
        String validToken = jwtService.generateToken(mockUser);
        String tamperedToken = validToken + "Tamper";

        // Act & Assert
        assertFalse(jwtService.isTokenValid(tamperedToken));

        assertThrows(SignatureException.class, () -> {
            jwtService.extractUsername(tamperedToken);
        });
    }
}