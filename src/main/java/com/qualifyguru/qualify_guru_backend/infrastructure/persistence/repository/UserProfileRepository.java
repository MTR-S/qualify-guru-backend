package com.qualifyguru.qualify_guru_backend.infrastructure.persistence.repository;

import com.qualifyguru.qualify_guru_backend.infrastructure.persistence.entity.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Long> {
    Optional<UserProfileEntity> findByUserId(Long id);
}
