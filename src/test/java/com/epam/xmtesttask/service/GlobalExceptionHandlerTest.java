package com.epam.xmtesttask.service;

import com.epam.xmtesttask.domain.ErrorResponse;
import com.epam.xmtesttask.domain.exception.CryptoDataNotFoundException;
import com.epam.xmtesttask.domain.exception.CryptoNotFoundException;
import com.epam.xmtesttask.domain.exception.RateLimitExceededException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleCryptoNotFound_shouldReturn404AndErrorResponse() {
        // Given
        String expectedMessage = "Crypto not supported or no data: ETH";
        String symbol = "ETH";
        CryptoNotFoundException ex = new CryptoNotFoundException(symbol);

        // When
        ResponseEntity<ErrorResponse> response = handler.handleCryptoNotFound(ex);

        // Then
        assertEquals(404, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("NOT_FOUND", response.getBody().code());
        assertEquals(expectedMessage, response.getBody().message());
    }

    @Test
    void handleCryptoDataNotFound_shouldReturn404AndErrorResponse() {
        // Given
        String expectedMessage = "Crypto (BTC) data not found for date: 2022-01-01";
        String symbol = "BTC";
        LocalDate localDate = LocalDate.of(2022, 01, 01);
        CryptoDataNotFoundException ex = new CryptoDataNotFoundException(symbol, localDate);

        // When
        ResponseEntity<ErrorResponse> response = handler.handleCryptoNotFound(ex);

        // Then
        assertEquals(404, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("NOT_FOUND", response.getBody().code());
        assertEquals(expectedMessage, response.getBody().message());
    }

    @Test
    void handleRateLimitExceeded_shouldReturn429AndErrorResponse() {
        // Given
        String message = "Rate Limit Exceeded from ip: 192.168.1.100";
        String ip = "192.168.1.100";
        RateLimitExceededException ex = new RateLimitExceededException(ip);

        // When
        ResponseEntity<ErrorResponse> response = handler.handleRateLimitExceeded(ex);

        // Then
        assertEquals(429, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("TOO_MANY_REQUESTS", response.getBody().code());
        assertEquals(message, response.getBody().message());
    }

    @Test
    void handleOther_shouldReturn500AndErrorResponse() {
        // Given
        String message = "Unexpected error";
        Exception ex = new Exception(message);

        // When
        ResponseEntity<ErrorResponse> response = handler.handleOther(ex);

        // Then
        assertEquals(500, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("INTERNAL_ERROR", response.getBody().code());
        assertEquals(message, response.getBody().message());
    }
}