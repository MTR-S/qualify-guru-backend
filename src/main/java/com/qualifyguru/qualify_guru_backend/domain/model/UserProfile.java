package com.qualifyguru.qualify_guru_backend.domain.model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;


public class UserProfile {

    private Long id;

    private UUID publicId = UUID.randomUUID();

    private User user;

    private String title;

    private String originalResumeKey;

    private Map<String, Object> parsedBaseContent;

    private Map<String, Object> contactMetadata;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public UserProfile() {
    }

    public UserProfile(String title, String originalResumeKey, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.title = title;
        this.originalResumeKey = originalResumeKey;
        this.parsedBaseContent = parsedBaseContent;
        this.contactMetadata = contactMetadata;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UserProfile(Long id, UUID publicId, User user, String title, String originalResumeKey, Map<String, Object> parsedBaseContent, Map<String, Object> contactMetadata, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.publicId = publicId;
        this.user = user;
        this.title = title;
        this.originalResumeKey = originalResumeKey;
        this.parsedBaseContent = parsedBaseContent;
        this.contactMetadata = contactMetadata;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public UUID getPublicId() {
        return publicId;
    }

    public User getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public String getOriginalResumeKey() {
        return originalResumeKey;
    }

    public Map<String, Object> getParsedBaseContent() {
        return parsedBaseContent;
    }

    public Map<String, Object> getContactMetadata() {
        return contactMetadata;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPublicId(UUID publicId) {
        this.publicId = publicId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOriginalResumeKey(String originalResumeKey) {
        this.originalResumeKey = originalResumeKey;
    }

    public void setParsedBaseContent(Map<String, Object> parsedBaseContent) {
        this.parsedBaseContent = parsedBaseContent;
    }

    public void setContactMetadata(Map<String, Object> contactMetadata) {
        this.contactMetadata = contactMetadata;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}