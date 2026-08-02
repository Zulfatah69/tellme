package com.tellme.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link PasswordUtil}.
 *
 * <p>Covers all three password formats supported:
 * <ol>
 *   <li>BCrypt (current default)</li>
 *   <li>SHA-256 hex (legacy v1.0 format)</li>
 *   <li>Plain-text (very early dev builds)</li>
 * </ol>
 */
@DisplayName("PasswordUtil")
class PasswordUtilTest {

    // =========================================================================
    // BCrypt hashing (current behavior)
    // =========================================================================

    @Nested
    @DisplayName("hash() — BCrypt")
    class HashTests {

        @Test
        @DisplayName("produces a BCrypt hash starting with $2a$")
        void hashReturnsBcryptFormat() {
            String hash = PasswordUtil.hash("myPassword");
            assertNotNull(hash);
            assertTrue(hash.startsWith("$2a$"), "BCrypt hashes must start with $2a$");
        }

        @Test
        @DisplayName("produces a unique hash each time (different salt)")
        void hashIsRandomlySalted() {
            String hash1 = PasswordUtil.hash("samePassword");
            String hash2 = PasswordUtil.hash("samePassword");
            assertNotEquals(hash1, hash2, "BCrypt should produce a different hash each time due to random salt");
        }

        @Test
        @DisplayName("throws IllegalArgumentException for null input")
        void hashThrowsForNull() {
            assertThrows(IllegalArgumentException.class, () -> PasswordUtil.hash(null));
        }
    }

    // =========================================================================
    // verify() — BCrypt passwords (current format)
    // =========================================================================

    @Nested
    @DisplayName("verify() — BCrypt")
    class VerifyBcryptTests {

        @Test
        @DisplayName("returns true when password matches the BCrypt hash")
        void verifyCorrectBcryptPassword() {
            String hash = PasswordUtil.hash("correctPassword");
            assertTrue(PasswordUtil.verify("correctPassword", hash));
        }

        @Test
        @DisplayName("returns false when password does not match the BCrypt hash")
        void verifyWrongBcryptPassword() {
            String hash = PasswordUtil.hash("correctPassword");
            assertFalse(PasswordUtil.verify("wrongPassword", hash));
        }

        @Test
        @DisplayName("returns false for null plain-text input")
        void verifyNullPlaintext() {
            String hash = PasswordUtil.hash("password");
            assertFalse(PasswordUtil.verify(null, hash));
        }

        @Test
        @DisplayName("returns false for null stored hash")
        void verifyNullStoredHash() {
            assertFalse(PasswordUtil.verify("password", null));
        }

        @Test
        @DisplayName("returns false for both inputs null")
        void verifyBothNull() {
            assertFalse(PasswordUtil.verify(null, null));
        }
    }

    // =========================================================================
    // verify() — SHA-256 passwords (legacy)
    // =========================================================================

    @Nested
    @DisplayName("verify() — SHA-256 legacy format")
    class VerifySha256Tests {

        /** Pre-computed SHA-256 hash of "password123" for test stability. */
        private static final String SHA256_OF_PASSWORD123 =
                "ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f";

        @Test
        @DisplayName("returns true when password matches the stored SHA-256 hex hash")
        void verifyCorrectSha256Password() {
            assertTrue(PasswordUtil.verify("password123", SHA256_OF_PASSWORD123));
        }

        @Test
        @DisplayName("returns false when password does not match the stored SHA-256 hex hash")
        void verifyWrongSha256Password() {
            assertFalse(PasswordUtil.verify("wrongPassword", SHA256_OF_PASSWORD123));
        }

        @Test
        @DisplayName("sha256Hex() produces a deterministic 64-character lowercase hex string")
        void sha256HexIsDeterministic() {
            String hash1 = PasswordUtil.sha256Hex("test");
            String hash2 = PasswordUtil.sha256Hex("test");
            assertEquals(hash1, hash2);
            assertEquals(64, hash1.length());
            assertTrue(hash1.matches("[0-9a-f]+"));
        }

        @Test
        @DisplayName("sha256Hex() produces different values for different inputs")
        void sha256HexDifferentInputs() {
            assertNotEquals(
                    PasswordUtil.sha256Hex("inputA"),
                    PasswordUtil.sha256Hex("inputB"));
        }
    }

    // =========================================================================
    // verify() — plain-text legacy passwords (very early builds)
    // =========================================================================

    @Nested
    @DisplayName("verify() — plain-text legacy fallback")
    class VerifyPlainTextTests {

        @Test
        @DisplayName("returns true when plain-text matches stored plain-text (legacy)")
        void verifyLegacyPlainTextMatch() {
            assertTrue(PasswordUtil.verify("myLegacyPassword", "myLegacyPassword"));
        }

        @Test
        @DisplayName("returns false when plain-text does not match stored plain-text (legacy)")
        void verifyLegacyPlainTextMismatch() {
            assertFalse(PasswordUtil.verify("wrong", "myLegacyPassword"));
        }
    }

    // =========================================================================
    // isBcryptHash() helper
    // =========================================================================

    @Nested
    @DisplayName("isBcryptHash()")
    class IsBcryptHashTests {

        @Test
        @DisplayName("returns true for a BCrypt hash")
        void detectsBcryptHash() {
            String hash = PasswordUtil.hash("any");
            assertTrue(PasswordUtil.isBcryptHash(hash));
        }

        @Test
        @DisplayName("returns false for a SHA-256 hash")
        void detectsSha256AsNotBcrypt() {
            assertFalse(PasswordUtil.isBcryptHash(
                    "ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f"));
        }

        @Test
        @DisplayName("returns false for null")
        void detectsNullAsNotBcrypt() {
            assertFalse(PasswordUtil.isBcryptHash(null));
        }
    }
}
