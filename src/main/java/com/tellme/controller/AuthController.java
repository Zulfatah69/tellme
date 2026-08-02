package com.tellme.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tellme.dto.LoginRequest;
import com.tellme.model.User;
import com.tellme.service.interfaces.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

/**
 * REST controller for authentication operations (login / logout).
 *
 * <p>Authentication is token-based. On login, the server generates an opaque
 * session token that the client includes in subsequent requests via the
 * {@code Authorization: Bearer <token>} header.
 */
@Tag(name = "Authentication", description = "Login and logout endpoints — no Bearer token required for login")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Authenticates a user and returns a session token.
     *
     * @param request login credentials (email/NIM + password)
     * @return the authenticated user object containing the session token
     */
    @Operation(summary = "Log in and receive a session token")
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest request) {
        User user = userService.login(request.getIdentifier(), request.getPassword());
        return ResponseEntity.ok(user);
    }

    /**
     * Invalidates the current user's session token.
     */
    @Operation(summary = "Log out and invalidate the current session token")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("currentUser");
        if (currentUser != null) {
            userService.logout(currentUser.getId());
        }
        return ResponseEntity.noContent().build();
    }
}