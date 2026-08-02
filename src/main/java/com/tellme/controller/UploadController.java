package com.tellme.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tellme.exception.BusinessException;

/**
 * REST controller that handles file uploads for submissions.
 */
@RestController
@RequestMapping("/api/upload")
public class UploadController {
    
    private static final Logger log = LoggerFactory.getLogger(UploadController.class);

    private final Path fileStorageLocation;
    private final int maxFiles;

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "pdf");

    public UploadController(@Value("${tellme.upload.dir:uploads}") String uploadDir,
                            @Value("${tellme.upload.max-files:5}") int maxFiles) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        this.maxFiles = maxFiles;
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @PostMapping
    public ResponseEntity<List<String>> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        if (files.length > maxFiles) {
            throw new BusinessException("Maximum " + maxFiles + " files allowed per request.");
        }

        List<String> filePaths = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;
            
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.contains("..")) {
                throw new BusinessException("Invalid file path sequence in filename: " + originalFilename);
            }

            String ext = "";
            int extIndex = originalFilename.lastIndexOf('.');
            if (extIndex > 0) {
                ext = originalFilename.substring(extIndex + 1).toLowerCase();
            }

            if (!ALLOWED_EXTENSIONS.contains(ext)) {
                throw new BusinessException("Unsupported file type: " + ext);
            }

            String newFilename = UUID.randomUUID().toString() + "." + ext;
            Path targetLocation = this.fileStorageLocation.resolve(newFilename);
            
            try {
                Files.copy(file.getInputStream(), targetLocation);
                filePaths.add("uploads/" + newFilename);
            } catch (IOException ex) {
                throw new BusinessException("Could not store file " + originalFilename + ". Please try again!");
            }
        }

        return ResponseEntity.ok(filePaths);
    }
}
