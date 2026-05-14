package com.tellme.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tellme.dto.AspirasiResponse;
import com.tellme.dto.DashboardResponse;
import com.tellme.dto.TopKategoriResponse;
import com.tellme.model.Aspirasi;
import com.tellme.model.Kategori;
import com.tellme.model.Status;
import com.tellme.model.User;
import com.tellme.repository.AspirasiRepository;
import com.tellme.repository.KategoriRepository;
import com.tellme.repository.StatusRepository;
import com.tellme.repository.UserRepository;
import com.tellme.service.EmailService;
import com.tellme.service.interfaces.AspirasiService;

@Service
public class AspirasiServiceImpl implements AspirasiService {

    @Autowired
    private AspirasiRepository aspirasiRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KategoriRepository kategoriRepository;

    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private EmailService emailService;

    @Override
    public Aspirasi createAspirasi(Aspirasi aspirasi) {

        User user = userRepository.findById(aspirasi.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

        Status status = statusRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Status tidak ditemukan"));

        aspirasi.setUser(user);
        aspirasi.setStatus(status);
        aspirasi.setKategori(null);
        aspirasi.setTanggal(LocalDateTime.now());

        return aspirasiRepository.save(aspirasi);
    }

    @Override
    public List<AspirasiResponse> getAllAspirasi() {
        return aspirasiRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AspirasiResponse> getAspirasiByKategori(Long kategoriId) {
        return aspirasiRepository.findByKategoriId(kategoriId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Aspirasi updateStatus(Long id, Long statusId) {

        Aspirasi aspirasi = aspirasiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aspirasi tidak ditemukan"));

        Status status = statusRepository.findById(statusId)
                .orElseThrow(() -> new RuntimeException("Status tidak ditemukan"));

        aspirasi.setStatus(status);

        return aspirasiRepository.save(aspirasi);
    }

    @Override
    public DashboardResponse getDashboard() {

        DashboardResponse res = new DashboardResponse();

        res.setTotalAspirasi(aspirasiRepository.count());
        res.setTotalDiproses(aspirasiRepository.countByStatusId(1L));
        res.setTotalSelesai(aspirasiRepository.countByStatusId(2L));
        res.setTotalDitolak(aspirasiRepository.countByStatusId(3L));

        Map<String, Long> kategoriMap = new HashMap<>();

        aspirasiRepository.findAll().forEach(a -> {
            if (a.getKategori() != null) {
                String nama = a.getKategori().getNamaKategori();
                kategoriMap.put(nama, kategoriMap.getOrDefault(nama, 0L) + 1);
            }
        });

        res.setPerKategori(kategoriMap);

        return res;
    }

    @Override
    public TopKategoriResponse getTopKategori() {

        Map<String, Long> map = new HashMap<>();

        aspirasiRepository.findAll().forEach(a -> {
            if (a.getKategori() != null) {
                String nama = a.getKategori().getNamaKategori();
                map.put(nama, map.getOrDefault(nama, 0L) + 1);
            }
        });

        String topKategori = null;
        Long max = 0L;

        for (Map.Entry<String, Long> entry : map.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                topKategori = entry.getKey();
            }
        }

        return new TopKategoriResponse(topKategori, max);
    }

    @Override
    public void deleteById(Long id) {
        aspirasiRepository.deleteById(id);
    }

    @Override
    public Aspirasi prosesAspirasi(Long id, Long kategoriId) {

        Aspirasi aspirasi = aspirasiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aspirasi tidak ditemukan"));

        if (kategoriId == 0) {
            Status ditolak = statusRepository.findById(3L)
                    .orElseThrow(() -> new RuntimeException("Status tidak ditemukan"));

            aspirasi.setStatus(ditolak);

            return aspirasiRepository.save(aspirasi);
        }

        Kategori kategori = kategoriRepository.findById(kategoriId)
                .orElseThrow(() -> new RuntimeException("Kategori tidak ditemukan"));

        Status diproses = statusRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Status tidak ditemukan"));

        String emailTujuan = "";

        if (kategori.getNamaKategori().equalsIgnoreCase("Akademik")) {
            emailTujuan = "varroblake0@gmail.com";
        }

        if (kategori.getNamaKategori().equalsIgnoreCase("Organisasi")) {
            emailTujuan = "poke113333@gmail.com";
        }

        aspirasi.setKategori(kategori);
        aspirasi.setStatus(diproses);
        aspirasi.setEmailTujuan(emailTujuan);

        emailService.sendEmail(
                emailTujuan,
                "Aspirasi Baru - " + kategori.getNamaKategori(),
                "Isi Aspirasi:\n\n" + aspirasi.getIsiAspirasi()
        );

        return aspirasiRepository.save(aspirasi);
    }

    @Override
    public Aspirasi updateWithFeedback(Long id, Long statusId, String feedback) {

        Aspirasi aspirasi = aspirasiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aspirasi tidak ditemukan"));

        Status status = statusRepository.findById(statusId)
                .orElseThrow(() -> new RuntimeException("Status tidak ditemukan"));

        aspirasi.setStatus(status);
        aspirasi.setFeedback(feedback);

        return aspirasiRepository.save(aspirasi);
    }

    private AspirasiResponse mapToResponse(Aspirasi a) {

        AspirasiResponse res = new AspirasiResponse();

        res.setId(a.getId());
        res.setIsiAspirasi(a.getIsiAspirasi());
        res.setTanggal(a.getTanggal());
        res.setAnonim(a.getAnonim());

        if (Boolean.TRUE.equals(a.getAnonim())) {
            res.setUser(null);
        } else {
            res.setUser(a.getUser());
        }

        res.setKategori(a.getKategori());
        res.setStatus(a.getStatus());
        res.setFeedback(a.getFeedback());
        res.setEmailTujuan(a.getEmailTujuan());

        return res;
    }
}