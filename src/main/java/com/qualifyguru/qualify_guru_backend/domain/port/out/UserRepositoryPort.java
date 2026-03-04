package com.qualifyguru.qualify_guru_backend.domain.port.out;

import com.qualifyguru.qualify_guru_backend.domain.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findByEmail(String email);
}
