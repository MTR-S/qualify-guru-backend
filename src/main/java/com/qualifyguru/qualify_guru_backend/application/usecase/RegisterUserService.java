package com.qualifyguru.qualify_guru_backend.application.usecase;

import com.qualifyguru.qualify_guru_backend.application.dto.request.RegisterUserRequest;
import com.qualifyguru.qualify_guru_backend.application.port.in.RegisterUserUseCases;
import com.qualifyguru.qualify_guru_backend.application.port.out.UserRepositoryPort;
import com.qualifyguru.qualify_guru_backend.domain.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserService implements RegisterUserUseCases {

    private static final String ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE = "This email already exists. Try again with a different login";

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserService(UserRepositoryPort userRepositoryPort,
                               PasswordEncoder passwordEncoder) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(RegisterUserRequest registerUserRequest) {

        if (userRepositoryPort.existsByEmail(registerUserRequest.email())) {

            throw new IllegalArgumentException(ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE);
        }

        String hashedPassword = passwordEncoder.encode(registerUserRequest.password());

        User newUser = User.createNewClient(registerUserRequest.email(), hashedPassword);

        userRepositoryPort.save(newUser);
    }
}
