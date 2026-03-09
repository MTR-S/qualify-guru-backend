package com.qualifyguru.qualify_guru_backend.application.dto.response;

public record AuthResponse(
        String token,
        String type
) {
}
