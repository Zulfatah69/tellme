package com.tellme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tellme.dto.LoginRequest;
import com.tellme.model.User;
import com.tellme.repository.UserRepository;
import com.tellme.service.interfaces.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public User login(@RequestBody LoginRequest request){
        return userService.login(
                request.getIdentifier(),
                request.getPassword()
        );
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("currentUser");
        if (currentUser != null) {
            currentUser.setToken(null);
            userRepository.save(currentUser);
        }
    }
}