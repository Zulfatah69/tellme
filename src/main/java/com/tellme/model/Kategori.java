package com.tellme.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Represents a submission category (e.g., "Akademik", "Organisasi").
 *
 * <p>Categories are used to classify submissions and route email notifications
 * to the appropriate recipient. The email routing is configured via
 * {@code tellme.mail.routing.*} application properties.
 */
@Entity
@Table(name = "kategori")
public class Kategori {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Display name of this category (e.g., "Akademik", "Organisasi"). */
    @Column(name = "nama_kategori", nullable = false, unique = true)
    private String namaKategori;

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNamaKategori() { return namaKategori; }
    public void setNamaKategori(String namaKategori) { this.namaKategori = namaKategori; }
}