package com.qualifyguru.qualify_guru_backend.infrastructure.persistence.repository;

import com.qualifyguru.qualify_guru_backend.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByPublicId(UUID publicId);
}
