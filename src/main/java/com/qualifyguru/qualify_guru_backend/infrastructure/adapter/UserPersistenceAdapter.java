package com.qualifyguru.qualify_guru_backend.infrastructure.adapter;

import com.qualifyguru.qualify_guru_backend.domain.model.User;
import com.qualifyguru.qualify_guru_backend.domain.port.out.UserRepositoryPort;
import com.qualifyguru.qualify_guru_backend.infrastructure.persistence.entity.UserEntity;
import com.qualifyguru.qualify_guru_backend.infrastructure.persistence.mapper.UserMapper;
import com.qualifyguru.qualify_guru_backend.infrastructure.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserPersistenceAdapter implements UserRepositoryPort {

    private final UserRepository repository;
    private final UserMapper userMapper;

    public UserPersistenceAdapter(UserRepository repository,
                                  UserMapper userMapper) {
        this.repository = repository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public User save(User user) {

        UserEntity entity = repository.findByPublicId(user.getPublicId())
                .orElseGet(UserEntity::new);

        userMapper.updateEntityFromDomain(user, entity);

        UserEntity savedEntity = repository.save(entity);

        return userMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository
                .findByEmail(email)
                .map(userMapper::toDomain);
    }
}
