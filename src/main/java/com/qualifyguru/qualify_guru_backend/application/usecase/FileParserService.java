package com.qualifyguru.qualify_guru_backend.application.usecase;

import com.qualifyguru.qualify_guru_backend.application.port.out.FileParserPort;
import com.qualifyguru.qualify_guru_backend.application.port.out.FileStoragePort;
import com.qualifyguru.qualify_guru_backend.application.port.out.UserRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class FileParserService {

    private final FileParserPort fileParserPort;
    private final FileStoragePort fileStoragePort;
    private final UserRepositoryPort userRepositoryPort;

    public FileParserService(FileParserPort fileParserPort,
                             FileStoragePort fileStoragePort,
                             UserRepositoryPort userRepositoryPort) {
        this.fileParserPort = fileParserPort;
        this.fileStoragePort = fileStoragePort;
        this.userRepositoryPort = userRepositoryPort;
    }

    public void parseFile(String userEmail, String fileKey) {
        log.info("Starting PDF text extraction. FileKey: {}, UserEmail: {}", fileKey, userEmail);

        InputStream pdfStream = fileStoragePort.downloadFile(fileKey);
        log.info("S3 download completed for FileKey: {}. Starting text extraction...", fileKey);

        String extractedText = fileParserPort.extractText(pdfStream);

        int textLength = (extractedText != null) ? extractedText.length() : 0;
        log.info("Text extracted successfully. Size: {} characters.", textLength);

        Map<String, Object> parsedContent = setParsedContent(extractedText);

        userRepositoryPort.updateParsedContent(userEmail, fileKey, parsedContent);
        log.info("Extracted content successfully saved to the database for FileKey: {}", fileKey);
    }

    private Map<String, Object> setParsedContent(String extractedText) {
        Map<String, Object> parsedContent = new HashMap<>();

        parsedContent.put("raw_text", extractedText);
        parsedContent.put("status", "EXTRACTION_COMPLETED");

        return parsedContent;
    }
}