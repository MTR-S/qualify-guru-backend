package com.qualifyguru.qualify_guru_backend.infrastructure.persistence.adapter;

import com.qualifyguru.qualify_guru_backend.domain.model.User;
import com.qualifyguru.qualify_guru_backend.application.port.out.UserRepositoryPort;
import com.qualifyguru.qualify_guru_backend.domain.model.UserProfile;
import com.qualifyguru.qualify_guru_backend.infrastructure.persistence.entity.UserEntity;
import com.qualifyguru.qualify_guru_backend.infrastructure.persistence.entity.UserProfileEntity;
import com.qualifyguru.qualify_guru_backend.infrastructure.persistence.mapper.UserMapper;
import com.qualifyguru.qualify_guru_backend.infrastructure.persistence.mapper.UserProfileMapper;
import com.qualifyguru.qualify_guru_backend.infrastructure.persistence.repository.UserProfileRepository;
import com.qualifyguru.qualify_guru_backend.infrastructure.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
public class UserPersistenceAdapter implements UserRepositoryPort {

    private static final String USER_NOT_FOUND = "User not found.";
    private static final String PROFILE_NOT_FOUND = "Profile not found for this file key.";

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;

    public UserPersistenceAdapter(UserRepository userRepository, UserMapper userMapper,
                                  UserProfileRepository userProfileRepository, UserProfileMapper userProfileMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;

        this.userProfileRepository = userProfileRepository;
        this.userProfileMapper = userProfileMapper;
    }

    @Override
    @Transactional
    public User save(User user) {

        UserEntity entity = userRepository.findByPublicId(user.getPublicId())
                .orElseGet(UserEntity::new);

        userMapper.updateEntityFromDomain(user, entity);

        UserEntity savedEntity = userRepository.save(entity);

        return userMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .map(userMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public void saveProfile(String userEmail, UserProfile userProfile) {

        UserEntity userEntity = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));

        UserProfileEntity userProfileEntity = userProfileMapper.toEntity(userProfile);

        userProfileEntity.setUser(userEntity);

        userProfileRepository.save(userProfileEntity);
    }

    @Override
    @Transactional
    public void updateParsedContent(String userEmail, String fileKey, Map<String, Object> parsedContent) {

        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));

        UserProfileEntity profile = userProfileRepository.findByUserIdAndOriginalResumeKey(user.getId(), fileKey)
                .orElseThrow(() -> new IllegalArgumentException(PROFILE_NOT_FOUND));

        profile.setParsedBaseContent(parsedContent);
        userProfileRepository.save(profile);
    }
}
