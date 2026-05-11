package com.tellme.dto;

import java.util.Map;

public class DashboardResponse {

    private Long totalAspirasi;
    private Long totalDiproses;
    private Long totalSelesai;
    private Long totalDitolak;
    private Map<String, Long> perKategori;

    public Long getTotalAspirasi() { return totalAspirasi; }
    public void setTotalAspirasi(Long totalAspirasi) { this.totalAspirasi = totalAspirasi; }

    public Long getTotalDiproses() { return totalDiproses; }
    public void setTotalDiproses(Long totalDiproses) { this.totalDiproses = totalDiproses; }

    public Long getTotalSelesai() { return totalSelesai; }
    public void setTotalSelesai(Long totalSelesai) { this.totalSelesai = totalSelesai; }

    public Long getTotalDitolak() { return totalDitolak; }
    public void setTotalDitolak(Long totalDitolak) { this.totalDitolak = totalDitolak; }

    public Map<String, Long> getPerKategori() { return perKategori; }
    public void setPerKategori(Map<String, Long> perKategori) { this.perKategori = perKategori; }
}