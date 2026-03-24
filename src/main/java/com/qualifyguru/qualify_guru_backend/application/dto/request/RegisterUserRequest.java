package com.qualifyguru.qualify_guru_backend.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(
        @NotBlank(message = "The e-mail cannot be blank")
        @Email(message = "Invalid e-mail format")
        String email,

        @NotBlank(message = "The password cannot be blank")
        @Size(min = 8, message = "The password has to be at 8 characters minimum")
        String password
) {
}
