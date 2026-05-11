package com.tellme.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tellme.model.Kategori;

public interface KategoriRepository extends JpaRepository<Kategori, Long> {
}