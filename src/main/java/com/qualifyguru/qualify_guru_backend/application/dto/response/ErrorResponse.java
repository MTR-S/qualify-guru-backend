package com.qualifyguru.qualify_guru_backend.application.dto.response;

import java.time.LocalDateTime;

public record ErrorResponse(
        String error,
        String message,
        int status,
        String path,
        LocalDateTime timestamp
) {

    public static ErrorResponse of(String error, String message, int status, String path) {
        return new ErrorResponse(
                error,
                message,
                status,
                path,
                LocalDateTime.now()
        );
    }
}
