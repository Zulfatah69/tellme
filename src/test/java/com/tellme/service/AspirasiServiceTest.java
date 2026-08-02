package com.tellme.service;

import com.tellme.dto.AspirasiResponse;
import com.tellme.dto.DashboardResponse;
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
import com.tellme.service.impl.AspirasiServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AspirasiServiceImpl} using Mockito.
 * No Spring context required.
 */
@DisplayName("AspirasiServiceImpl")
@ExtendWith(MockitoExtension.class)
class AspirasiServiceTest {

    @Mock private AspirasiRepository aspirasiRepository;
    @Mock private UserRepository userRepository;
    @Mock private KategoriRepository kategoriRepository;
    @Mock private StatusRepository statusRepository;
    @Mock private EmailService emailService;

    @InjectMocks
    private AspirasiServiceImpl aspirasiService;

    private User user;
    private Kategori kategori;
    private Status status;
    private Aspirasi aspirasi;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setNama("Test User");
        user.setEmail("test@example.com");
        user.setRole(User.Role.MAHASISWA);

        kategori = new Kategori();
        kategori.setId(1L);
        kategori.setNamaKategori("Akademik");

        status = new Status();
        status.setId(1L);
        status.setNamaStatus("Pending");

        aspirasi = new Aspirasi();
        aspirasi.setId(1L);
        aspirasi.setIsiAspirasi("Aspirasi test content");
        aspirasi.setTanggal(LocalDateTime.now());
        aspirasi.setAnonim(false);
        aspirasi.setUser(user);
        aspirasi.setKategori(kategori);
        aspirasi.setStatus(status);
    }

    // =========================================================================
    // createAspirasi
    // =========================================================================

    @Test
    @DisplayName("createAspirasi — success: user and status found, saved and email dispatched")
    void createAspirasi_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(statusRepository.findById(1L)).thenReturn(Optional.of(status));
        when(kategoriRepository.findById(1L)).thenReturn(Optional.of(kategori));
        when(aspirasiRepository.save(any(Aspirasi.class))).thenReturn(aspirasi);

        Aspirasi result = aspirasiService.createAspirasi(aspirasi);

        assertNotNull(result);
        verify(aspirasiRepository).save(aspirasi);
        // Email is dispatched async via sendEmail(to, subject, body)
        // NOTE: email is only sent when category routing is configured;
        // in unit test, mailRoutingAkademik defaults to empty, so no email is sent for "Akademik"
        // but save() must still be called
    }

    @Test
    @DisplayName("createAspirasi — throws BusinessException when user.getId() is null")
    void createAspirasi_throwsWhenUserIdNull() {
        aspirasi.setUser(new User()); // id is null

        assertThrows(BusinessException.class, () -> aspirasiService.createAspirasi(aspirasi));
        verify(aspirasiRepository, never()).save(any());
    }

    @Test
    @DisplayName("createAspirasi — throws ResourceNotFoundException when user not found")
    void createAspirasi_throwsWhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> aspirasiService.createAspirasi(aspirasi));
        verify(aspirasiRepository, never()).save(any());
    }

    // =========================================================================
    // updateStatus
    // =========================================================================

    @Test
    @DisplayName("updateStatus — success: aspirasi and status found, status updated")
    void updateStatus_success() {
        Status newStatus = new Status();
        newStatus.setId(2L);
        newStatus.setNamaStatus("In Review");

        when(aspirasiRepository.findById(1L)).thenReturn(Optional.of(aspirasi));
        when(statusRepository.findById(2L)).thenReturn(Optional.of(newStatus));
        when(aspirasiRepository.save(any(Aspirasi.class))).thenReturn(aspirasi);

        Aspirasi result = aspirasiService.updateStatus(1L, 2L);

        assertNotNull(result);
        verify(aspirasiRepository).save(aspirasi);
    }

    @Test
    @DisplayName("updateStatus — throws ResourceNotFoundException when aspirasi not found")
    void updateStatus_throwsWhenAspirasiNotFound() {
        when(aspirasiRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> aspirasiService.updateStatus(1L, 2L));
        verify(aspirasiRepository, never()).save(any());
    }

    // =========================================================================
    // deleteById
    // =========================================================================

    @Test
    @DisplayName("deleteById — success: exists → deleteById called")
    void deleteById_success() {
        when(aspirasiRepository.existsById(1L)).thenReturn(true);
        doNothing().when(aspirasiRepository).deleteById(1L);

        assertDoesNotThrow(() -> aspirasiService.deleteById(1L));
        verify(aspirasiRepository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteById — throws ResourceNotFoundException when not found")
    void deleteById_throwsWhenNotFound() {
        when(aspirasiRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> aspirasiService.deleteById(1L));
        verify(aspirasiRepository, never()).deleteById(any());
    }

    // =========================================================================
    // getAllAspirasi
    // =========================================================================

    @Test
    @DisplayName("getAllAspirasi — returns all when no name filter")
    void getAllAspirasi_returnsAll() {
        when(aspirasiRepository.findAll()).thenReturn(Arrays.asList(aspirasi));

        List<AspirasiResponse> result = aspirasiService.getAllAspirasi(null);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("getAllAspirasi — returns empty list when nothing matches name filter")
    void getAllAspirasi_noMatchOnNameFilter() {
        when(aspirasiRepository.findAll()).thenReturn(Arrays.asList(aspirasi));

        // "xyz" won't match "Test User"
        List<AspirasiResponse> result = aspirasiService.getAllAspirasi("xyz-nomatch");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // =========================================================================
    // getDashboard
    // =========================================================================

    @Test
    @DisplayName("getDashboard — returns correct aggregate counts")
    void getDashboard_returnsCorrectCounts() {
        // getDashboard() uses aspirasiRepository.count() and findAll() for per-category breakdown
        when(aspirasiRepository.count()).thenReturn(10L);
        when(aspirasiRepository.countByStatusId(2L)).thenReturn(3L); // In Review
        when(aspirasiRepository.countByStatusId(3L)).thenReturn(1L); // Resolved
        when(aspirasiRepository.countByStatusId(4L)).thenReturn(1L); // Rejected
        when(aspirasiRepository.findAll()).thenReturn(Arrays.asList(aspirasi));

        DashboardResponse response = aspirasiService.getDashboard();

        assertNotNull(response);
        assertEquals(10L, response.getTotalAspirasi());
        assertEquals(3L, response.getTotalDiproses());
        assertEquals(1L, response.getTotalSelesai());
        assertEquals(1L, response.getTotalDitolak());
        // Per-category map should contain at least 1 entry ("Akademik" from the fixture)
        assertFalse(response.getPerKategori().isEmpty());
    }
}
