package com.epam.xmtesttask.domain;

/**
 * Immutable value object representing a standardized error response for API endpoints.
 * <p>
 * This record is used to convey error information in a consistent format in HTTP responses,
 * typically when an exception is thrown or a request cannot be processed successfully.
 *
 * @param code    a machine-readable error code (e.g., "NOT_FOUND", "INTERNAL_ERROR")
 * @param message a human-readable error message describing the error
 */
public record ErrorResponse(String code, String message) {}

