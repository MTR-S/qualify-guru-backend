package com.qualifyguru.qualify_guru_backend.domain.port.out.usecase;

import com.qualifyguru.qualify_guru_backend.application.dto.request.LoginRequest;
import com.qualifyguru.qualify_guru_backend.application.dto.response.AuthResponse;

public interface AuthenticationUseCases {
    public AuthResponse authenticate(LoginRequest loginRequest);
}
