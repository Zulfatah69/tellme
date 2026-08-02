package com.tellme.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tellme.dto.AspirasiResponse;
import com.tellme.dto.DashboardResponse;
import com.tellme.dto.TopKategoriResponse;
import com.tellme.exception.BusinessException;
import com.tellme.exception.ResourceNotFoundException;
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

/**
 * Default implementation of {@link AspirasiService}.
 *
 * <p>Handles creation, retrieval, status management, and feedback for
 * submissions (aspirasi). Email notifications are dispatched asynchronously
 * via {@link EmailService}.
 */
@Service
public class AspirasiServiceImpl implements AspirasiService {

    private static final Logger log = LoggerFactory.getLogger(AspirasiServiceImpl.class);

    private static final long INITIAL_STATUS_ID = 1L;

    private final AspirasiRepository aspirasiRepository;
    private final UserRepository userRepository;
    private final KategoriRepository kategoriRepository;
    private final StatusRepository statusRepository;
    private final EmailService emailService;

    /** Email address that receives submissions from the 'Organisasi' category. */
    @Value("${tellme.mail.routing.organisasi:}")
    private String mailRoutingOrganisasi;

    /** Email address that receives submissions from the 'Akademik' category. */
    @Value("${tellme.mail.routing.akademik:}")
    private String mailRoutingAkademik;

    public AspirasiServiceImpl(AspirasiRepository aspirasiRepository,
                               UserRepository userRepository,
                               KategoriRepository kategoriRepository,
                               StatusRepository statusRepository,
                               EmailService emailService) {
        this.aspirasiRepository = aspirasiRepository;
        this.userRepository = userRepository;
        this.kategoriRepository = kategoriRepository;
        this.statusRepository = statusRepository;
        this.emailService = emailService;
    }

