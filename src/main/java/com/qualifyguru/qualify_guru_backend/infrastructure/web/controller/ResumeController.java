package com.qualifyguru.qualify_guru_backend.infrastructure.web.controller;

import com.qualifyguru.qualify_guru_backend.application.usecase.UploadBaseResumeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/qualify-guru/api/v1/resumes")
public class ResumeController {

    private final UploadBaseResumeService uploadBaseResumeService;

    public ResumeController(UploadBaseResumeService uploadBaseResumeService) {
        this.uploadBaseResumeService = uploadBaseResumeService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadBaseResume(
            @RequestParam("file") MultipartFile file
    )throws IOException {

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        String fileKey = uploadBaseResumeService.execute(
                userEmail,
                file.getOriginalFilename(),
                file.getInputStream(),
                file.getContentType(),
                file.getSize());

        return ResponseEntity.ok(
                Map.of(
                        "message", "File saved successfully.",
                        "fileKey", fileKey
                )
        );
    }
}
