package com.tellme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tellme.config.AuthInterceptor;
import com.tellme.dto.LoginRequest;
import com.tellme.exception.BusinessException;
import com.tellme.model.User;
import com.tellme.service.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Slice test for {@link AuthController} using MockMvc.
 *
 * <p>Uses {@code @WebMvcTest} which boots only the web layer.
 * The real {@link AuthInterceptor} is replaced by a {@code @MockBean}
 * that allows all requests through.
 */
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    /**
     * Mock the interceptor so it doesn't block requests in the test environment.
     * The real interceptor requires a live database (token lookup).
     */
    @MockBean
    private AuthInterceptor authInterceptor;

    private User testUser;

    @BeforeEach
    void setUp() throws Exception {
        testUser = new User();
        testUser.setId(1L);
        testUser.setNama("Test User");
        testUser.setEmail("test@example.com");
        testUser.setToken("dummy-token-uuid");
        testUser.setRole(User.Role.MAHASISWA);

        // Allow all requests through the mocked interceptor
        when(authInterceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    // =========================================================================

    @Test
    @DisplayName("POST /api/auth/login — valid credentials → 200 OK with token in body")
    void login_returns200WithUser() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setIdentifier("test@example.com");
        loginRequest.setPassword("secret");

        when(userService.login("test@example.com", "secret")).thenReturn(testUser);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("dummy-token-uuid"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(userService).login("test@example.com", "secret");
    }

    @Test
    @DisplayName("POST /api/auth/login — invalid credentials → 422 Unprocessable Entity")
    void login_returns422OnInvalidCredentials() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setIdentifier("test@example.com");
        loginRequest.setPassword("wrong-password");

        when(userService.login("test@example.com", "wrong-password"))
                .thenThrow(new BusinessException("Invalid credentials."));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnprocessableEntity());

        verify(userService).login("test@example.com", "wrong-password");
    }

    @Test
    @DisplayName("POST /api/auth/logout — authenticated user → 204 No Content, logout called")
    void logout_returns204() throws Exception {
        mockMvc.perform(post("/api/auth/logout")
                        .requestAttr("currentUser", testUser))
                .andExpect(status().isNoContent());

        verify(userService).logout(testUser.getId());
    }
}
