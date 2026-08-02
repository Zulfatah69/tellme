package com.tellme.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Represents a registered user account.
 *
 * <p>Users may have one of two roles:
 * <ul>
 *   <li>{@link Role#MAHASISWA} — a student who can submit feedback and participate in forums</li>
 *   <li>{@link Role#ADMIN} — an administrator who can manage submissions, users, categories, and statuses</li>
 * </ul>
 *
 * <p>Authentication is token-based. On login, a UUID session token is generated
 * and stored in the {@code token} field. Logout nullifies this field.
 *
 * <p><strong>Password storage:</strong> Passwords are hashed with BCrypt (work factor 12).
 * Legacy SHA-256 hashes are transparently upgraded to BCrypt on first successful login.
 * See {@link com.tellme.util.PasswordUtil} for full details.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Full name of the user. */
    @Column(nullable = false)
    private String nama;

    /**
     * Student identification number (NIM).
     * Required for {@link Role#MAHASISWA} accounts.
     */
    @Column(unique = true)
    private String nim;

    /** Email address — must be unique across all accounts. */
    @Column(nullable = false, unique = true)
    private String email;

    /** SHA-256 hashed password. Never stored in plain text. */
    @Column(nullable = false)
    private String password;

    /** User role determining access level and permissions. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    /**
     * Opaque session token generated on login.
     * Set to {@code null} on logout.
     */
    @Column(unique = true)
    private String token;

    // -------------------------------------------------------------------------
    // Role enum
    // -------------------------------------------------------------------------

    /** Roles available in the system. */
    public enum Role {
        /** Student user — can submit feedback, use the forum, and view own submissions. */
        MAHASISWA,
        /** Administrator — full access to manage users, submissions, categories, and statuses. */
        ADMIN
    }

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getNim() { return nim; }
    public void setNim(String nim) { this.nim = nim; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}