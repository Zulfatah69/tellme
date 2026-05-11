package com.tellme.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tellme.model.Status;

public interface StatusRepository extends JpaRepository<Status, Long> {
}