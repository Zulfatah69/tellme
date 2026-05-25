package com.tellme.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tellme.model.Aspirasi;

public interface AspirasiRepository
        extends JpaRepository<Aspirasi, Long> {

    List<Aspirasi> findByKategoriId(
            Long kategoriId
    );

    long countByStatusId(
            Long statusId
    );

    void deleteByUserId(
            Long userId
    );

    List<Aspirasi> findByUserId(
            Long userId
    );
}