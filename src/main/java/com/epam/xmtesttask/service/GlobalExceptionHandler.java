package com.epam.xmtesttask.service;

import com.epam.xmtesttask.domain.ErrorResponse;
import com.epam.xmtesttask.domain.exception.CryptoDataNotFoundException;
import com.epam.xmtesttask.domain.exception.RateLimitExceededException;
import com.epam.xmtesttask.domain.exception.CryptoNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for REST controllers.
 * <p>
 * This class provides centralized handling of exceptions thrown by controller methods,
 * mapping them to standardized HTTP responses with appropriate status codes and error messages.
 * <p>
 * Each handler method returns a {@link ResponseEntity} containing an {@link ErrorResponse}
 * with a specific error code and message.
 *
 * <p><b>Handled exceptions:</b></p>
 * <ul>
 *     <li>{@link CryptoNotFoundException} - 404 Not Found</li>
 *     <li>{@link CryptoDataNotFoundException} - 404 Not Found</li>
 *     <li>{@link RateLimitExceededException} - 429 Too Many Requests</li>
 *     <li>{@link Exception} (all other exceptions) - 500 Internal Server Error</li>
 * </ul>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CryptoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCryptoNotFound(CryptoNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(CryptoDataNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCryptoNotFound(CryptoDataNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleRateLimitExceeded(RateLimitExceededException ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(new ErrorResponse("TOO_MANY_REQUESTS", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOther(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("INTERNAL_ERROR", ex.getMessage()));
    }
}
