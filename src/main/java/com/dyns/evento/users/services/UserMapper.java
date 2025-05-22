package com.dyns.evento.users.services;

import com.dyns.evento.users.User;
import com.dyns.evento.users.dtos.UserDto;
import com.dyns.evento.users.dtos.UserPatchRequest;
import com.dyns.evento.users.dtos.UserPostRequest;
import com.dyns.evento.users.dtos.UserPutRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserDto toDto(User entity);

    User toEntity(UserPostRequest dto);

    User toEntity(UserPatchRequest dto);

    User toEntity(UserPutRequest dto);
}
