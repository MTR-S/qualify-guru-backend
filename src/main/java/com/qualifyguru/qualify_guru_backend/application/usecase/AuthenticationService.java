package com.qualifyguru.qualify_guru_backend.application.usecase;

import com.qualifyguru.qualify_guru_backend.application.dto.request.LoginRequest;
import com.qualifyguru.qualify_guru_backend.application.dto.response.AuthResponse;
import com.qualifyguru.qualify_guru_backend.domain.port.out.usecase.AuthenticationUseCases;
import com.qualifyguru.qualify_guru_backend.infrastructure.security.jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements AuthenticationUseCases {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponse authenticate(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwtToken = jwtService.generateToken(userDetails);

        return new AuthResponse(jwtToken, "Bearer");
    }
}
