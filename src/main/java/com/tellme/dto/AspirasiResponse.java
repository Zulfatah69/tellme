package com.tellme.dto;

import java.time.LocalDateTime;

import com.tellme.model.Kategori;
import com.tellme.model.Status;

public class AspirasiResponse {

    private Long id;
    private String isiAspirasi;
    private LocalDateTime tanggal;
    private Boolean anonim;
    private Object user;
    private Kategori kategori;
    private Status status;
    private String feedback;
    private String emailTujuan;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsiAspirasi() {
        return isiAspirasi;
    }

    public void setIsiAspirasi(String isiAspirasi) {
        this.isiAspirasi = isiAspirasi;
    }

    public LocalDateTime getTanggal() {
        return tanggal;
    }

    public void setTanggal(LocalDateTime tanggal) {
        this.tanggal = tanggal;
    }

    public Boolean getAnonim() {
        return anonim;
    }

    public void setAnonim(Boolean anonim) {
        this.anonim = anonim;
    }

    public Object getUser() {
        return user;
    }

    public void setUser(Object user) {
        this.user = user;
    }

    public Kategori getKategori() {
        return kategori;
    }

    public void setKategori(Kategori kategori) {
        this.kategori = kategori;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getEmailTujuan() {
        return emailTujuan;
    }

    public void setEmailTujuan(String emailTujuan) {
        this.emailTujuan = emailTujuan;
    }
}