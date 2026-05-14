package com.tellme.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "forum_post")
public class ForumPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String isiPost;

    private LocalDateTime tanggal;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({
        "password",
        "hibernateLazyInitializer",
        "handler"
    })
    private User user;

    @OneToMany(
        mappedBy = "post",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JsonIgnore
    private List<ForumComment> comments;

    public Long getId() {
        return id;
    }

    public String getIsiPost() {
        return isiPost;
    }

    public void setIsiPost(String isiPost) {
        this.isiPost = isiPost;
    }

    public LocalDateTime getTanggal() {
        return tanggal;
    }

    public void setTanggal(LocalDateTime tanggal) {
        this.tanggal = tanggal;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<ForumComment> getComments() {
        return comments;
    }

    public void setComments(List<ForumComment> comments) {
        this.comments = comments;
    }
}