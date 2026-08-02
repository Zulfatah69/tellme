package com.tellme.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Represents the lifecycle status of a submission.
 *
 * <p>Typical statuses and their IDs (seeded by initial data):
 * <ol>
 *   <li>Pending (default for new submissions)</li>
 *   <li>In Review</li>
 *   <li>Resolved</li>
 *   <li>Rejected</li>
 * </ol>
 *
 * <p>Status IDs 1–4 are referenced by ID in the service layer.
 * Changing the seed data order will break status transitions.
 */
@Entity
@Table(name = "status")
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Human-readable status name displayed in the UI (e.g., "Pending", "Resolved"). */
    @Column(name = "nama_status", nullable = false, unique = true)
    private String namaStatus;

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNamaStatus() { return namaStatus; }
    public void setNamaStatus(String namaStatus) { this.namaStatus = namaStatus; }
}