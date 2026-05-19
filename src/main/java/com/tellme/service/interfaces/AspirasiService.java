package com.tellme.service.interfaces;

import java.util.List;

import com.tellme.dto.AspirasiResponse;
import com.tellme.dto.DashboardResponse;
import com.tellme.dto.TopKategoriResponse;
import com.tellme.model.Aspirasi;

public interface AspirasiService {

    Aspirasi createAspirasi(Aspirasi aspirasi);

    List<AspirasiResponse> getAllAspirasi();

    List<AspirasiResponse> getAspirasiByKategori(Long kategoriId);

    Aspirasi updateStatus(Long id, Long statusId);

    DashboardResponse getDashboard();

    TopKategoriResponse getTopKategori();

    void deleteById(Long id);

    Aspirasi prosesAspirasi(
            Long id,
            Long kategoriId,
            Long statusId
    );

    Aspirasi updateWithFeedback(Long id, Long statusId, String feedback);
}