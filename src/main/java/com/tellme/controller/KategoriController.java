package com.tellme.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tellme.model.Kategori;
import com.tellme.repository.KategoriRepository;

@RestController
@RequestMapping("/api/kategori")
public class KategoriController {

    @Autowired
    private KategoriRepository kategoriRepository;

    @GetMapping
    public List<Kategori> getAll() {
        return kategoriRepository.findAll();
    }

    @PostMapping
    public Kategori create(@RequestBody Kategori k) {
        return kategoriRepository.save(k);
    }

    @PutMapping("/{id}")
    public Kategori update(@PathVariable Long id, @RequestBody Kategori k) {
        Kategori data = kategoriRepository.findById(id).orElseThrow();
        data.setNamaKategori(k.getNamaKategori());
        return kategoriRepository.save(data);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        kategoriRepository.deleteById(id);
    }
}