package com.tellme.service.interfaces;

import java.util.List;

import com.tellme.model.User;

/**
 * Service contract for user account management and authentication.
 */
public interface UserService {

    /**
     * Registers a new user account.
     *
     * @param user the user to create; password will be hashed before persistence
     * @return the persisted user entity
     */
    User createUser(User user);

    /**
     * Returns all registered users.
     *
     * @return list of all users
     */
    List<User> getAllUsers();

    /**
     * Returns a single user by ID.
     *
     * @param id the user's primary key
     * @return the user entity
     */
    User getById(Long id);

    /**
     * Updates an existing user's profile.
     *
     * @param id       the ID of the user to update
     * @param incoming updated field values
     * @return the updated user entity
     */
    User updateUser(Long id, User incoming);

    /**
     * Deletes a user account and all associated data.
     * Admin accounts cannot be deleted.
     *
     * @param id the user's primary key
     */
    void deleteById(Long id);

    /**
     * Authenticates a user by email or NIM and password.
     *
     * @param identifier email address or student ID
     * @param password   plain-text password
     * @return the authenticated user with a new session token
     */
    User login(String identifier, String password);

    /**
     * Invalidates the session token of the given user.
     *
     * @param userId the user's primary key
     */
    void logout(Long userId);
}