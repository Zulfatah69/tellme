package com.tellme.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tellme.model.Aspirasi;

/**
 * Spring Data JPA repository for {@link Aspirasi} (submission) entities.
 *
 * <p>Provides query methods for filtering submissions by category, status,
 * and submitting user, as well as aggregate count queries for the dashboard.
 */
public interface AspirasiRepository extends JpaRepository<Aspirasi, Long> {

    /**
     * Returns all submissions belonging to the specified category.
     *
     * @param kategoriId the category's primary key
     * @return list of submissions in that category
     */
    List<Aspirasi> findByKategoriId(Long kategoriId);

    /**
     * Returns the total number of submissions in the specified status.
     *
     * @param statusId the status's primary key
     * @return count of submissions with that status
     */
    long countByStatusId(Long statusId);

    /**
     * Deletes all submissions authored by the specified user.
     * Used during account deletion to cascade-remove user content.
     *
     * @param userId the submitter's primary key
     */
    void deleteByUserId(Long userId);

    /**
     * Returns all submissions authored by the specified user.
     *
     * @param userId the submitter's primary key
     * @return list of the user's submissions
     */
    List<Aspirasi> findByUserId(Long userId);
}