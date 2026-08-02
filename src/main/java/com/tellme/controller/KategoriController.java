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
import com.tellme.model.Kategori;
import com.tellme.repository.KategoriRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller for submission category management.
 *
 * <p>Categories classify submissions (e.g. "Akademik", "Organisasi").
 * CRUD operations here are typically restricted to administrators.
 */
@Tag(name = "Categories (Kategori)", description = "Manage submission categories — admin only")
@RestController
@RequestMapping("/api/kategori")
public class KategoriController {

    private final KategoriRepository kategoriRepository;

    public KategoriController(KategoriRepository kategoriRepository) {
        this.kategoriRepository = kategoriRepository;
    }

    /**
     * Returns all available categories.
     */
    @GetMapping
    public ResponseEntity<List<Kategori>> getAll() {
        return ResponseEntity.ok(kategoriRepository.findAll());
    }

    /**
     * Creates a new category.
     *
     * @param kategori the category to create
     * @return the created category with HTTP 201
     */
    @PostMapping
    public ResponseEntity<Kategori> create(@RequestBody Kategori kategori) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(kategoriRepository.save(kategori));
    }

    /**
     * Updates an existing category's name.
     *
     * @param id      the category ID
     * @param updated the updated category data
     * @return the updated category
     */
    @PutMapping("/{id}")
    public ResponseEntity<Kategori> update(@PathVariable Long id, @RequestBody Kategori updated) {
        Kategori existing = kategoriRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));
        existing.setNamaKategori(updated.getNamaKategori());
        return ResponseEntity.ok(kategoriRepository.save(existing));
    }

    /**
     * Deletes a category by ID.
     *
     * @param id the category ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!kategoriRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category", id);
        }
        kategoriRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}