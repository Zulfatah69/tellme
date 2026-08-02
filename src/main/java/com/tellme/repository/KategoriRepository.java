package com.tellme.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tellme.model.Kategori;

/**
 * Spring Data JPA repository for {@link Kategori} (category) entities.
 *
 * <p>Categories classify submissions into areas such as "Akademik"
 * (academic matters) or "Organisasi" (student organizations).
 */
public interface KategoriRepository extends JpaRepository<Kategori, Long> {
}