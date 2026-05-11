package com.tellme.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.tellme.dto.AspirasiResponse;
import com.tellme.dto.DashboardResponse;
import com.tellme.dto.TopKategoriResponse;
import com.tellme.dto.UpdateStatusRequest;
import com.tellme.model.Aspirasi;
import com.tellme.service.interfaces.AspirasiService;

@RestController
@RequestMapping("/api/aspirasi")
public class AspirasiController {

    @Autowired
    private AspirasiService aspirasiService;

    @PostMapping
    public Aspirasi createAspirasi(@RequestBody Aspirasi aspirasi) {
        return aspirasiService.createAspirasi(aspirasi);
    }

    @GetMapping
    public List<AspirasiResponse> getAspirasi(
            @RequestParam(required = false) Long kategoriId) {

        if (kategoriId != null) {
            return aspirasiService.getAspirasiByKategori(kategoriId);
        }

        return aspirasiService.getAllAspirasi();
    }

    @PutMapping("/{id}/status")
    public Aspirasi updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateStatusRequest request) {

        return aspirasiService.updateStatus(id, request.getStatusId());
    }

    @GetMapping("/dashboard")
    public DashboardResponse getDashboard() {
        return aspirasiService.getDashboard();
    }

    @GetMapping("/top-kategori")
    public TopKategoriResponse getTopKategori() {
        return aspirasiService.getTopKategori();
    }

    @DeleteMapping("/{id}")
    public void deleteAspirasi(@PathVariable Long id) {
        aspirasiService.deleteById(id);
    }

    @PutMapping("/{id}/proses")
    public Aspirasi proses(
            @PathVariable Long id,
            @RequestBody UpdateStatusRequest request) {

        Long kategoriId = request.getStatusId();

        return aspirasiService.prosesAspirasi(id, kategoriId);
    }

    @PutMapping("/{id}/feedback")
    public Aspirasi feedback(
            @PathVariable Long id,
            @RequestBody Map<String, Object> req) {

        Long statusId = Long.valueOf(req.get("statusId").toString());
        String feedback = req.get("feedback").toString();

        return aspirasiService.updateWithFeedback(id, statusId, feedback);
    }
}