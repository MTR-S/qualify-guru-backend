package com.qualifyguru.qualify_guru_backend.infrastructure.web.controller;

import com.qualifyguru.qualify_guru_backend.application.usecase.FileParserService;
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
    private final FileParserService fileParserService;

    public ResumeController(UploadBaseResumeService uploadBaseResumeService,
                            FileParserService fileParserService) {
        this.uploadBaseResumeService = uploadBaseResumeService;
        this.fileParserService = fileParserService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadBaseResume(
            @RequestParam("file") MultipartFile file
    )throws IOException {

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        String fileKey = uploadBaseResumeService.uploadResume(
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

    @PostMapping("/test-extract")
    public ResponseEntity<Map<String, String>> testExtraction(@RequestParam("fileKey") String fileKey) {
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

            fileParserService.parseFile(userEmail, fileKey);

            return ResponseEntity.ok(Map.of(
                    "status", "Success",
                    "message", "The PDF was read, the text extracted, and saved to the database."
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.internalServerError().body(Map.of("error", "Inside server error when extracting the text to PDF."));
        }
    }
}
