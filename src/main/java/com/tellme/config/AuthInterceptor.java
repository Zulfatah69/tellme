package com.tellme.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.tellme.model.User;
import com.tellme.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String path = request.getRequestURI();
        String method = request.getMethod();

        if (("/api/auth/login".equals(path) && "POST".equalsIgnoreCase(method)) ||
            ("/api/users/login".equals(path) && "POST".equalsIgnoreCase(method)) ||
            ("/api/users".equals(path) && "POST".equalsIgnoreCase(method))) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Token missing.");
            return false;
        }

        String token = authHeader.substring(7);
        User user = userRepository.findByToken(token);
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Invalid session token.");
            return false;
        }

        if (path.startsWith("/api/users") && !"POST".equalsIgnoreCase(method)) {
            if (path.matches("^/api/users/\\d+$")) {
                String idStr = path.substring(11);
                Long reqId = Long.parseLong(idStr);
                if (user.getRole() != User.Role.ADMIN && !user.getId().equals(reqId)) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("Forbidden: Access denied.");
                    return false;
                }
            } else if (user.getRole() != User.Role.ADMIN) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Forbidden: Admin role required.");
                return false;
            }
        }

        request.setAttribute("currentUser", user);
        return true;
    }
}
