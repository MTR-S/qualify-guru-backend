package com.qualifyguru.qualify_guru_backend.application.port.in;

import com.qualifyguru.qualify_guru_backend.application.dto.request.RegisterUserRequest;

public interface RegisterUserUseCases {
    public void registerUser(RegisterUserRequest registerUserRequest);
}
