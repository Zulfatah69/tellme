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

/**
 * Represents a user submission — a piece of feedback, complaint, or suggestion
 * submitted by a student to the institution.
 *
 * <p>Submissions support:
 * <ul>
 *   <li><strong>Anonymity</strong> — when {@code anonim} is {@code true}, the
 *       submitter's identity is hidden in API responses</li>
 *   <li><strong>Category</strong> — optional classification (e.g., "Akademik", "Organisasi")
 *       that triggers email routing notifications</li>
 *   <li><strong>Status lifecycle</strong> — Pending → In Review → Resolved / Rejected</li>
 *   <li><strong>Admin feedback</strong> — a free-text response from administrators</li>
 *   <li><strong>Attachments</strong> — up to {@code tellme.upload.max-files} image files</li>
 * </ul>
 *
 * <p>The table and column names are in Indonesian to maintain backward compatibility
 * with existing deployments. English aliases are exposed in the DTO layer.
 */
@Entity
@Table(name = "aspirasi")
public class Aspirasi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The main content / body of the submission. */
    @Column(name = "isi_aspirasi", nullable = false, columnDefinition = "TEXT")
    private String isiAspirasi;

    /** Timestamp of when the submission was created. Set automatically by the service layer. */
    @Column(nullable = false)
    private LocalDateTime tanggal;

    /**
     * Whether the submission is anonymous.
     * When {@code true}, {@code user} is omitted from API responses.
     */
    @Column(nullable = false)
    private Boolean anonim = false;

    /**
     * Optional direct email target for this submission.
     * Used when the submitter wants to notify a specific address.
     */
    private String emailTujuan;

    /** Optional administrative feedback text written by an admin when resolving the submission. */
    @Column(columnDefinition = "TEXT")
    private String feedback;

    /** Relative URL paths to uploaded attachment images (stored in {@code aspirasi_foto}). */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "aspirasi_foto", joinColumns = @JoinColumn(name = "aspirasi_id"))
    @Column(name = "foto_path")
    private List<String> fotoPaths;

    /** The user who submitted this entry. */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * The category this submission belongs to.
     * May be {@code null} if the submitter did not select one, or may be
     * assigned later by an administrator during processing.
     */
    @ManyToOne
    @JoinColumn(name = "kategori_id")
    private Kategori kategori;

    /** Current lifecycle status of this submission. Defaults to "Pending" (id=1) on creation. */
    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIsiAspirasi() { return isiAspirasi; }
    public void setIsiAspirasi(String isiAspirasi) { this.isiAspirasi = isiAspirasi; }

    public LocalDateTime getTanggal() { return tanggal; }
    public void setTanggal(LocalDateTime tanggal) { this.tanggal = tanggal; }

    public Boolean getAnonim() { return anonim; }
    public void setAnonim(Boolean anonim) { this.anonim = anonim; }

    public String getEmailTujuan() { return emailTujuan; }
    public void setEmailTujuan(String emailTujuan) { this.emailTujuan = emailTujuan; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    public List<String> getFotoPaths() { return fotoPaths; }
    public void setFotoPaths(List<String> fotoPaths) { this.fotoPaths = fotoPaths; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Kategori getKategori() { return kategori; }
    public void setKategori(Kategori kategori) { this.kategori = kategori; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}