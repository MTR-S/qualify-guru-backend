package com.qualifyguru.qualify_guru_backend.application.port.out;

import com.qualifyguru.qualify_guru_backend.domain.model.User;
import com.qualifyguru.qualify_guru_backend.domain.model.UserProfile;

import java.util.Map;
import java.util.Optional;

public interface UserRepositoryPort {
    User save(User user);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    void saveProfile(String userEmail, UserProfile userProfile);

    void updateParsedContent(String userEmail, String fileKey, Map<String, Object> parsedContent);
}
