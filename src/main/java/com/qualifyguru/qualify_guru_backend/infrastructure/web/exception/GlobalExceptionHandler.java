package com.qualifyguru.qualify_guru_backend.infrastructure.web.exception;

import com.qualifyguru.qualify_guru_backend.application.dto.response.ErrorResponse;
import com.qualifyguru.qualify_guru_backend.domain.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleUnauthorized(HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ErrorResponse.of(
                        "Unauthorized",
                        "E-mail or password incorrect.",
                        HttpStatus.UNAUTHORIZED.value(),
                        request.getRequestURI()
                )
        );
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex,
            HttpServletRequest request
    ) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.of(
                        "Business Error",
                        ex.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        request.getRequestURI()
                )
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request
    ) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.of(
                        "Bad Request",
                        ex.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        request.getRequestURI()
                )
        );
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> handleIOException(
            IOException ex,
            HttpServletRequest request
    ) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ErrorResponse.of(
                        "File Error",
                        "An error occurred while processing the file.",
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        request.getRequestURI()
                )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request
    ) {
        //ex.printStackTrace();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ErrorResponse.of(
                        "Internal Server Error",
                        "An unexpected error occurred.",
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        request.getRequestURI()
                )
        );
    }
}
