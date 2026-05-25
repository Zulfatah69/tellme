package com.tellme.service.interfaces;

import java.util.List;

import com.tellme.model.User;

public interface UserService {

    User createUser(User user);

    List<User> getAllUsers();

    User getById(Long id);

    User updateUser(Long id, User user);

    void deleteById(Long id);

    User login(
            String identifier,
            String password
    );
}