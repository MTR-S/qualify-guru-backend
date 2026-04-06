package com.qualifyguru.qualify_guru_backend.unit.application.usecase;

import com.qualifyguru.qualify_guru_backend.application.port.out.FileParserPort;
import com.qualifyguru.qualify_guru_backend.application.port.out.FileStoragePort;
import com.qualifyguru.qualify_guru_backend.application.port.out.UserRepositoryPort;
import com.qualifyguru.qualify_guru_backend.application.usecase.FileParserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileParserServiceTest {

    @Mock
    private FileParserPort fileParserPort;

    @Mock
    private FileStoragePort fileStoragePort;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private FileParserService fileParserService;

    @Test
    @DisplayName("Should orchestrate the download, extraction, and saving of the curriculum content.")
    void shouldExtractAndSaveResumeContent() {
        // Arrange
        String userEmail = "dev@qualifyguru.com";
        String fileKey = "s3-file-key.pdf";
        InputStream mockStream = mock(InputStream.class);
        String fakeExtractedText = "Random User\nJava Developer\nSpring Boot";

        when(fileStoragePort.downloadFile(fileKey)).thenReturn(mockStream);
        when(fileParserPort.extractText(mockStream)).thenReturn(fakeExtractedText);

        // Act
        fileParserService.parseFile(userEmail, fileKey);

        // Assert
        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, Object>> mapCaptor = ArgumentCaptor.forClass(Map.class);

        verify(userRepositoryPort).updateParsedContent(eq(userEmail), eq(fileKey), mapCaptor.capture());

        Map<String, Object> savedMap = mapCaptor.getValue();
        assertEquals("EXTRACTION_COMPLETED", savedMap.get("status"));
        assertTrue(savedMap.get("raw_text").toString().contains("Random User"));
    }
}
