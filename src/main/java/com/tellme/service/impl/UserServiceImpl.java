package com.tellme.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tellme.config.PasswordUtil;
import com.tellme.model.User;
import com.tellme.repository.AspirasiRepository;
import com.tellme.repository.ForumCommentRepository;
import com.tellme.repository.ForumPostRepository;
import com.tellme.repository.UserRepository;
import com.tellme.service.interfaces.UserService;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ForumCommentRepository forumCommentRepository;

    @Autowired
    private ForumPostRepository forumPostRepository;

    @Autowired
    private AspirasiRepository aspirasiRepository;

    @Override
    public User createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email sudah digunakan");
        }
        if (userRepository.findByNim(user.getNim()) != null) {
            throw new RuntimeException("NIM sudah digunakan");
        }
        user.setPassword(PasswordUtil.hash(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
    }

    @Override
    public User updateUser(Long id, User user) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

        existing.setNama(user.getNama());
        existing.setEmail(user.getEmail());

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existing.setPassword(PasswordUtil.hash(user.getPassword()));
        }

        existing.setRole(user.getRole());
        return userRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

        if (user.getRole() == User.Role.ADMIN) {
            throw new RuntimeException("Admin tidak bisa dihapus");
        }

        forumCommentRepository.deleteByUserId(id);
        aspirasiRepository.deleteByUserId(id);
        forumPostRepository.deleteByUserId(id);
        userRepository.deleteById(id);
    }

    @Override
    public User login(String identifier, String password) {
        User user = userRepository.findByEmail(identifier);
        if (user == null) {
            user = userRepository.findByNim(identifier);
        }

        if (user == null || !PasswordUtil.verify(password, user.getPassword())) {
            throw new RuntimeException("Email/NIM atau password salah");
        }

        if (user.getPassword().length() != 64) {
            user.setPassword(PasswordUtil.hash(password));
        }

        user.setToken(UUID.randomUUID().toString());
        return userRepository.save(user);
    }
}