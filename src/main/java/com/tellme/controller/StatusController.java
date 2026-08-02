package com.tellme.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tellme.exception.ResourceNotFoundException;
import com.tellme.model.Status;
import com.tellme.repository.StatusRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller for submission status management.
 *
 * <p>Statuses represent the lifecycle of a submission
 * (e.g. "Pending", "In Review", "Resolved", "Rejected").
 * CRUD operations here are typically restricted to administrators.
 */
@Tag(name = "Statuses", description = "Manage submission lifecycle statuses — admin only")
@RestController
@RequestMapping("/api/status")
public class StatusController {

    private final StatusRepository statusRepository;

    public StatusController(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    /**
     * Returns all available statuses.
     */
    @GetMapping
    public ResponseEntity<List<Status>> getAll() {
        return ResponseEntity.ok(statusRepository.findAll());
    }

    /**
     * Creates a new status.
     *
     * @param status the status to create
     * @return the created status with HTTP 201
     */
    @PostMapping
    public ResponseEntity<Status> create(@RequestBody Status status) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(statusRepository.save(status));
    }

    /**
     * Updates an existing status's name.
     *
     * @param id      the status ID
     * @param updated the updated status data
     * @return the updated status
     */
    @PutMapping("/{id}")
    public ResponseEntity<Status> update(@PathVariable Long id, @RequestBody Status updated) {
        Status existing = statusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Status", id));
        existing.setNamaStatus(updated.getNamaStatus());
        return ResponseEntity.ok(statusRepository.save(existing));
    }

    /**
     * Deletes a status by ID.
     *
     * @param id the status ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!statusRepository.existsById(id)) {
            throw new ResourceNotFoundException("Status", id);
        }
        statusRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}