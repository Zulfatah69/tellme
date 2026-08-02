package com.tellme.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.tellme.model.User;
import com.tellme.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * HTTP interceptor that enforces token-based authentication on all
 * {@code /api/**} routes.
 *
 * <p>The client must supply a valid session token as a Bearer credential
 * in the {@code Authorization} header. The resolved {@link User} object is
 * stored as a request attribute ({@code "currentUser"}) for downstream use.
 *
 * <p><strong>Public routes</strong> (no token required):
 * <ul>
 *   <li>{@code POST /api/auth/login}</li>
 *   <li>{@code POST /api/users} (self-registration)</li>
 * </ul>
 *
 * <p><strong>Authorization rules for {@code /api/users}:</strong>
 * Non-admin users may only access their own record via the
 * {@code GET /api/users/{id}} endpoint.
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AuthInterceptor.class);

    private static final String BEARER_PREFIX = "Bearer ";

    private final UserRepository userRepository;

    public AuthInterceptor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Validates the Bearer token before the request reaches any controller.
     *
     * @return {@code true} to continue processing; {@code false} to abort
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        // Allow CORS pre-flight requests through
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String path = request.getRequestURI();
        String method = request.getMethod();

        // Public endpoints — no token required
        if (isPublicEndpoint(path, method)) {
            return true;
        }

        // Extract and validate the Bearer token
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            log.warn("Missing or malformed Authorization header for {} {}", method, path);
            sendUnauthorized(response, "Authentication required. Please provide a valid Bearer token.");
            return false;
        }

        String token = authHeader.substring(BEARER_PREFIX.length()).trim();
        if (token.isEmpty()) {
            sendUnauthorized(response, "Empty token provided.");
            return false;
        }

        User user = userRepository.findByToken(token);
        if (user == null) {
            log.warn("Invalid or expired session token for {} {}", method, path);
            sendUnauthorized(response, "Invalid or expired session. Please log in again.");
            return false;
        }

        // Enforce resource-level authorization on /api/users
        if (!isAuthorizedForUsersEndpoint(path, method, user, response)) {
            return false;
        }

        // Make the resolved user available to controllers
        request.setAttribute("currentUser", user);
        return true;
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private boolean isPublicEndpoint(String path, String method) {
        // Authentication endpoints
        if ("/api/auth/login".equals(path) && "POST".equalsIgnoreCase(method)) return true;
        if ("/api/users".equals(path) && "POST".equalsIgnoreCase(method)) return true;

        // Swagger UI and OpenAPI spec — must be accessible without a token
        if (path.startsWith("/swagger-ui")) return true;
        if (path.startsWith("/v3/api-docs")) return true;
        if ("/swagger-ui.html".equals(path)) return true;

        return false;
    }

    /**
     * Enforces authorization for {@code /api/users/**} endpoints.
     * Only admins may list all users or access other users' records.
     *
     * @return {@code true} if access is granted
     */
    private boolean isAuthorizedForUsersEndpoint(String path,
                                                  String method,
                                                  User user,
                                                  HttpServletResponse response) throws Exception {
        if (!path.startsWith("/api/users")) {
            return true;
        }

        // Individual user record endpoint: /api/users/{id}
        if (path.matches("^/api/users/\\d+$")) {
            String idStr = path.substring("/api/users/".length());
            Long requestedId = Long.parseLong(idStr);
            if (user.getRole() != User.Role.ADMIN && !user.getId().equals(requestedId)) {
                log.warn("User {} attempted to access resource of user {}", user.getId(), requestedId);
                sendForbidden(response, "Access denied. You may only access your own account.");
                return false;
            }
            return true;
        }

        // List all users or other /api/users/** sub-paths: admin only
        if (user.getRole() != User.Role.ADMIN) {
            log.warn("Non-admin user {} attempted to access admin endpoint {} {}", user.getId(), method, path);
            sendForbidden(response, "Access denied. Administrator role required.");
            return false;
        }

        return true;
    }

    private void sendUnauthorized(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"" + message + "\"}");
    }

    private void sendForbidden(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"status\":403,\"error\":\"Forbidden\",\"message\":\"" + message + "\"}");
    }
}
