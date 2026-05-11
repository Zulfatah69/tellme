package com.tellme.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "status")
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String namaStatus;

    // getter setter
    public Long getId() { return id; }

    public String getNamaStatus() { return namaStatus; }

    public void setNamaStatus(String namaStatus) {
        this.namaStatus = namaStatus;
    }
}