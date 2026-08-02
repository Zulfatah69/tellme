package com.tellme.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tellme.model.Status;

/**
 * Spring Data JPA repository for {@link Status} entities.
 *
 * <p>Statuses represent the lifecycle of a submission
 * (e.g., "Pending", "In Review", "Resolved", "Rejected").
 */
public interface StatusRepository extends JpaRepository<Status, Long> {
}