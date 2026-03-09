package com.qualifyguru.qualify_guru_backend.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "The e-mail cannot be blank")
        @Email(message = "Formato de e-mail inválido")
        String email,

        @NotBlank(message = "The password cannot be blank")
        String password
) {
}
