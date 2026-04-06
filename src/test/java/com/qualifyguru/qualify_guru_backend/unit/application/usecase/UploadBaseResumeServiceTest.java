package com.qualifyguru.qualify_guru_backend.unit.application.usecase;

import com.qualifyguru.qualify_guru_backend.application.port.out.FileStoragePort;
import com.qualifyguru.qualify_guru_backend.application.port.out.UserRepositoryPort;
import com.qualifyguru.qualify_guru_backend.application.usecase.UploadBaseResumeService;
import com.qualifyguru.qualify_guru_backend.domain.model.UserProfile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UploadBaseResumeServiceTest {

    @Mock
    private FileStoragePort fileStoragePort;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private UploadBaseResumeService uploadBaseResumeService;

    @Test
    @DisplayName("Should be able to successfully upload and save the profile if the file is a PDF.")
    void shouldUploadAndSaveProfileSuccessfully() {
        // Arrange
        String userEmail = "test@qualifyguru.com";
        String fileName = "resume.pdf";
        InputStream mockInputStream = mock(InputStream.class);
        String contentType = "application/pdf";
        long contentLength = 1024L;
        String expectedFileKey = "s3-generated-key-123";

        when(fileStoragePort.uploadAndReturnKey(fileName, mockInputStream, contentType, contentLength))
                .thenReturn(expectedFileKey);

        // Act
        String resultKey = uploadBaseResumeService.uploadResume(userEmail, fileName, mockInputStream, contentType, contentLength);

        assertEquals(expectedFileKey, resultKey);

        ArgumentCaptor<UserProfile> profileCaptor = ArgumentCaptor.forClass(UserProfile.class);
        verify(userRepositoryPort).saveProfile(eq(userEmail), profileCaptor.capture());

        UserProfile savedProfile = profileCaptor.getValue();

        assertTrue(savedProfile.getTitle().startsWith("Base profile - "));
        assertEquals(expectedFileKey, savedProfile.getOriginalResumeKey());
        assertNotNull(savedProfile.getCreatedAt());
        assertNotNull(savedProfile.getUpdatedAt());
    }

    @Test
    @DisplayName("Should throw an IllegalArgumentException when the contentType is not PDF.")
    void shouldThrowExceptionWhenContentTypeIsNotPdf() {
        // Arrange
        String userEmail = "test@qualifyguru.com";
        String fileName = "photo.png";
        InputStream mockInputStream = mock(InputStream.class);
        String invalidContentType = "image/png";
        long contentLength = 1024L;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            uploadBaseResumeService.uploadResume(userEmail, fileName, mockInputStream, invalidContentType, contentLength);
        });

        assertEquals("Only PDF files are allowed.", exception.getMessage());

        verify(fileStoragePort, never()).uploadAndReturnKey(anyString(), any(InputStream.class), anyString(), anyLong());
        verify(userRepositoryPort, never()).saveProfile(anyString(), any(UserProfile.class));
    }
}
