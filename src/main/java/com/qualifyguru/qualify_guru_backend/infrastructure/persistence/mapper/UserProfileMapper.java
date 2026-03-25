package com.qualifyguru.qualify_guru_backend.infrastructure.persistence.mapper;

import com.qualifyguru.qualify_guru_backend.domain.model.User;
import com.qualifyguru.qualify_guru_backend.domain.model.UserProfile;
import com.qualifyguru.qualify_guru_backend.infrastructure.persistence.entity.UserEntity;
import com.qualifyguru.qualify_guru_backend.infrastructure.persistence.entity.UserProfileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    @Mapping(target = "user", ignore = true)
    UserProfile toDomain(UserProfileEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    UserProfileEntity toEntity(UserProfile domain);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEntityFromDomain(UserProfile domain, @MappingTarget UserProfileEntity entity);
}
