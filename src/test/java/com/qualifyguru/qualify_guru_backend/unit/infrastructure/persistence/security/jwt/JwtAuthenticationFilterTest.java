package com.qualifyguru.qualify_guru_backend.unit.infrastructure.persistence.security.jwt;

import com.qualifyguru.qualify_guru_backend.infrastructure.security.filter.JwtAuthenticationFilter;
import com.qualifyguru.qualify_guru_backend.infrastructure.security.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {
    @Mock
    private JwtService jwtService;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();

        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {

        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Should bypass filter when Authorization header is missing or invalid")
    void shouldBypassFilterWhenAuthorizationHeaderIsMissingOrInvalid() throws Exception {

        // Arrange & Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
        verifyNoInteractions(jwtService);
    }

    @Test
    @DisplayName("Should authenticate user and populate SecurityContext when token is valid")
    void shouldAuthenticateUserAndPopulateSecurityContextWhenTokenIsValid() throws Exception {

        // Arrange
        String token = "valid.jwt.token";
        String userEmail = "usermock@teste.com";
        request.addHeader("Authorization", "Bearer " + token);

        when(jwtService.extractUsername(token)).thenReturn(userEmail);
        when(jwtService.isTokenValid(token)).thenReturn(true);
        when(jwtService.extractAuthorities(token)).thenReturn(List.of(new SimpleGrantedAuthority("CLIENT")));

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        var auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertEquals(userEmail, auth.getPrincipal());
        assertTrue(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("CLIENT")));

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("Should clear SecurityContext and continue filter chain when JWT throws exception")
    void shouldClearSecurityContextAndContinueFilterChainWhenJwtThrowsException() throws Exception {

        // Arrange
        String invalidToken = "expired.jwt.token";
        request.addHeader("Authorization", "Bearer " + invalidToken);

        when(jwtService.extractUsername(invalidToken)).thenThrow(new ExpiredJwtException(null, null, "Expired Token"));

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
