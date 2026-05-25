package com.tellme.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "aspirasi")
public class Aspirasi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String isiAspirasi;

    private LocalDateTime tanggal;

    private Boolean anonim;

    private String emailTujuan;

    private String feedback;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "aspirasi_foto", joinColumns = @JoinColumn(name = "aspirasi_id"))
    @Column(name = "foto_path")
    private List<String> fotoPaths;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "kategori_id")
    private Kategori kategori;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

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

    public String getEmailTujuan() {
        return emailTujuan;
    }

    public void setEmailTujuan(String emailTujuan) {
        this.emailTujuan = emailTujuan;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public List<String> getFotoPaths() {
        return fotoPaths;
    }

    public void setFotoPaths(List<String> fotoPaths) {
        this.fotoPaths = fotoPaths;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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
}