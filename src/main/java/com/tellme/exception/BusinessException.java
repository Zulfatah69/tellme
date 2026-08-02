package com.tellme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a business rule or domain constraint is violated.
 * Maps to HTTP 422 Unprocessable Entity.
 */
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class BusinessException extends RuntimeException {

    /**
     * @param message a human-readable description of the violated rule
     */
    public BusinessException(String message) {
        super(message);
    }

    /**
     * @param message a human-readable description of the violated rule
     * @param cause   the underlying cause
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
