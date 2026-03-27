package com.qualifyguru.qualify_guru_backend.unit.application.usecase;

import com.qualifyguru.qualify_guru_backend.application.dto.request.RegisterUserRequest;
import com.qualifyguru.qualify_guru_backend.application.port.out.UserRepositoryPort;
import com.qualifyguru.qualify_guru_backend.application.usecase.RegisterUserService;
import com.qualifyguru.qualify_guru_backend.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegisterUserService registerUserService;

    @Test
    @DisplayName("You should successfully register a user when the email address does not exist.")
    void shouldRegisterUserSuccessfully() {
        // Arrange
        RegisterUserRequest request = new RegisterUserRequest("new@email.com", "veryStrongPassword123");

        when(userRepositoryPort.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("fake_bcrypt_hash");

        // Act
        registerUserService.registerUser(request);

        // Assert
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepositoryPort).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertEquals("new@email.com", savedUser.getEmail());
        assertEquals("fake_bcrypt_hash", savedUser.getPasswordHash());
        assertNotNull(savedUser.getPublicId());
        assertNotNull(savedUser.getRole());
    }

    @Test
    @DisplayName("It should throw an IllegalArgumentException when the email is already in use.")
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // Arrange
        RegisterUserRequest request = new RegisterUserRequest("existing@email.com", "veryStrongPassword123");

        when(userRepositoryPort.existsByEmail(request.email())).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            registerUserService.registerUser(request);
        });

        assertEquals("This email already exists. Try again with a different login", exception.getMessage());

        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepositoryPort, never()).save(any(User.class));
    }
}
