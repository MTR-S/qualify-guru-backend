package com.qualifyguru.qualify_guru_backend.infrastructure.persistence.mapper;

import com.qualifyguru.qualify_guru_backend.domain.model.User;
import com.qualifyguru.qualify_guru_backend.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface UserMapper {

    @Mapping(target = "profiles", ignore = true)
    User toDomain(UserEntity entity);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profiles", ignore = true)
    UserEntity toEntity(User domain);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profiles", ignore = true)
    void updateEntityFromDomain(User domain, @MappingTarget UserEntity entity);
}
