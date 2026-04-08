package com.qualifyguru.qualify_guru_backend.application.usecase;

import com.qualifyguru.qualify_guru_backend.application.port.out.FileParserPort;
import com.qualifyguru.qualify_guru_backend.application.port.out.FileStoragePort;
import com.qualifyguru.qualify_guru_backend.application.port.out.UserRepositoryPort;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

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

        InputStream pdfStream = fileStoragePort.downloadFile(fileKey);

        String extractedText = fileParserPort.extractText(pdfStream);

        Map<String, Object> parsedContent = setParsedContent(extractedText);

        userRepositoryPort.updateParsedContent(userEmail, fileKey, parsedContent);
    }

    private Map<String, Object> setParsedContent(String extractedText) {
        Map<String, Object> parsedContent = new HashMap<>();

        parsedContent.put("raw_text", extractedText);
        parsedContent.put("status", "EXTRACTION_COMPLETED");

        return parsedContent;
    }
}
