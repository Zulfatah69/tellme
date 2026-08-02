package com.tellme.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * Utility class for password hashing and verification.
 *
 * <p><strong>Current implementation: BCrypt</strong> — all new passwords
 * are hashed with BCrypt (work factor 12), which is salted and adaptive.
 *
 * <p><strong>Backward compatibility:</strong> Existing SHA-256 hashes
 * (64-character hex strings from the v1.0 era) are automatically detected
 * and verified. On the next successful login, they are transparently upgraded
 * to BCrypt by the service layer.
 *
 * <p><strong>Detection heuristic:</strong>
 * <ul>
 *   <li>Starts with {@code $2} → BCrypt</li>
 *   <li>64-character lowercase hex string → SHA-256 (legacy)</li>
 *   <li>Anything else → plain-text (very early development builds)</li>
 * </ul>
 *
 * <p><strong>Migration path for administrators:</strong>
 * No manual action needed. Passwords are automatically upgraded to BCrypt
 * when each user next logs in successfully.
 */
public final class PasswordUtil {

    /** BCrypt work factor. Higher = slower hashing = more brute-force resistant. */
    private static final int BCRYPT_WORK_FACTOR = 12;

    private PasswordUtil() {
        // Utility class — do not instantiate
    }

    // =========================================================================
    // Public API
    // =========================================================================

    /**
     * Hashes a plain-text password using BCrypt.
     *
     * <p>Each call produces a unique hash (BCrypt generates a random salt internally).
     * Two calls with the same input will return different strings — use
     * {@link #verify(String, String)} to check passwords, never string equality.
     *
     * @param password the plain-text password; must not be {@code null}
     * @return a BCrypt hash string (e.g., {@code $2a$12$...})
     * @throws IllegalArgumentException if {@code password} is {@code null}
     */
    public static String hash(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password must not be null");
        }
        return BCrypt.hashpw(password, BCrypt.gensalt(BCRYPT_WORK_FACTOR));
    }

    /**
     * Verifies a plain-text password against a stored credential string.
     *
     * <p>Supports three stored formats transparently:
     * <ol>
     *   <li><strong>BCrypt</strong> — strings starting with {@code $2a$}, {@code $2b$}, {@code $2y$}</li>
     *   <li><strong>SHA-256 (legacy)</strong> — 64-character lowercase hex strings</li>
     *   <li><strong>Plain-text (very legacy)</strong> — any other string (direct equality)</li>
     * </ol>
     *
     * @param plainPassword  the raw password to verify; returns {@code false} if {@code null}
     * @param storedPassword the stored credential string; returns {@code false} if {@code null}
     * @return {@code true} if the password matches the stored credential
     */
    public static boolean verify(String plainPassword, String storedPassword) {
        if (plainPassword == null || storedPassword == null) {
            return false;
        }

        if (isBcrypt(storedPassword)) {
            return BCrypt.checkpw(plainPassword, storedPassword);
        }

        if (isSha256(storedPassword)) {
            return sha256Hex(plainPassword).equals(storedPassword);
        }

        // Legacy plain-text fallback (very early development builds only)
        return plainPassword.equals(storedPassword);
    }

    /**
     * Returns {@code true} if the stored password is already in BCrypt format.
     * Used by the service layer to decide whether an upgrade is needed.
     *
     * @param storedPassword the stored password string
     * @return {@code true} if BCrypt format
     */
    public static boolean isBcryptHash(String storedPassword) {
        return storedPassword != null && isBcrypt(storedPassword);
    }

    // =========================================================================
    // Private helpers
    // =========================================================================

    private static boolean isBcrypt(String stored) {
        return stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$");
    }

    private static boolean isSha256(String stored) {
        return stored.length() == 64 && stored.matches("[0-9a-f]+");
    }

    /**
     * Computes the SHA-256 hex digest of a string.
     * Used only for verifying legacy passwords — not for creating new hashes.
     */
    static String sha256Hex(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(hashBytes.length * 2);
            for (byte b : hashBytes) {
                String h = Integer.toHexString(0xff & b);
                if (h.length() == 1) hex.append('0');
                hex.append(h);
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }
}
