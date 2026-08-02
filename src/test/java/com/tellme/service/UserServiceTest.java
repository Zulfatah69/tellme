package com.tellme.service;

import com.tellme.exception.BusinessException;
import com.tellme.exception.ResourceNotFoundException;
import com.tellme.model.User;
import com.tellme.repository.AspirasiRepository;
import com.tellme.repository.ForumCommentRepository;
import com.tellme.repository.ForumPostRepository;
import com.tellme.repository.UserRepository;
import com.tellme.service.impl.UserServiceImpl;
import com.tellme.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link UserServiceImpl} using Mockito.
 * No Spring context required.
 */
@DisplayName("UserServiceImpl")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private ForumCommentRepository forumCommentRepository;
    @Mock private ForumPostRepository forumPostRepository;
    @Mock private AspirasiRepository aspirasiRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setNama("Test User");
        user.setNim("12345678");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setRole(User.Role.MAHASISWA);
    }

    // =========================================================================
    // createUser
    // =========================================================================

    @Test
    @DisplayName("createUser — success: email not taken, NIM not taken, saved with hashed password")
    void createUser_success() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
        when(userRepository.findByNim(user.getNim())).thenReturn(null);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User result = userService.createUser(user);

        assertNotNull(result);
        // Password should be hashed (BCrypt), not stored as plain-text
        assertNotEquals("password123", result.getPassword());
        assertTrue(PasswordUtil.verify("password123", result.getPassword()),
                "Password should be verifiable with PasswordUtil.verify()");
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("createUser — throws BusinessException when email already exists")
    void createUser_throwsWhenEmailAlreadyExists() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        assertThrows(BusinessException.class, () -> userService.createUser(user));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("createUser — throws BusinessException when MAHASISWA NIM is blank")
    void createUser_throwsWhenNimMissingForMahasiswa() {
        user.setNim("");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);

        assertThrows(BusinessException.class, () -> userService.createUser(user));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("createUser — throws BusinessException when NIM is already taken")
    void createUser_throwsWhenNimAlreadyTaken() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
        when(userRepository.findByNim(user.getNim())).thenReturn(new User());

        assertThrows(BusinessException.class, () -> userService.createUser(user));
        verify(userRepository, never()).save(any());
    }

    // =========================================================================
    // login
    // =========================================================================

    @Test
    @DisplayName("login — success by email: user found, password matches, token set and saved")
    void login_successByEmail() {
        // Pre-hash the password so verify() works
        String bcryptHash = PasswordUtil.hash("password123");
        user.setPassword(bcryptHash);

        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.login("test@example.com", "password123");

        assertNotNull(result);
        assertNotNull(result.getToken());
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("login — success by NIM: user not found by email, found by NIM")
    void login_successByNim() {
        String bcryptHash = PasswordUtil.hash("password123");
        user.setPassword(bcryptHash);

        when(userRepository.findByEmail("12345678")).thenReturn(null);
        when(userRepository.findByNim("12345678")).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.login("12345678", "password123");

        assertNotNull(result);
        assertNotNull(result.getToken());
    }

    @Test
    @DisplayName("login — throws BusinessException on wrong password")
    void login_throwsOnWrongPassword() {
        String bcryptHash = PasswordUtil.hash("correctPassword");
        user.setPassword(bcryptHash);

        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        assertThrows(BusinessException.class,
                () -> userService.login("test@example.com", "wrongPassword"));
    }

    @Test
    @DisplayName("login — throws BusinessException when user not found by email or NIM")
    void login_throwsWhenUserNotFound() {
        when(userRepository.findByEmail("notfound")).thenReturn(null);
        when(userRepository.findByNim("notfound")).thenReturn(null);

        assertThrows(BusinessException.class,
                () -> userService.login("notfound", "password123"));
    }

    // =========================================================================
    // getById
    // =========================================================================

    @Test
    @DisplayName("getById — success: found → returns user")
    void getById_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getById(1L);

        assertEquals(user, result);
    }

    @Test
    @DisplayName("getById — throws ResourceNotFoundException when not found")
    void getById_throwsWhenNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getById(1L));
    }

    // =========================================================================
    // deleteById
    // =========================================================================

    @Test
    @DisplayName("deleteById — throws BusinessException when user is ADMIN (cannot delete admin)")
    void deleteById_throwsForAdmin() {
        user.setRole(User.Role.ADMIN);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(BusinessException.class, () -> userService.deleteById(1L));
        verify(userRepository, never()).deleteById(any());
    }

    // =========================================================================
    // updateUser
    // =========================================================================

    @Test
    @DisplayName("updateUser — updates nama, email, and re-hashes new password")
    void updateUser_updatesFieldsCorrectly() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User updateData = new User();
        updateData.setNama("New Name");
        updateData.setEmail("new@example.com");
        updateData.setPassword("newpass");

        User result = userService.updateUser(1L, updateData);

        assertEquals("New Name", result.getNama());
        assertEquals("new@example.com", result.getEmail());
        // Password must be hashed, not stored as plain-text
        assertNotEquals("newpass", result.getPassword());
        assertTrue(PasswordUtil.verify("newpass", result.getPassword()));
        verify(userRepository).save(user);
    }
}
