package com.tellme.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ForumCommentResponse {

    private Long id;

    private String isiKomentar;

    private LocalDateTime tanggal;

    private Long userId;

    private String namaUser;

    private Long parentCommentId;

    private Integer totalReply = 0;

    private List<ForumCommentResponse> replies =
            new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
    }

    public Long getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(Long parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public Integer getTotalReply() {
        return totalReply;
    }

    public void setTotalReply(Integer totalReply) {
        this.totalReply = totalReply;
    }

    public List<ForumCommentResponse> getReplies() {
        return replies;
    }

    public void setReplies(
            List<ForumCommentResponse> replies) {

        this.replies = replies;
    }
}