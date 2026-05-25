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
import com.tellme.model.User;
import com.tellme.service.interfaces.AspirasiService;

import jakarta.servlet.http.HttpServletRequest;

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
            @RequestParam(required = false) Long kategoriId,
            @RequestParam(required = false) String nama) {

        if (kategoriId != null) {
            return aspirasiService.getAspirasiByKategori(kategoriId, nama);
        }

        return aspirasiService.getAllAspirasi(nama);
    }

    @GetMapping("/my")
    public List<AspirasiResponse> getMyAspirasi(HttpServletRequest request) {
        User user = (User) request.getAttribute("currentUser");
        return aspirasiService.getAspirasiByUserId(user.getId());
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
    public Aspirasi prosesAspirasi(
            @PathVariable Long id,
            @RequestBody Map<String, Long> body
    ){

        Long kategoriId = body.get("kategoriId");
        Long statusId = body.get("statusId");

        return aspirasiService.prosesAspirasi(
                id,
                kategoriId,
                statusId
        );
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