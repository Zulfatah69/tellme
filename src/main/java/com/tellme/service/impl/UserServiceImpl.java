package com.tellme.service.impl;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tellme.exception.BusinessException;
import com.tellme.exception.ResourceNotFoundException;
import com.tellme.model.User;
import com.tellme.repository.AspirasiRepository;
import com.tellme.repository.ForumCommentRepository;
import com.tellme.repository.ForumPostRepository;
import com.tellme.repository.UserRepository;
import com.tellme.service.interfaces.UserService;
import com.tellme.util.PasswordUtil;

import jakarta.transaction.Transactional;

/**
 * Default implementation of {@link UserService}.
 *
 * <p>Manages user registration, authentication, profile updates, and deletion.
 * New passwords are hashed with BCrypt via {@link PasswordUtil}.
 * Legacy SHA-256 hashes are transparently upgraded on first successful login.
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final ForumCommentRepository forumCommentRepository;
    private final ForumPostRepository forumPostRepository;
    private final AspirasiRepository aspirasiRepository;

    public UserServiceImpl(UserRepository userRepository,
                           ForumCommentRepository forumCommentRepository,
                           ForumPostRepository forumPostRepository,
                           AspirasiRepository aspirasiRepository) {
        this.userRepository = userRepository;
        this.forumCommentRepository = forumCommentRepository;
        this.forumPostRepository = forumPostRepository;
        this.aspirasiRepository = aspirasiRepository;
    }

    /** {@inheritDoc} */
    @Override
    public User createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new BusinessException("Email address is already registered.");
        }

        if (user.getRole() == User.Role.MAHASISWA) {
            if (user.getNim() == null || user.getNim().trim().isEmpty()) {
                throw new BusinessException("Student ID (NIM) is required for student accounts.");
            }
            if (userRepository.findByNim(user.getNim()) != null) {
                throw new BusinessException("Student ID (NIM) is already registered.");
            }
        } else if (user.getNim() != null && !user.getNim().trim().isEmpty()) {
            if (userRepository.findByNim(user.getNim()) != null) {
                throw new BusinessException("Student ID (NIM) is already registered.");
            }
        }

        user.setPassword(PasswordUtil.hash(user.getPassword()));
        User saved = userRepository.save(user);
        log.info("New user registered: id={}, role={}", saved.getId(), saved.getRole());
        return saved;
    }

    /** {@inheritDoc} */
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /** {@inheritDoc} */
    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    /** {@inheritDoc} */
    @Override
    public User updateUser(Long id, User incoming) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));

        existing.setNama(incoming.getNama());
        existing.setEmail(incoming.getEmail());

        if (incoming.getPassword() != null && !incoming.getPassword().isEmpty()) {
            existing.setPassword(PasswordUtil.hash(incoming.getPassword()));
        }
        if (incoming.getRole() != null) {
            existing.setRole(incoming.getRole());
        }

        return userRepository.save(existing);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public void deleteById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));

        if (user.getRole() == User.Role.ADMIN) {
            throw new BusinessException("Admin accounts cannot be deleted through this endpoint.");
        }

        forumCommentRepository.deleteByUserId(id);
        aspirasiRepository.deleteByUserId(id);
        forumPostRepository.deleteByUserId(id);
        userRepository.deleteById(id);
        log.info("User {} deleted", id);
    }

    /** {@inheritDoc} */
    @Override
    public User login(String identifier, String password) {
        User user = userRepository.findByEmail(identifier);
        if (user == null) {
            user = userRepository.findByNim(identifier);
        }
        if (user == null || !PasswordUtil.verify(password, user.getPassword())) {
            throw new BusinessException("Invalid credentials. Please check your email/NIM and password.");
        }
        // Transparently upgrade legacy SHA-256 / plain-text passwords to BCrypt
        // on first successful login. No user action required.
        if (!PasswordUtil.isBcryptHash(user.getPassword())) {
            log.info("Upgrading password hash for user {} to BCrypt", user.getId());
            user.setPassword(PasswordUtil.hash(password));
        }
        user.setToken(UUID.randomUUID().toString());
        return userRepository.save(user);
    }

    /** {@inheritDoc} */
    @Override
    public void logout(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        user.setToken(null);
        userRepository.save(user);
        log.info("User {} logged out", userId);
    }
}