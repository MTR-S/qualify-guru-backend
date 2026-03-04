package com.qualifyguru.qualify_guru_backend.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Collections;

public class User {

    private final UUID publicId;

    private String email;
    private String passwordHash;

    private UserRole role;

    private final List<UserProfile> profiles;

    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(UUID publicId, String email, String passwordHash, UserRole role,
                List<UserProfile> profiles, LocalDateTime createdAt, LocalDateTime updatedAt) {

        this.publicId = publicId;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.profiles = profiles != null ? new ArrayList<>(profiles) : new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User createNewClient(String email, String passwordHash) {

        return new User(
                UUID.randomUUID(),
                email,
                passwordHash,
                UserRole.CLIENT,
                new ArrayList<>(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }


    public void changePassword(String newPasswordHash) {

        if (newPasswordHash == null || newPasswordHash.trim().isEmpty()) {
            throw new IllegalArgumentException("The password cannot be empty.");
        }
        this.passwordHash = newPasswordHash;
        this.updateTimestamp();
    }

    public void addProfile(UserProfile profile) {

        if (profile == null) throw new IllegalArgumentException("The profile cannot be null");

        this.profiles.add(profile);

        this.updateTimestamp();
    }

    private void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }


    public UUID getPublicId() { return publicId; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public UserRole getRole() { return role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public List<UserProfile> getProfiles() {

        return Collections.unmodifiableList(profiles);
    }
}
