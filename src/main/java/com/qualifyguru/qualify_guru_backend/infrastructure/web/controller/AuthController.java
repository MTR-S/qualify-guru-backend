package com.qualifyguru.qualify_guru_backend.infrastructure.web.controller;

import com.qualifyguru.qualify_guru_backend.application.dto.request.LoginRequest;
import com.qualifyguru.qualify_guru_backend.application.dto.request.RegisterUserRequest;
import com.qualifyguru.qualify_guru_backend.application.dto.response.AuthResponse;
import com.qualifyguru.qualify_guru_backend.application.port.in.AuthenticationUseCases;
import com.qualifyguru.qualify_guru_backend.application.port.in.RegisterUserUseCases;
import com.qualifyguru.qualify_guru_backend.infrastructure.security.adapter.AuthenticationAdapter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/qualify-guru/api/v1/auth")
public class AuthController {
    private final AuthenticationUseCases authenticationUseCases;
    private final RegisterUserUseCases registerUserUseCases;

    public AuthController(AuthenticationAdapter authenticationUseCases
                            , RegisterUserUseCases registerUserUseCases) {
        this.authenticationUseCases = authenticationUseCases;
        this.registerUserUseCases = registerUserUseCases;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse response = authenticationUseCases.authenticate(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> login(
            @Valid @RequestBody RegisterUserRequest request) {

        registerUserUseCases.registerUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
