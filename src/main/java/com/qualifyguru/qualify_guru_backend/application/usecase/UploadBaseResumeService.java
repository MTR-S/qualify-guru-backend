package com.qualifyguru.qualify_guru_backend.application.usecase;

import com.qualifyguru.qualify_guru_backend.application.port.out.FileStoragePort;
import com.qualifyguru.qualify_guru_backend.application.port.out.UserRepositoryPort;
import com.qualifyguru.qualify_guru_backend.domain.model.UserProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class UploadBaseResumeService {

    private final FileStoragePort fileStoragePort;
    private final UserRepositoryPort userRepositoryPort;

    private static final String ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE = "Only PDF files are allowed.";

    public UploadBaseResumeService(FileStoragePort fileStoragePort, UserRepositoryPort userRepositoryPort) {
        this.fileStoragePort = fileStoragePort;
        this.userRepositoryPort = userRepositoryPort;
    }

    public String uploadResume(String userEmail, String fileName, InputStream content,
                               String contentType, long contentLength) {

        log.info("Starting base resume upload process for user: {}. FileName: {}", userEmail, fileName);

        if (!"application/pdf".equals(contentType)) {
            log.warn("Upload rejected: Invalid content type ({}). Expected application/pdf.", contentType);

            throw new IllegalArgumentException(ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE);
        }

        log.info("Validation passed. Uploading file to S3 storage...");
        String fileKey = fileStoragePort.uploadAndReturnKey(fileName, content, contentType, contentLength);
        log.info("File successfully uploaded to S3. FileKey: {}", fileKey);

        String title = "Base profile - " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        LocalDateTime now = LocalDateTime.now();

        UserProfile domainUserProfile = new UserProfile(title, fileKey, now, now);

        userRepositoryPort.saveProfile(userEmail, domainUserProfile);
        log.info("Base profile saved to database successfully for user: {}. FileKey: {}", userEmail, fileKey);

        return fileKey;
    }
}