package com.tellme.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request body for the {@code POST /api/auth/login} endpoint.
 *
 * <p>The {@code identifier} field accepts either an email address
 * or a student ID (NIM), allowing both authentication methods.
 */
public class LoginRequest {

    /**
     * The user's email address or student ID (NIM).
     * Must not be blank.
     */
    @NotBlank(message = "Identifier (email or NIM) must not be blank.")
    private String identifier;

    /**
     * The user's plain-text password. Must not be blank.
     * This is transmitted over HTTPS and hashed server-side.
     */
    @NotBlank(message = "Password must not be blank.")
    private String password;

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}