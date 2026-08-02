package com.tellme.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Represents a discussion post in the student forum.
 *
 * <p>Forum posts allow students to raise topics for open discussion.
 * Each post can receive multiple {@link ForumComment} responses,
 * which may themselves form one level of threaded replies.
 *
 * <p>The {@code comments} collection is excluded from JSON serialization
 * ({@code @JsonIgnore}) because comments are retrieved via a separate
 * endpoint with a richer DTO structure.
 */
@Entity
@Table(name = "forum_post")
public class ForumPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The text content of the post. */
    @Column(name = "isi_post", nullable = false, columnDefinition = "TEXT")
    private String isiPost;

    /** Timestamp of when the post was created. Set automatically by the service layer. */
    @Column(nullable = false)
    private LocalDateTime tanggal;

    /** The user who created this post. Password is excluded from serialization. */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"password", "token", "hibernateLazyInitializer", "handler"})
    private User user;

    /**
     * All comments on this post. Excluded from JSON to prevent circular references;
     * use the {@code /api/forum-comment/{postId}} endpoint instead.
     */
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ForumComment> comments;

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIsiPost() { return isiPost; }
    public void setIsiPost(String isiPost) { this.isiPost = isiPost; }

    public LocalDateTime getTanggal() { return tanggal; }
    public void setTanggal(LocalDateTime tanggal) { this.tanggal = tanggal; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<ForumComment> getComments() { return comments; }
    public void setComments(List<ForumComment> comments) { this.comments = comments; }
}