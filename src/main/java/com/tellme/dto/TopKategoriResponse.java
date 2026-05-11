package com.tellme.dto;

public class TopKategoriResponse {

    private String kategori;
    private Long jumlah;

    public TopKategoriResponse(String kategori, Long jumlah) {
        this.kategori = kategori;
        this.jumlah = jumlah;
    }

    public String getKategori() {
        return kategori;
    }

    public Long getJumlah() {
        return jumlah;
    }
}