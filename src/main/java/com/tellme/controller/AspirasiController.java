package com.tellme.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tellme.dto.AspirasiResponse;
import com.tellme.dto.DashboardResponse;
import com.tellme.dto.TopKategoriResponse;
import com.tellme.dto.UpdateStatusRequest;
import com.tellme.model.Aspirasi;
import com.tellme.model.User;
import com.tellme.service.interfaces.AspirasiService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

/**
 * REST controller for student submission (aspirasi) operations.
 *
 * <p>Handles creating, listing, filtering, updating, and deleting submissions.
 * Admin-only endpoints include dashboard statistics and status management.
 */
@Tag(name = "Submissions (Aspirasi)", description = "Endpoints for creating, reading, updating, and deleting student submissions")
@RestController
@RequestMapping("/api/aspirasi")
public class AspirasiController {

    private final AspirasiService aspirasiService;

    public AspirasiController(AspirasiService aspirasiService) {
        this.aspirasiService = aspirasiService;
    }

    /**
     * Creates a new submission.
     *
     * @param aspirasi the submission payload
     * @return the created submission with HTTP 201
     */
    @Operation(summary = "Create a new submission")
    @PostMapping
    public ResponseEntity<Aspirasi> createAspirasi(@RequestBody Aspirasi aspirasi) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(aspirasiService.createAspirasi(aspirasi));
    }

    /**
     * Lists submissions, optionally filtered by category and/or submitter name.
     *
     * @param kategoriId optional category filter
     * @param nama       optional submitter name filter (case-insensitive, partial match)
     * @return list of submission responses
     */
    @Operation(summary = "List submissions with optional category and name filters")
    @GetMapping
    public ResponseEntity<List<AspirasiResponse>> getAspirasi(
            @Parameter(description = "Filter by category ID") @RequestParam(required = false) Long kategoriId,
            @Parameter(description = "Filter by submitter name (partial match)") @RequestParam(required = false) String nama) {

        List<AspirasiResponse> result = (kategoriId != null)
                ? aspirasiService.getAspirasiByKategori(kategoriId, nama)
                : aspirasiService.getAllAspirasi(nama);

        return ResponseEntity.ok(result);
    }

    /**
     * Returns the submissions belonging to the currently authenticated user.
     */
    @Operation(summary = "Get my submissions (authenticated user only)")
    @GetMapping("/my")
    public ResponseEntity<List<AspirasiResponse>> getMyAspirasi(HttpServletRequest request) {
        User user = (User) request.getAttribute("currentUser");
        return ResponseEntity.ok(aspirasiService.getAspirasiByUserId(user.getId()));
    }

    /**
     * Updates the lifecycle status of a submission. Admin only.
     *
     * @param id      the submission ID
     * @param request body containing the new status ID
     * @return the updated submission
     */
    @Operation(summary = "Update submission status (admin only)")
    @PutMapping("/{id}/status")
    public ResponseEntity<Aspirasi> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateStatusRequest request) {
        return ResponseEntity.ok(aspirasiService.updateStatus(id, request.getStatusId()));
    }

    /**
     * Returns aggregate dashboard statistics (total counts per status and category).
     * Admin only.
     */
    @Operation(summary = "Get dashboard statistics (admin only)")
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponse> getDashboard() {
        return ResponseEntity.ok(aspirasiService.getDashboard());
    }

    /**
     * Returns the category with the most submissions.
     */
    @Operation(summary = "Get the most popular submission category")
    @GetMapping("/top-kategori")
    public ResponseEntity<TopKategoriResponse> getTopKategori() {
        return ResponseEntity.ok(aspirasiService.getTopKategori());
    }

    /**
     * Deletes a submission by ID.
     *
     * @param id the submission ID
     */
    @Operation(summary = "Delete a submission")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAspirasi(@PathVariable Long id) {
        aspirasiService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Processes a submission: assigns a category and updates its status. Admin only.
     *
     * @param id   the submission ID
     * @param body map containing {@code kategoriId} and {@code statusId}
     * @return the updated submission
     */
    @Operation(summary = "Process a submission — assign category and status (admin only)")
    @PutMapping("/{id}/proses")
    public ResponseEntity<Aspirasi> prosesAspirasi(
            @PathVariable Long id,
            @RequestBody Map<String, Long> body) {

        Long kategoriId = body.get("kategoriId");
        Long statusId   = body.get("statusId");
        return ResponseEntity.ok(aspirasiService.prosesAspirasi(id, kategoriId, statusId));
    }

    /**
     * Adds admin feedback and updates the status of a submission. Admin only.
     *
     * @param id  the submission ID
     * @param req body containing {@code statusId} (Long) and {@code feedback} (String)
     * @return the updated submission
     */
    @Operation(summary = "Add feedback to a submission (admin only)")
    @PutMapping("/{id}/feedback")
    public ResponseEntity<Aspirasi> feedback(
            @PathVariable Long id,
            @RequestBody Map<String, Object> req) {

        Long statusId = Long.valueOf(req.get("statusId").toString());
        String feedback = req.get("feedback").toString();
        return ResponseEntity.ok(aspirasiService.updateWithFeedback(id, statusId, feedback));
    }
}