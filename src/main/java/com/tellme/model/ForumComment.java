package com.tellme.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "forum_comment")
public class ForumComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String isiKomentar;

    private LocalDateTime tanggal;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonIgnoreProperties({
        "hibernateLazyInitializer",
        "handler"
    })
    private ForumPost post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({
        "password",
        "hibernateLazyInitializer",
        "handler"
    })
    private User user;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    @JsonIgnoreProperties({
        "post",
        "parentComment",
        "user",
        "hibernateLazyInitializer",
        "handler"
    })
    private ForumComment parentComment;

    public Long getId() {
        return id;
    }

    public String getIsiKomentar() {
        return isiKomentar;
    }

    public void setIsiKomentar(String isiKomentar) {
        this.isiKomentar = isiKomentar;
    }

    public LocalDateTime getTanggal() {
        return tanggal;
    }

    public void setTanggal(LocalDateTime tanggal) {
        this.tanggal = tanggal;
    }

    public ForumPost getPost() {
        return post;
    }

    public void setPost(ForumPost post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ForumComment getParentComment() {
        return parentComment;
    }

    public void setParentComment(ForumComment parentComment) {
        this.parentComment = parentComment;
    }
}