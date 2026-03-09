package com.qualifyguru.qualify_guru_backend.infrastructure.api.controller;

import com.qualifyguru.qualify_guru_backend.application.dto.request.LoginRequest;
import com.qualifyguru.qualify_guru_backend.application.dto.response.AuthResponse;
import com.qualifyguru.qualify_guru_backend.application.usecase.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/qualify/guru/v1/auth")
public class AuthController {
    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {

        AuthResponse response = authenticationService.authenticate(request);

        return ResponseEntity.ok(response);
    }
}
