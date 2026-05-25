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
        if (aspirasi.getUser() == null || aspirasi.getUser().getId() == null) {
            throw new RuntimeException("Data user pengirim aspirasi wajib disertakan.");
        }

        User user = userRepository.findById(aspirasi.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

        Status status = statusRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Status tidak ditemukan"));

        aspirasi.setUser(user);
        aspirasi.setStatus(status);

        if (aspirasi.getKategori() != null && aspirasi.getKategori().getId() != null) {
            Kategori kategori = kategoriRepository.findById(aspirasi.getKategori().getId())
                    .orElseThrow(() -> new RuntimeException("Kategori tidak ditemukan"));
            aspirasi.setKategori(kategori);
        } else {
            aspirasi.setKategori(null);
        }

        aspirasi.setTanggal(LocalDateTime.now());
        Aspirasi saved = aspirasiRepository.save(aspirasi);
        sendAspirasiEmail(saved);
        return saved;
    }

    @Override
    public List<AspirasiResponse> getAllAspirasi(String nama) {
        return aspirasiRepository.findAll()
                .stream()
                .filter(a -> {
                    if (nama == null || nama.trim().isEmpty()) return true;
                    if (a.getUser() == null || a.getUser().getNama() == null) return false;
                    return a.getUser().getNama().toLowerCase().contains(nama.toLowerCase());
                })
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AspirasiResponse> getAspirasiByKategori(Long kategoriId, String nama) {
        return aspirasiRepository.findByKategoriId(kategoriId)
                .stream()
                .filter(a -> {
                    if (nama == null || nama.trim().isEmpty()) return true;
                    if (a.getUser() == null || a.getUser().getNama() == null) return false;
                    return a.getUser().getNama().toLowerCase().contains(nama.toLowerCase());
                })
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
        res.setTotalDiproses(aspirasiRepository.countByStatusId(2L));
        res.setTotalSelesai(aspirasiRepository.countByStatusId(3L));
        res.setTotalDitolak(aspirasiRepository.countByStatusId(4L));

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
    public Aspirasi prosesAspirasi(Long id, Long kategoriId, Long statusId) {
        Aspirasi aspirasi = aspirasiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aspirasi tidak ditemukan"));

        Status status = statusRepository.findById(statusId)
                .orElseThrow(() -> new RuntimeException("Status tidak ditemukan"));

        Kategori kategori = kategoriRepository.findById(kategoriId)
                .orElseThrow(() -> new RuntimeException("Kategori tidak ditemukan"));

        aspirasi.setStatus(status);
        aspirasi.setKategori(kategori);
        Aspirasi saved = aspirasiRepository.save(aspirasi);
        sendAspirasiEmail(saved);
        return saved;
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

    @Override
    public List<AspirasiResponse> getAspirasiByUserId(Long userId) {
        return aspirasiRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private void sendAspirasiEmail(Aspirasi a) {
        if (a.getKategori() == null) return;
        String toEmail = null;
        String namaKategori = a.getKategori().getNamaKategori();
        if ("Organisasi".equalsIgnoreCase(namaKategori)) {
            toEmail = "poke113333@gmail.com";
        } else if ("Akademik".equalsIgnoreCase(namaKategori)) {
            toEmail = "varroblake0@gmail.com";
        }

        if (toEmail != null) {
            final String finalTo = toEmail;
            final String subject = "Aspirasi Baru: " + namaKategori;
            final String body = "Aspirasi baru dengan kategori " + namaKategori + " telah masuk.\n\n" +
                               "Isi Aspirasi:\n" + a.getIsiAspirasi() + "\n\n" +
                               "Status saat ini: " + (a.getStatus() != null ? a.getStatus().getNamaStatus() : "Pending");
            new Thread(() -> {
                try {
                    emailService.sendEmail(finalTo, subject, body);
                } catch (Exception e) {
                }
            }).start();
        }
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
        res.setFotoPaths(a.getFotoPaths());

        return res;
    }
}