    /** {@inheritDoc} */
    @Override
    public Aspirasi createAspirasi(Aspirasi aspirasi) {
        if (aspirasi.getUser() == null || aspirasi.getUser().getId() == null) {
            throw new BusinessException("Submitter user information is required.");
        }

        User user = userRepository.findById(aspirasi.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", aspirasi.getUser().getId()));

        Status status = statusRepository.findById(INITIAL_STATUS_ID)
                .orElseThrow(() -> new ResourceNotFoundException("Status", INITIAL_STATUS_ID));

        aspirasi.setUser(user);
        aspirasi.setStatus(status);

        if (aspirasi.getKategori() != null && aspirasi.getKategori().getId() != null) {
            Kategori kategori = kategoriRepository.findById(aspirasi.getKategori().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", aspirasi.getKategori().getId()));
            aspirasi.setKategori(kategori);
        } else {
            aspirasi.setKategori(null);
        }

        aspirasi.setTanggal(LocalDateTime.now());
        Aspirasi saved = aspirasiRepository.save(aspirasi);
        dispatchNotificationEmail(saved);
        return saved;
    }

    /** {@inheritDoc} */
    @Override
    public List<AspirasiResponse> getAllAspirasi(String nameFilter) {
        return aspirasiRepository.findAll()
                .stream()
                .filter(a -> matchesNameFilter(a, nameFilter))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /** {@inheritDoc} */
    @Override
    public List<AspirasiResponse> getAspirasiByKategori(Long kategoriId, String nameFilter) {
        return aspirasiRepository.findByKategoriId(kategoriId)
                .stream()
                .filter(a -> matchesNameFilter(a, nameFilter))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /** {@inheritDoc} */
    @Override
    public Aspirasi updateStatus(Long id, Long statusId) {
        Aspirasi aspirasi = aspirasiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Submission", id));
        Status status = statusRepository.findById(statusId)
                .orElseThrow(() -> new ResourceNotFoundException("Status", statusId));
        aspirasi.setStatus(status);
        return aspirasiRepository.save(aspirasi);
    }

    /** {@inheritDoc} */
    @Override
    public DashboardResponse getDashboard() {
        DashboardResponse response = new DashboardResponse();
        response.setTotalAspirasi(aspirasiRepository.count());
        response.setTotalDiproses(aspirasiRepository.countByStatusId(2L));
        response.setTotalSelesai(aspirasiRepository.countByStatusId(3L));
        response.setTotalDitolak(aspirasiRepository.countByStatusId(4L));

        Map<String, Long> perCategory = new HashMap<>();
        aspirasiRepository.findAll().forEach(a -> {
            if (a.getKategori() != null) {
                String name = a.getKategori().getNamaKategori();
                perCategory.merge(name, 1L, Long::sum);
            }
        });
        response.setPerKategori(perCategory);
        return response;
    }

    /** {@inheritDoc} */
    @Override
    public TopKategoriResponse getTopKategori() {
        Map<String, Long> counts = new HashMap<>();
        aspirasiRepository.findAll().forEach(a -> {
            if (a.getKategori() != null) {
                counts.merge(a.getKategori().getNamaKategori(), 1L, Long::sum);
            }
        });

        return counts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(e -> new TopKategoriResponse(e.getKey(), e.getValue()))
                .orElse(new TopKategoriResponse(null, 0L));
    }

    /** {@inheritDoc} */
    @Override
    public void deleteById(Long id) {
        if (!aspirasiRepository.existsById(id)) {
            throw new ResourceNotFoundException("Submission", id);
        }
        aspirasiRepository.deleteById(id);
        log.info("Submission {} deleted", id);
    }

    /** {@inheritDoc} */
    @Override
    public Aspirasi prosesAspirasi(Long id, Long kategoriId, Long statusId) {
        Aspirasi aspirasi = aspirasiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Submission", id));
        Status status = statusRepository.findById(statusId)
                .orElseThrow(() -> new ResourceNotFoundException("Status", statusId));
        Kategori kategori = kategoriRepository.findById(kategoriId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", kategoriId));

        aspirasi.setStatus(status);
        aspirasi.setKategori(kategori);
        Aspirasi saved = aspirasiRepository.save(aspirasi);
        dispatchNotificationEmail(saved);
        return saved;
    }

    /** {@inheritDoc} */
    @Override
    public Aspirasi updateWithFeedback(Long id, Long statusId, String feedback) {
        Aspirasi aspirasi = aspirasiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Submission", id));
        Status status = statusRepository.findById(statusId)
                .orElseThrow(() -> new ResourceNotFoundException("Status", statusId));
        aspirasi.setStatus(status);
        aspirasi.setFeedback(feedback);
        return aspirasiRepository.save(aspirasi);
    }

    /** {@inheritDoc} */
    @Override
    public List<AspirasiResponse> getAspirasiByUserId(Long userId) {
        return aspirasiRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    /**
     * Returns {@code true} if the submission's submitter name contains the filter
     * string (case-insensitive), or if the filter is blank.
     */
    private boolean matchesNameFilter(Aspirasi a, String nameFilter) {
        if (nameFilter == null || nameFilter.trim().isEmpty()) {
            return true;
        }
        if (a.getUser() == null || a.getUser().getNama() == null) {
            return false;
        }
        return a.getUser().getNama().toLowerCase().contains(nameFilter.toLowerCase());
    }

    /**
     * Dispatches an asynchronous email notification when a submission is created
     * or its category is assigned. Routing is determined by category name and
     * configured via {@code tellme.mail.routing.*} properties.
     *
     * <p>Email failures are swallowed here because notification is a best-effort
     * operation and must not roll back the main transaction.
     *
     * @param a the saved submission entity
     */
    private void dispatchNotificationEmail(Aspirasi a) {
        if (a.getKategori() == null) {
            return;
        }

        String category = a.getKategori().getNamaKategori();
        String recipient = resolveEmailRecipient(category);

        if (recipient == null || recipient.isBlank()) {
            log.debug("No email routing configured for category '{}' — skipping notification", category);
            return;
        }

        String subject = "New Submission: " + category;
        String body = String.format(
                "A new submission in category '%s' has been received.%n%n"
                + "Content:%n%s%n%n"
                + "Current Status: %s",
                category,
                a.getIsiAspirasi(),
                a.getStatus() != null ? a.getStatus().getNamaStatus() : "Pending"
        );

        emailService.sendEmail(recipient, subject, body);
    }

    /**
     * Maps category names to configured email recipients.
     * Extend this method or replace it with a configurable map for additional categories.
     *
     * @param categoryName the submission category name
     * @return the recipient email, or {@code null} if not configured
     */
    private String resolveEmailRecipient(String categoryName) {
        if (categoryName == null) return null;
        return switch (categoryName.toLowerCase()) {
            case "organisasi" -> mailRoutingOrganisasi;
            case "akademik"   -> mailRoutingAkademik;
            default           -> null;
        };
    }

    /**
     * Maps a {@link Aspirasi} entity to a {@link AspirasiResponse} DTO.
     * Anonymises the user field when the submission is marked as anonymous.
     */
    private AspirasiResponse mapToResponse(Aspirasi a) {
        AspirasiResponse response = new AspirasiResponse();
        response.setId(a.getId());
        response.setIsiAspirasi(a.getIsiAspirasi());
        response.setTanggal(a.getTanggal());
        response.setAnonim(a.getAnonim());
        response.setUser(Boolean.TRUE.equals(a.getAnonim()) ? null : a.getUser());
        response.setKategori(a.getKategori());
        response.setStatus(a.getStatus());
        response.setFeedback(a.getFeedback());
        response.setEmailTujuan(a.getEmailTujuan());
        response.setFotoPaths(a.getFotoPaths());
        return response;
    }
}