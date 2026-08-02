package com.tellme.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tellme.model.User;
import com.tellme.service.interfaces.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller for user account management.
 *
 * <p>Provides endpoints for registering new users and for administrators to
 * list, update, and delete user accounts. Authorization is enforced by
 * {@link com.tellme.config.AuthInterceptor}: only ADMIN users may list all
 * accounts or access other users' records.
 */
@Tag(name = "Users", description = "User registration and management")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Returns all registered users. Admin only.
     *
     * @return list of all user accounts
     */
    @Operation(summary = "List all users (admin only)")
    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Returns a single user by their ID.
     *
     * <p>Non-admin users may only retrieve their own record.
     * Access to other users' records is blocked by {@link com.tellme.config.AuthInterceptor}.
     *
     * @param id the user ID
     * @return the user account
     */
    @Operation(summary = "Get a user by ID")
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    /**
     * Registers a new user account (self-registration — no token required).
     *
     * @param user the new user data (name, email, NIM, password, role)
     * @return the created user with HTTP 201
     */
    @Operation(summary = "Register a new user account — no auth required")
    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(user));
    }

    /**
     * Updates an existing user's profile.
     *
     * @param id   the user ID to update
     * @param user the updated user data
     * @return the updated user account
     */
    @Operation(summary = "Update a user account")
    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    /**
     * Deletes a user account and all their associated content. Admin only.
     *
     * <p>Admin accounts cannot be deleted through this endpoint.
     *
     * @param id the user ID to delete
     */
    @Operation(summary = "Delete a user account (admin only)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}