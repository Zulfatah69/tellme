package com.tellme.dto;

import java.util.Map;

/**
 * Response DTO for the admin dashboard statistics endpoint
 * ({@code GET /api/aspirasi/dashboard}).
 *
 * <p>Contains aggregate counts of submissions by status and a
 * breakdown of submissions per category.
 */
public class DashboardResponse {

    /** Total number of submissions in all statuses. */
    private Long totalAspirasi;

    /** Number of submissions currently in the "In Review" status. */
    private Long totalDiproses;

    /** Number of submissions in the "Resolved" status. */
    private Long totalSelesai;

    /** Number of submissions in the "Rejected" status. */
    private Long totalDitolak;

    /**
     * Breakdown of submission counts per category name.
     * Keys are category names; values are the count of submissions in that category.
     */
    private Map<String, Long> perKategori;

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

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