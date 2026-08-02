package com.tellme.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Represents a comment on a forum post, or a reply to another comment.
 *
 * <p>Comments support one level of threading: a comment may reference a
 * {@code parentComment} to indicate it is a reply. The UI should render
 * top-level comments (where {@code parentComment} is {@code null}) and
 * their replies separately.
 *
 * <p>Sensitive fields ({@code password}, {@code token}) are excluded from
 * JSON serialization of nested user objects to prevent data leakage.
 */
@Entity
@Table(name = "forum_comment")
public class ForumComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Text content of this comment. */
    @Column(name = "isi_komentar", nullable = false, columnDefinition = "TEXT")
    private String isiKomentar;

    /** Timestamp of when the comment was created. Set automatically by the service layer. */
    @Column(nullable = false)
    private LocalDateTime tanggal;

    /** The forum post this comment belongs to. */
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ForumPost post;

    /** The user who wrote this comment. Password and token are excluded from serialization. */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"password", "token", "hibernateLazyInitializer", "handler"})
    private User user;

    /**
     * Optional parent comment — if present, this comment is a reply.
     * The parent's own parent and post are excluded to prevent deep nesting in JSON.
     */
    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    @JsonIgnoreProperties({"post", "parentComment", "user", "hibernateLazyInitializer", "handler"})
    private ForumComment parentComment;

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIsiKomentar() { return isiKomentar; }
    public void setIsiKomentar(String isiKomentar) { this.isiKomentar = isiKomentar; }

    public LocalDateTime getTanggal() { return tanggal; }
    public void setTanggal(LocalDateTime tanggal) { this.tanggal = tanggal; }

    public ForumPost getPost() { return post; }
    public void setPost(ForumPost post) { this.post = post; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public ForumComment getParentComment() { return parentComment; }
    public void setParentComment(ForumComment parentComment) { this.parentComment = parentComment; }
}